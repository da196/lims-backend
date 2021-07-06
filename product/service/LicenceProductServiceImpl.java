package tz.go.tcra.lims.product.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.product.entity.ProductWorkflow;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.feestructure.dto.FeeStructureMinDto;
import tz.go.tcra.lims.product.dto.LicenceProductDto;
import tz.go.tcra.lims.product.dto.LicenceProductMaxDto;
import tz.go.tcra.lims.feestructure.dto.FeeStructureDto;
import tz.go.tcra.lims.licence.service.LicenseCategoryService;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.DuplicateException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.workflow.dto.WorkflowMinDto;
import tz.go.tcra.lims.workflow.entity.Workflow;
import tz.go.tcra.lims.workflow.repository.WorkflowRepository;
import tz.go.tcra.lims.workflow.service.WorkflowService;
import tz.go.tcra.lims.product.repository.ProductWorkflowRepository;
import tz.go.tcra.lims.feestructure.service.FeeStructureService;
import tz.go.tcra.lims.feestructure.repository.FeeStructureRepository;
import tz.go.tcra.lims.utils.exception.OperationNotAllowedException;
import tz.go.tcra.lims.workflow.entity.WorkflowType;
import tz.go.tcra.lims.licence.repository.LicenceCategoryRepository;
import tz.go.tcra.lims.product.repository.LicenceProductRepository;

/**
 * @author DonaldSj
 */
@Slf4j
@Service
public class LicenceProductServiceImpl implements LicenceProductService {

	@Autowired
	private LicenceProductRepository productRepo;

	@Autowired
	private LicenceCategoryRepository categoryRepo;

	@Autowired
	private ListOfValueRepository listOfValueRepo;

	@Autowired
	private FeeStructureRepository feeStructureRepo;

	@Autowired
	private WorkflowRepository workflowRepo;

	@Autowired
	private ProductWorkflowRepository productWorkflowRepo;

	@Autowired
	private LicenceProductAssembler assembler;

	@Autowired
	private LicenseCategoryService licenceCategoryService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private FeeStructureService feeStructureService;

	@Autowired
	private PagedResourcesAssembler<LicenceProduct> pagedResourcesAssembler;

	@Autowired
	private AppUtility utility;

	@Override
	@Transactional
	public Response<EntityModel<LicenceProduct>> saveLicenseProduct(LicenceProductDto data, Long id) {
		Response<EntityModel<LicenceProduct>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENSE PRODUCT SAVED SUCCESSFULLY", null);
		try {
			LimsUser actor = utility.getUser();
			LicenceProduct product = new LicenceProduct();
			if (id > 0L) {

				Optional<LicenceProduct> existing = productRepo.findById(id);

				if (!existing.isPresent()) {

					throw new DataNotFoundException("LICENCE PRODUCT NOT FOUND");
				}

				product = existing.get();
			}

			LicenceCategory category = categoryRepo.getOne(data.getCategoryID());
			product.setCode(data.getCode());
			product.setDisplayName(data.getDisplayName());
			product.setName(data.getName());
			product.setDescription(data.getDescription());
			product.setDuration(data.getDuration());
			product.setLicenseCategory(category);
			product.setCreatedBy(actor.getId());
			product = productRepo.saveAndFlush(product);

			if (data.getFeeStructure().size() > 0) {

                                feeStructureRepo.deactivateFeesByFeeable(product);
                                
				for (FeeStructureDto feeStructure : data.getFeeStructure()) {

					Optional<ListOfValue> currency = listOfValueRepo.findById(feeStructure.getFeeCurrency());
					if (!currency.isPresent()) {

						throw new DataNotFoundException("CURRENCY NOT VALID");
					}

					Optional<ListOfValue> feeType = listOfValueRepo.findById(feeStructure.getFeeType());
					if (!feeType.isPresent()) {

						throw new DataNotFoundException("FEE TYPE NOT VALID");
					}

					FeeStructure fee = new FeeStructure();
					if (feeStructure.getId() > 0) {

                                            Optional<FeeStructure> existing = feeStructureRepo.findById(feeStructure.getId());
                                            if (!existing.isPresent()) {

                                                    throw new DataNotFoundException(
                                                                    "FEE STRUCTURE DATA NOT FOUND [" + feeStructure.getId() + "]");
                                            }

                                            fee = existing.get();
                                            fee.setActive(Boolean.TRUE);
                                            fee.setUpdatedBy(actor.getId());
                                            fee.setUpdatedAt(LocalDateTime.now());
                                            
					}else{
                                        
                                            fee.setCreatedBy(actor.getId());
                                            fee.setCreatedAt(LocalDateTime.now());
                                        }

                                        fee.setCode(feeStructure.getCode());
                                        fee.setAccountCode(feeStructure.getAccountCode());
					fee.setName(feeStructure.getName());
					fee.setFrequency(feeStructure.getFrequency());
					fee.setFeePercent(feeStructure.getFeePercent());
					fee.setFeeAmount(feeStructure.getFeeAmount());
					fee.setPeriod(feeStructure.getPeriod());
					fee.setFeeCurrency(currency.get());
					fee.setApplicableState(feeStructure.getApplicableState());
					fee.setFeeType(feeType.get());
					fee.setFeeable(product);
					fee = feeStructureRepo.saveAndFlush(fee);
				}
			}

			if (data.getWorkflows().size() > 0) {

				productWorkflowRepo.deActivateByProductable(Boolean.FALSE, product.getId(),product.getProductType());
                                List<WorkflowType> workflowTypes=new ArrayList();
				for (Long ln : data.getWorkflows()) {

					Optional<Workflow> workflowExistance = workflowRepo.findById(ln);
					if (!workflowExistance.isPresent()) {

						throw new DataNotFoundException("WORKFLOW DETAILS NOT FOUND");
					}
                                        
                                        Workflow workflow=workflowExistance.get();
                                        
                                        if(workflowTypes.contains(workflow.getWorkflowType())){
                                        
                                            throw new OperationNotAllowedException("DUPLICATE IDENTICAL WORKFLOW TYPES MAPPED TO A PRODUCT");
                                        }
                                        
					ProductWorkflow productWorkflow = new ProductWorkflow();

					// check existance of workflow mapping
					Optional<ProductWorkflow> existance = productWorkflowRepo.findByProductableIdAndProductableTypeAndWorkflow(product.getId(),product.getProductType(),
							workflow);

					if (existance.isPresent()) {

						productWorkflow = existance.get();
					}

					productWorkflow.setActive(Boolean.TRUE);
					productWorkflow.setWorkflow(workflow);
					productWorkflow.setProductable(product);
					productWorkflowRepo.saveAndFlush(productWorkflow);
                                        
                                        workflowTypes.add(workflow.getWorkflowType());
				}
			}

			// save audit
			String activity = id > 0 ? "NEW Licence Product [ " + product.getName() + "] Saved "
					: "Licence Product [ " + product.getName() + "] Updated";
			utility.saveAudit(activity, actor);

			response.setData(assembler.toModel(product));

		} catch (ConstraintViolationException e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new DuplicateException("DUPLICATE RECORD");

		} catch (DataNotFoundException e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new DataNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<LicenceProductMaxDto> findLicenseProductById(Long id) {
		Response<LicenceProductMaxDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENSE PRODUCT DETAILS RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<LicenceProduct> data = productRepo.findById(id);

			if (!data.isPresent()) {

				response.setMessage("LICENSE PRODUCT DETAILS NOT FOUND");
				return response;
			}

			LicenceProduct prd = data.get();
			LicenceProductMaxDto product = new LicenceProductMaxDto();
			product.setId(prd.getId());
			product.setCode(prd.getCode());
			product.setName(prd.getName());
			product.setDisplayName(prd.getDisplayName());
			product.setDescription(prd.getDescription());
			product.setDuration(prd.getDuration());
			product.setCategory(licenceCategoryService.composeLicenseCategoryMinDto(prd.getLicenseCategory()));

			List<WorkflowMinDto> workflows = new ArrayList();
			for (ProductWorkflow productWorkflow : prd.getWorkflows()) {

				workflows.add(workflowService.composeWorkflowMinDto(productWorkflow.getWorkflow()));
			}

			product.setWorkflows(workflows);

			List<FeeStructureMinDto> fees = new ArrayList();
			for (FeeStructure feeStructure : prd.getFeeStructures()) {

				fees.add(feeStructureService.composeFeeStructureMinDto(feeStructure));
			}

			product.setFeeStructures(fees);

			response.setData(product);

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<LicenceProduct>> activateDeactivateLicenseProductById(Long id) {
		Response<EntityModel<LicenceProduct>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENSE PRODUCT DETAILS DELETED SUCCESSFULLY", null);
		try {
			LimsUser actor = utility.getUser();

			LicenceProduct data = productRepo.getOne(id);
			Boolean active = data.getActive() ? false : true;
			data.setActive(active);
			data.setUpdatedBy(actor.getId());
			data.setUpdatedAt(LocalDateTime.now());
			data = productRepo.saveAndFlush(data);

			response.setData(assembler.toModel(data));

		} catch (EntityNotFoundException e) {

			log.error(e.getMessage());
			e.printStackTrace();
			response.setMessage("LICENSE PRODUCT DETAILS NOT FOUND");

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<LicenceProduct>>> getListOfLicenseProducts(String keyword,
			Pageable pageable) {
		Response response = new Response<>(ResponseCode.SUCCESS, true, "LICENSE PRODUCTS RETRIEVED SUCCESSFULLY", null);
		try {

			Page<LicenceProduct> data = null;
			if (keyword.equalsIgnoreCase("All")) {
				data = productRepo.findByActive(true, pageable);
			} else {

				data = productRepo.findByKeywordActive(keyword, true, pageable);
			}

			if (data == null) {

				response.setMessage("LICENSE PRODUCTS NOT FOUND");
				return response;
			}

			response.setData(pagedResourcesAssembler.toModel(data));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<LicenceProduct>>> getAllLicenseProducts() {
		Response<CollectionModel<EntityModel<LicenceProduct>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENSE PRODUCTS RETRIEVED SUCCESSFULLY", null);
		try {

			List<LicenceProduct> data = productRepo.findAll();

			if (data.size() == 0) {

				response.setMessage("LICENSE PRODUCTS NOT FOUND");
				return response;
			}

			response.setData(assembler.toCollectionModel(data));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
		return response;
	}
}
