/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.product.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.product.entity.ProductWorkflow;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.feestructure.dto.FeeStructureDto;
import tz.go.tcra.lims.feestructure.dto.FeeStructureMinDto;
import tz.go.tcra.lims.product.repository.ProductWorkflowRepository;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.product.dto.EntityProductDto;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.product.repository.EntityProductRepository;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.DuplicateException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.workflow.entity.Workflow;
import tz.go.tcra.lims.workflow.repository.WorkflowRepository;
import tz.go.tcra.lims.workflow.service.WorkflowService;
import tz.go.tcra.lims.feestructure.service.FeeStructureService;
import tz.go.tcra.lims.feestructure.repository.FeeStructureRepository;
import tz.go.tcra.lims.product.dto.EntityProductMaxDto;
import tz.go.tcra.lims.product.entity.EntityProduct;
import tz.go.tcra.lims.utils.exception.OperationNotAllowedException;
import tz.go.tcra.lims.workflow.dto.WorkflowMinDto;
import tz.go.tcra.lims.workflow.entity.WorkflowType;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class EntityProductServiceImpl implements EntityProductService{

    @Autowired
    private EntityProductRepository productRepo;

    @Autowired
    private ListOfValueRepository listOfValueRepo;

    @Autowired
    private FeeStructureRepository feeStructureRepo;

    @Autowired
    private WorkflowRepository workflowRepo;

    @Autowired
    private ProductWorkflowRepository productWorkflowRepo;

    @Autowired
    private EntityProductAssembler assembler;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private FeeStructureService feeStructureService;

    @Autowired
    private PagedResourcesAssembler<EntityProduct> pagedResourcesAssembler;

    @Autowired
    private AppUtility utility;
        
    @Override
    @Transactional
    public Response<EntityModel<EntityProduct>> saveEntityProduct(EntityProductDto data, Long id) {
        Response<EntityModel<EntityProduct>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITY PRODUCT SAVED SUCCESSFULLY", null);
		try {
			LimsUser actor = utility.getUser();
			EntityProduct product = new EntityProduct();
			if (id > 0L) {

				Optional<EntityProduct> existing = productRepo.findById(id);

				if (!existing.isPresent()) {

					throw new DataNotFoundException("ENTITY PRODUCT NOT FOUND");
				}

				product = existing.get();
			}

			product.setCode(data.getCode());
			product.setDisplayName(data.getDisplayName());
			product.setName(data.getName());
                        product.setApplicationType(data.getApplicationType());
			product.setDescription(data.getDescription());
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
					}

                                        fee.setCode(feeStructure.getCode());
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
			String activity = id > 0 ? "NEW Entity Product [ " + product.getName() + "] Saved "
					: "Entity Product [ " + product.getName() + "] Updated";
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
    public Response<EntityProductMaxDto> findEntityProductById(Long id) {
        Response<EntityProductMaxDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITY PRODUCTS DETAILS RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<EntityProduct> data = productRepo.findById(id);

			if (!data.isPresent()) {

				response.setMessage("ENTITY PRODUCT DETAILS NOT FOUND");
				return response;
			}

			EntityProduct prd = data.get();
			EntityProductMaxDto product = new EntityProductMaxDto();
			product.setId(prd.getId());
			product.setCode(prd.getCode());
			product.setName(prd.getName());
			product.setDisplayName(prd.getDisplayName());
			product.setDescription(prd.getDescription());
			product.setApplicationType(prd.getApplicationType());
                        
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
    public Response<EntityModel<EntityProduct>> activateDeactivateEntityProductById(Long id) {
        Response<EntityModel<EntityProduct>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITY PRODUCT DETAILS ACTIVATED/DEACTIVATED SUCCESSFULLY", null);
		try {
			LimsUser actor = utility.getUser();

			Optional<EntityProduct> dataExisting = productRepo.findById(id);
                        if(!dataExisting.isPresent()){
                        
                            throw new DataNotFoundException("ENTITY PRODUCT NOT FOUND");
                        }
                        
                        EntityProduct data=dataExisting.get();
			Boolean active = data.getActive() ? false : true;
			data.setActive(active);
			data.setUpdatedBy(actor.getId());
			data.setUpdatedAt(LocalDateTime.now());
			data = productRepo.saveAndFlush(data);

			response.setData(assembler.toModel(data));

		} catch (DataNotFoundException e) {

                    log.error(e.getMessage());
                    throw new DataNotFoundException(e.getLocalizedMessage());
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
		return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<EntityProduct>>> getListOfEntityProducts(String keyword, Pageable pageable) {
        Response response = new Response<>(ResponseCode.SUCCESS, true, "ENTITY PRODUCTS RETRIEVED SUCCESSFULLY", null);
		try {

			Page<EntityProduct> data = null;
			if (keyword.equalsIgnoreCase("All")) {
				data = productRepo.findByActive(true, pageable);
			} else {

                            data = productRepo.findByKeywordActive(keyword, true, pageable);
			}

			if (data == null) {

                            response.setMessage("ENTITY PRODUCTS NOT FOUND");
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
    public Response<CollectionModel<EntityModel<EntityProduct>>> getAllEntityProducts() {
        Response<CollectionModel<EntityModel<EntityProduct>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITY PRODUCTS RETRIEVED SUCCESSFULLY", null);
            try {

                List<EntityProduct> data = productRepo.findAll();

                if (data.size() == 0) {

                        response.setMessage("ENTITY PRODUCTS NOT FOUND");
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
