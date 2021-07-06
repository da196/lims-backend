package tz.go.tcra.lims.portal.application.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.entity.GeoLocation;
import tz.go.tcra.lims.entity.IndividualLicenceApplicationContentResource;
import tz.go.tcra.lims.entity.IndividualLicenceApplicationDetail;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.entity.LicenceAcquiredSpectrum;
import tz.go.tcra.lims.entity.LicenceApplicationCoverage;
import tz.go.tcra.lims.entity.LicenceApplicationEntity;
import tz.go.tcra.lims.entity.LicenceApplicationServiceDetail;
import tz.go.tcra.lims.entity.LicenceApplicationShareholder;
import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.entity.LicenceCategoryServiceDetail;
import tz.go.tcra.lims.entity.LicenceServiceDetail;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.feestructure.repository.FeeStructureRepository;
import tz.go.tcra.lims.feestructure.service.FeeStructureService;
import tz.go.tcra.lims.geolocation.dto.GeoLocationMinDto;
import tz.go.tcra.lims.geolocation.repository.GeoLocationRepository;
import tz.go.tcra.lims.geolocation.service.GeoLocationService;
import tz.go.tcra.lims.licence.dto.LicenceCancellationDto;
import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;
import tz.go.tcra.lims.licence.dto.SpectrumValueDto;
import tz.go.tcra.lims.licence.repository.LicenceAcquiredSpectrumRepository;
import tz.go.tcra.lims.licence.repository.LicenceApplicationCoverageRepository;
import tz.go.tcra.lims.licence.repository.LicenceApplicationEntityRepository;
import tz.go.tcra.lims.licence.repository.LicenceApplicationEntityShareholderRepository;
import tz.go.tcra.lims.licence.repository.LicenceApplicationIndividualContentResourceRepository;
import tz.go.tcra.lims.licence.repository.LicenceApplicationIndividualDetailRepository;
import tz.go.tcra.lims.licence.repository.LicenceApplicationServiceRepository;
import tz.go.tcra.lims.licence.repository.LicenceCategoryRepository;
import tz.go.tcra.lims.licence.repository.LicenceCategoryServiceRepository;
import tz.go.tcra.lims.licence.repository.LicenceDetailRepository;
import tz.go.tcra.lims.licence.repository.LicenceRepository;
import tz.go.tcra.lims.licence.service.LicenseCategoryService;
import tz.go.tcra.lims.licence.service.LicenseDetailService;
import tz.go.tcra.lims.licence.service.LicenseService;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.licencee.entity.LicenceeEntityShareholder;
import tz.go.tcra.lims.licencee.repository.EntityApplicationRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityRepository;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.miscellaneous.enums.BillingStatusEnum;
import tz.go.tcra.lims.miscellaneous.enums.LicenceApplicationStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.OperationalEnum;
import tz.go.tcra.lims.miscellaneous.enums.LicenceStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.miscellaneous.repository.StatusRepository;
import tz.go.tcra.lims.payment.dto.BillingChargesDto;
import tz.go.tcra.lims.payment.dto.BillingReceiptDto;
import tz.go.tcra.lims.payment.dto.ControlNumberAvailability;
import tz.go.tcra.lims.payment.entity.Billing;
import tz.go.tcra.lims.payment.entity.BillingCharges;
import tz.go.tcra.lims.payment.repository.BillingChargesRepository;
import tz.go.tcra.lims.payment.repository.BillingRepository;
import tz.go.tcra.lims.payment.service.BillingService;
import tz.go.tcra.lims.payment.service.MoneyInWords;
import tz.go.tcra.lims.portal.application.dto.ActivityPortalDto;
import tz.go.tcra.lims.portal.application.dto.ApplicantEntityDto;
import tz.go.tcra.lims.portal.application.dto.IndividualLicenseApplicationDto;
import tz.go.tcra.lims.portal.application.dto.LicencePortalMaxDto;
import tz.go.tcra.lims.portal.application.dto.LicencePortalMinDto;
import tz.go.tcra.lims.portal.application.dto.LicenseCategoryDto;
import tz.go.tcra.lims.portal.application.dto.PayableFeesDto;
import tz.go.tcra.lims.portal.application.dto.PresentationPortalDto;
import tz.go.tcra.lims.portal.application.dto.PresentationPortalRequestDto;
import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.product.repository.LicenceProductRepository;
import tz.go.tcra.lims.task.dto.TaskActionDto;
import tz.go.tcra.lims.task.entity.LicencePresentation;
import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.repository.LicencePresentationRepository;
import tz.go.tcra.lims.task.repository.TaskActivityRepository;
import tz.go.tcra.lims.task.repository.TaskTrackRepository;
import tz.go.tcra.lims.task.service.TaskService;
import tz.go.tcra.lims.task.service.TaskStatusHistroyService;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.uaa.service.UserService;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.utils.exception.OperationNotAllowedException;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;
import tz.go.tcra.lims.workflow.entity.WorkflowStepDecision;
import tz.go.tcra.lims.workflow.repository.WorkflowStepDecisionRepository;
import tz.go.tcra.lims.workflow.service.WorkflowService;

/**
 * @author DonaldSj
 */

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private LicenceRepository licenceRepo;

	@Autowired
	private MoneyInWords moneyInWords;

	@Autowired
	private LicenceProductRepository licenseProductRepository;

	@Autowired
	private LicenceCategoryRepository licenseCategoryRepository;

	@Autowired
	private LicenceDetailRepository licenseDetailRepository;

	@Autowired
	private GeoLocationRepository geoLocationRepository;

	@Autowired
	private LicenceApplicationServiceRepository licenseApplicationServiceRepo;

	@Autowired
	private LicenceApplicationCoverageRepository licenseApplicationCoverageRepo;

	@Autowired
	private FeeStructureRepository feeStructureRepo;

	@Autowired
	private LicenceApplicationEntityRepository licenseApplicationEntityRepo;

	@Autowired
	private LicenceApplicationEntityShareholderRepository licenseApplicationEntityShareholderRepo;

	@Autowired
	private StatusRepository statusRepo;

	@Autowired
	private LicenceApplicationIndividualDetailRepository individualDetailRepository;

	@Autowired
	private LicenceCategoryServiceRepository categoryServiceRepo;

	@Autowired
	private LicencePresentationRepository licencePresentationRepo;

	@Autowired
	private AppUtility utility;

	@Autowired
	private LicenseCategoryService licenseCategoryService;

	@Autowired
	private LicenseDetailService licenseDetailService;

	@Autowired
	private GeoLocationService geoLocationService;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private UserService userService;

	@Autowired
	private LicenseService licenceService;

	@Autowired
	private FeeStructureRepository feeStructureRepository;

	@Autowired
	private BillingRepository billingRepository;

	@Autowired
	private BillingChargesRepository billingChargesRepository;

	@Autowired
	private LicenceApplicationIndividualContentResourceRepository individualContentResourceRepo;

	@Autowired
	private ListOfValueRepository listOfValueRepo;

	@Autowired
	private TaskTrackRepository trackRepo;

	@Autowired
	private WorkflowStepDecisionRepository stepDecisionRepo;

	@Autowired
	private TaskActivityRepository activityRepo;

	@Autowired
	private LicencePresentationRepository presentationRepo;

	@Autowired
	private LicenceAcquiredSpectrumRepository acquiredSpectrumRepo;

	@Autowired
	private LicenceeEntityRepository entityRepo;

	@Autowired
	private TaskService taskService;

	@Autowired
	private BillingService billingService;

	@Autowired
	private EntityApplicationRepository entityApplicationRepository;

	@Autowired
	private TaskStatusHistroyService statusHistoryService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private FeeStructureService feeStructureService;

	@Value("${lims.licence.draft.status.id}")
	private Long draftStatusId;

	@Value("${lims.licence.submitted.status.id}")
	private Long submittedStatusId;

	@Value("${lims.licence.track.advance.flag.int}")
	private Integer advanceFlag;

	@Value("${lims.licence.track.return.flag.int}")
	private Integer returnFlag;

	@Value("${lims.licence.new.workflow.type.code}")
	private String newLicenceWorkflowTypeCode;

	@Value("${lims.licence.renew.workflow.type.code}")
	private String renewLicenceWorkflowTypeCode;

	@Value("${lims.licence.upgrade.workflow.type.code}")
	private String upgradeLicenceWorkflowTypeCode;

	@Value("${lims.licence.cancellation.workflow.type.code}")
	private String cancellationLicenceWorkflowTypeCode;

	@Value("${lims.licence.transferofownership.workflow.type.code}")
	private String transferOfOwnershipLicenceWorkflowTypeCode;
        
	@Override
	public Response<List<LicenseCategoryDto>> getLicenseCategoriesByParent(Long parentId) {
		Response<List<LicenseCategoryDto>> response = new Response(ResponseCode.SUCCESS, true,
				"LIST LICENCE CATEGORIES", null);
		try {

			List<LicenceCategory> categories = new ArrayList();
			if (parentId > 0L) {

				categories = licenseCategoryRepository.findByParentAndActivePortal(parentId, true);
			} else {

				categories = licenseCategoryRepository.findByAllNullParentAndActivePortal(true);
			}

			if (categories.size() == 0) {

				response.setMessage("NO DATA FOUND");
				return response;
			}
			List<LicenseCategoryDto> data = new ArrayList();
			for (LicenceCategory category : categories) {
				LicenseCategoryDto dt = new LicenseCategoryDto();
				dt.setId(category.getId());
				dt.setCode(category.getCode());
				dt.setDisplayName(category.getDisplayName());
				dt.setCategoryName(category.getName());

				List<LicenseDetailMaxDto> services = new ArrayList();
				for (LicenceCategoryServiceDetail service : category.getServices()) {

					LicenseDetailMaxDto srv = new LicenseDetailMaxDto();
					srv.setId(service.getService().getId());
					srv.setCode(service.getService().getCode());
					srv.setName(service.getService().getName());

					services.add(srv);
				}

				dt.setServices(services);
				data.add(dt);
			}
			response.setData(data);

		} catch (Exception e) {

			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	@Transactional
	public Response<SaveResponseDto> saveLicenseApplication(IndividualLicenseApplicationDto data, Long id,
			LicenceApplicationStateEnum state) {
		Response<SaveResponseDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"INDIVIDUAL LICENSE APPLICATION SAVED SUCCESSFULY", new SaveResponseDto(HttpStatus.CREATED.value(),
						HttpStatus.CREATED.toString(), System.currentTimeMillis()));
		try {

			LimsUser applicant = utility.getUser();

			if (!applicant.isComplete()) {

				throw new OperationNotAllowedException("PLEASE COMPLETE PROFILE = " + applicant.getId());
			}

			LicenceeEntity entity = applicant.getUserEntity();
			if (data.getEntityId() > 0) {

				Optional<LicenceeEntity> entityExistance = entityRepo.findById(data.getEntityId());
				if (!entityExistance.isPresent()) {

					throw new DataNotFoundException("ENTITY NOT FOUND");
				}

				entity = entityExistance.get();
			}

			Licence license = new Licence();
			if (id > 0) {

				Optional<Licence> existing = licenceRepo.findById(id);
				if (!existing.isPresent()) {

					throw new DataNotFoundException("LICENSE NOT FOUND = " + id);
				}

				license = existing.get();

				if (!license.getIsDraft()) {

					throw new OperationNotAllowedException("CANNOT UPDATE SUBMITTED APPLICATION");
				}

			} else {

				// entity details need to be saved at the first time of application and not
				// updated during updates
				LicenceApplicationEntity applicantEntity = this.saveApplicantEntity(entity);

				license.setApplicantEntity(applicantEntity);
				license.setStatus(statusRepo.getOne(draftStatusId));

				// populate all entity attachments
				data.getAttachments().addAll(attachmentService.getAttachments(entity));
			}

			Optional<LicenceCategory> category = licenseCategoryRepository.findById(data.getCoverageId());
			if (!category.isPresent()) {

				throw new DataNotFoundException("CATEGORY NOT FOUND = " + data.getCoverageId());
			}

			List<LicenceProduct> products = licenseProductRepository
					.findByLicenseCategoryAndActivePortal(category.get().getId(), true);

			if (products.size() == 0) {

				throw new DataNotFoundException("ACTIVE PRODUCT FOR THE CATEGORY NOT FOUND = " + data.getCoverageId());
			}

			if (data.getReferenceLicenceId() == 0 && (state.equals(LicenceApplicationStateEnum.RENEW)
					|| state.equals(LicenceApplicationStateEnum.UPGRADE)
					|| state.equals(LicenceApplicationStateEnum.TRANSFER))) {

				throw new OperationNotAllowedException(
						"REFERENCE APPLICATION ID CANNOT BE ZERO FOR RENEWALS,UPGRADES or TRANSFER");
			}

			license.setDeclaration(data.getDeclaration());
			license.setApplicationState(state);
			license.setLicenseState(LicenceStateEnum.APPLICATION);
			license.setLicenseProduct(products.get(0));
			license.setRootLicenceCategoryId(licenseCategoryService.getRootLicenceCategoryByProduct(products.get(0)));
			license.setApplicant(applicant.getId());
			license = licenceRepo.saveAndFlush(license);

			// save status history
			statusHistoryService.saveStatusHistory(license, license.getStatus());

			// save individual license details
			this.saveIndividualLicenceDetails(data, license);

			// check if licence attachments have been set
			if (data.getAttachments().size() > 0) {

				attachmentService.saveAttachments(data.getAttachments(), license);
			}

			// check if licence spectrum values have been set
			if (data.getSpectrumValue().size() > 0) {

				this.saveSpectrumAcquired(data.getSpectrumValue(), license.getId());
			}

			// check if licence services have been set
			if (data.getLicenseServices().size() > 0) {

				this.saveLicenceServices(data.getLicenseServices(), license);
			}

			// check if licence coverage locations have been set
			if (data.getCoverageLocations().size() > 0) {

				this.saveCoverageAreas(data.getCoverageLocations(), license);
			}

		} catch (OperationNotAllowedException e) {

			log.error(e.getMessage());
			throw new OperationNotAllowedException(e.getMessage());

		} catch (DataNotFoundException e) {

			log.error(e.getMessage());
			throw new DataNotFoundException(e.getMessage());

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	@Transactional
	public Response<LicencePortalMaxDto> submitLicenseApplication(IndividualLicenseApplicationDto data, Long id,
			LicenceApplicationStateEnum state) {
		Response<LicencePortalMaxDto> response = new Response<>();
		try {

			LimsUser applicant = utility.getUser();

			if (!applicant.isComplete()) {

				throw new OperationNotAllowedException("PLEASE COMPLETE PROFILE = " + applicant.getId());
			}

			LicenceeEntity licenceeEntity = applicant.getUserEntity();
			if (data.getEntityId() > 0) {

				Optional<LicenceeEntity> entityExistance = entityRepo.findById(data.getEntityId());
				if (!entityExistance.isPresent()) {

					throw new DataNotFoundException("ENTITY NOT FOUND");
				}

				licenceeEntity = entityExistance.get();
			}

			Licence license = new Licence();
			if (id > 0) {

				Optional<Licence> existing = licenceRepo.findById(id);
				if (!existing.isPresent()) {

					throw new DataNotFoundException("LICENSE NOT FOUND = " + id);
				}

				license = existing.get();

				if (!license.getIsDraft()) {

					throw new OperationNotAllowedException("CANNOT UPDATE SUBMITTED APPLICATION");
				}

				if (license.getStatus().getId() == draftStatusId) {

					license.setStatus(statusRepo.getOne(submittedStatusId));
				}
			} else {

				// licenceeEntity details need to be saved at the first time of application and
				// not
				// updated during updates
				LicenceApplicationEntity applicantEntity = this.saveApplicantEntity(licenceeEntity);
				license.setApplicantEntity(applicantEntity);
				license.setStatus(statusRepo.getOne(submittedStatusId));

				// populate all licenceeEntity attachments
				data.getAttachments().addAll(attachmentService.getAttachments(licenceeEntity));
			}

			Optional<LicenceCategory> category = licenseCategoryRepository.findById(data.getCoverageId());
			if (!category.isPresent()) {

				throw new DataNotFoundException("CATEGORY NOT FOUND = " + data.getCoverageId());
			}
			List<LicenceProduct> products = licenseProductRepository
					.findByLicenseCategoryAndActivePortal(category.get().getId(), true);

			if (products.size() == 0) {

                            throw new DataNotFoundException("PRODUCT FOR THE CATEGORY NOT FOUND = " + data.getCoverageId());
			}

			if (data.getReferenceLicenceId() == 0 && 
                                (state.equals(LicenceApplicationStateEnum.RENEW)
					|| state.equals(LicenceApplicationStateEnum.UPGRADE)
					|| state.equals(LicenceApplicationStateEnum.TRANSFER))) {

				throw new OperationNotAllowedException(
						"REFERENCE APPLICATION ID CANNOT BE ZERO FOR RENEWALS,UPGRADES or TRANSFER");
			}
                        
                        if(data.getReferenceLicenceId() > 0){
                            
                            Optional<Licence> oldExisting = licenceRepo.findById(id);
                            if (!oldExisting.isPresent()) {

                                throw new DataNotFoundException("REFERENCED LICENSE NOT FOUND = " + id);
                            }
                            
                            Licence oldLicence=oldExisting.get();
                            
                            if(state.equals(LicenceApplicationStateEnum.RENEW)){
                            
                                oldLicence.setOperationalStatus(OperationalEnum.OnRenewal.toString());
                            }else if(state.equals(LicenceApplicationStateEnum.TRANSFER)){
                            
                                oldLicence.setOperationalStatus(OperationalEnum.OnTransfer.toString());
                            }else if(state.equals(LicenceApplicationStateEnum.UPGRADE)){
                            
                                oldLicence.setOperationalStatus(OperationalEnum.OnUpgrade.toString());
                            }else if(state.equals(LicenceApplicationStateEnum.CANCELLATION)){
                            
                                oldLicence.setOperationalStatus(OperationalEnum.OnCancellation.toString());
                            }
                            
                            oldLicence.setUpdatedAt(LocalDateTime.now());
                            licenceRepo.save(oldLicence);
                        }

			license.setDeclaration(data.getDeclaration());
			license.setApplicationState(state);
			license.setLicenseState(LicenceStateEnum.APPLICATION);
			license.setLicenseProduct(products.get(0));
			license.setRootLicenceCategoryId(licenseCategoryService.getRootLicenceCategoryByProduct(products.get(0)));
			license.setApplicant(applicant.getId());
			license.setIsDraft(Boolean.FALSE);
			license.setDecision(WorkflowDecisionEnum.NEW.toString());
			license.setSubmittedAt(LocalDateTime.now());
			license.setComments(null);

			license = licenceRepo.saveAndFlush(license);

			// save status history
			statusHistoryService.saveStatusHistory(license, license.getStatus());

			// save individual license details
			this.saveIndividualLicenceDetails(data, license);

			// check if licence attachments have been set
			if (data.getAttachments().size() > 0) {

				attachmentService.saveAttachments(data.getAttachments(), license);
			}

			// check if licence spectrum values have been set
			if (data.getSpectrumValue().size() > 0) {

				this.saveSpectrumAcquired(data.getSpectrumValue(), license.getId());
			}

			// check if licence services have been set
			if (data.getLicenseServices().size() > 0) {

				this.saveLicenceServices(data.getLicenseServices(), license);
			}

			// check if licence coverage locations have been set
			if (data.getCoverageLocations().size() > 0) {

				this.saveCoverageAreas(data.getCoverageLocations(), license);
			}

			// check if the application status is not submitted status so as to
			// raise bill
			String workflowTypeCode = this.getWorkflowTypeCode(state);
			if (workflowTypeCode == null) {

				throw new DataNotFoundException("WORKFLOW CODE FOR THE APPLICATION STATE NOT FOUND");
			}
			if (license.getStatus().getId() != submittedStatusId) {

				taskService.intiateLicenceTrack(license, Boolean.FALSE, workflowTypeCode);

			} else {

				taskService.intiateLicenceTrack(license, Boolean.TRUE, workflowTypeCode);
			}

			response = this.getLicenseById(license.getId());
		} catch (OperationNotAllowedException e) {

			log.error(e.getMessage());
			throw new OperationNotAllowedException(e.getMessage());

		} catch (DataNotFoundException e) {

			log.error(e.getMessage());
			throw new DataNotFoundException(e.getMessage());

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<List<LicencePortalMinDto>> getLicenseByApplicant() {
		Response<List<LicencePortalMinDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENCE APPLICATIONS RETRIEVED SUCCESSFULLY", null);
		try {
			LimsUser user = utility.getUser();

			if (user.getUserEntity() == null) {

				response.setMessage("APPLICANT ENTITY DETAILS NOT FOUND");
				return response;
			}

			Set<Licence> licences = licenceRepo.findByApplicantEntity(user.getUserEntity().getId());

			if (licences.isEmpty()) {

				response.setMessage("LICENCE APPLICATIONS NOT FOUND");
				return response;
			}

			List<LicencePortalMinDto> data = new ArrayList<LicencePortalMinDto>();
			for (Licence licence : licences) {
				LicencePortalMinDto dt = new LicencePortalMinDto();
				dt.setId(licence.getId());
				dt.setIsDraft(licence.getIsDraft());
				dt.setIssueDate(licence.getIssuedDate());
				dt.setExpireDate(licence.getExpireDate());
				dt.setLicenseState(licence.getLicenseState().toString());
				dt.setApplicationState(licence.getApplicationState().toString());
				dt.setProduct(licence.getLicenseProduct().getDisplayName());
				dt.setStatus(statusRepo.getOne(licence.getStatus().getId()).getDisplayName());
                                dt.setOperationalStatus(licence.getOperationalStatus());

				// extract category hierachy
				List<LicenceCategory> categoryHierachy = licenseCategoryService
						.getListOfLicenceCategoryTopHierachyByCategoryId(
								licence.getLicenseProduct().getLicenseCategory().getId());
				int categoryLevel1 = categoryHierachy.size() - 2;
				if (categoryHierachy.size() > 0) {

					dt.setCoverage(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(0)));
				}

				if (categoryLevel1 > 0) {

					dt.setCategory(licenseCategoryService
							.composeLicenseCategoryMinDto(categoryHierachy.get(categoryHierachy.size() - 2)));
				}

				int subCategoryLevel = categoryHierachy.size() - 3;
				if (subCategoryLevel > 0) {

					dt.setSubCategory(licenseCategoryService
							.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevel)));
				}

				dt.setSubmittedAt(licence.getSubmittedAt());
				dt.setCreatedAt(licence.getCreatedAt());
                                

				data.add(dt);
			}

			if (data.size() == 0) {

				response.setMessage("APPLICANT APPLICATIONS NOT FOUND");
			}

			response.setData(data);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<List<LicenseDetailMaxDto>> getLicenceCategoryServices(Long categoryId) {
		Response<List<LicenseDetailMaxDto>> response = new Response(ResponseCode.SUCCESS, true,
				"LICENCE CATEGORY SERVICES RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<LicenceCategory> licenseCategory = licenseCategoryRepository.findById(categoryId);

			if (!licenseCategory.isPresent()) {

				response.setMessage("LICENCE CATEGORY NOT FOUND");
				return response;
			}

			// get only the active services
			List<LicenceCategoryServiceDetail> services = categoryServiceRepo
					.findByCategoryAndActive(licenseCategory.get(), true);
			if (services.size() == 0) {

				response.setMessage("LICENCE CATEGORY SERVICES NOT FOUND");
				return response;
			}

			List<LicenseDetailMaxDto> data = new ArrayList();
			for (LicenceCategoryServiceDetail service : services) {

				LicenseDetailMaxDto srv = new LicenseDetailMaxDto();
				srv.setId(service.getService().getId());
				srv.setCode(service.getService().getCode());
				srv.setName(service.getService().getName());

				data.add(srv);
			}

			response.setData(data);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<LicencePortalMaxDto> getLicenseById(Long id) {
		Response<LicencePortalMaxDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENCE DETAILS RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<Licence> existing = licenceRepo.findById(id);

			if (!existing.isPresent()) {

				response.setMessage("LICENCE DETAILS NOT FOUND");
				return response;
			}

			Licence licence = existing.get();

			LimsUser user = utility.getUser();

			// check if initiator is assigned entity an entity
			if (user.getUserEntity() == null) {

				throw new OperationNotAllowedException("OPERATION NOT ALLOWED, NO ENTITY ASSIGNED TO INITIATOR");
			}

			// check if applicant entity is assigned to the initiator
			if (licence.getApplicantEntity().getReferingEntityId() != user.getUserEntity().getId()) {

				throw new OperationNotAllowedException(
						"OPERATION NOT ALLOWED, INITIATOR NOT ASSIGNED TO REQUESTED ENTITY DETAILS");
			}

			// retrieve all the licence services applied for
			List<LicenseDetailMaxDto> services = new ArrayList();
			Set<LicenceApplicationServiceDetail> appliedServices = licenseApplicationServiceRepo
					.findByLicenseId(licence.getId());
			for (LicenceApplicationServiceDetail appliedService : appliedServices) {

				services.add(licenseDetailService.composeLicenceService(appliedService.getService()));
			}

			// retrieve all the licence coverage areas
			List<GeoLocationMinDto> locations = new ArrayList();
			Set<LicenceApplicationCoverage> appliedCoverages = licenseApplicationCoverageRepo
					.findByLicenseId(licence.getId());
			for (LicenceApplicationCoverage location : appliedCoverages) {

				locations.add(geoLocationService.composeGeoLocationMinDto(location.getLocation()));
			}

			LicencePortalMaxDto data = new LicencePortalMaxDto();
			data.setId(licence.getId());
			data.setIsDraft(licence.getIsDraft());
			data.setIssueDate(licence.getIssuedDate());
			data.setExpireDate(licence.getExpireDate());
			data.setLicenseState(licence.getLicenseState().toString());
			data.setApplicationState(licence.getApplicationState().toString());
			data.setProduct(licence.getLicenseProduct().getDisplayName());
			data.setServices(services);
			data.setCoverageAreas(locations);
			data.setAttachments(attachmentService.getAttachmentsMax(licence));
			data.setCreator(userService.composeUserMaxDto(user));
			data.setEntity(licenceService.composeLicenceApplicationEntity(licence.getApplicantEntity()));
			data.setApplicationNumber(licence.getApplicationNumber());
			data.setLicenceNumber(licence.getLicenceNumber());

			Optional<IndividualLicenceApplicationDetail> individualLicenseDetail = individualDetailRepository
					.findByLicense(licence);

			if (individualLicenseDetail.isPresent()) {

				data.setIndividual(licenceService.composeIndividualLicenceDto(individualLicenseDetail.get()));
			}

			data.setSubmittedAt(licence.getSubmittedAt());
			data.setCreatedAt(licence.getCreatedAt());
			data.setStatus(statusRepo.getOne(licence.getStatus().getId()).getDisplayName());
                        data.setOperationalStatus(licence.getOperationalStatus());
                        
                        // extract category hierachy
			List<LicenceCategory> categoryHierachy = licenseCategoryService
					.getListOfLicenceCategoryTopHierachyByCategoryId(
							licence.getLicenseProduct().getLicenseCategory().getId());
			
			if (categoryHierachy.size() > 0) {

				data.setCoverage(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(0)));
			}
                        
                        int subCategoryLevelLeaf = categoryHierachy.size() - 4;
                        if(subCategoryLevelLeaf > 0){
                        
                            data.setSubCategoryLeaf(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevelLeaf)));
                        }
                        
                        int subCategoryLevel = categoryHierachy.size() - 3;
			if (subCategoryLevel > 0) {

				data.setSubCategory(
						licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevel)));
			}
                        
                        int categoryLevel1 = categoryHierachy.size() - 2;
			if (categoryLevel1 > 0) {

				data.setCategory(licenseCategoryService
						.composeLicenseCategoryMinDto(categoryHierachy.get(categoryHierachy.size() - 2)));
			}


			data.setFees(feeStructureService.findFeeStructureByFeeable(licence.getLicenseProduct()));
			data.setBills(billingService.getLicenseBillingByLicenseid(licence.getId(), BillingAttachedToEnum.LICENCE));
			data.setStatusHistory(statusHistoryService.getStatusHistoryByTrackable(licence));
			data.setSpectrumValues(acquiredSpectrumRepo.findSpectrumValueByLicenceId(licence.getId()));
			data.setLicenceCertificateUri(attachmentService.getLicenceCertificateUri(licence));

			response.setData(data);

		} catch (OperationNotAllowedException e) {

			log.error(e.getLocalizedMessage());
			throw new OperationNotAllowedException(e.getLocalizedMessage());
		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return response;
	}

	@Override
	@Transactional
	public void saveLicenceServices(List<Long> data, Licence license) throws DataNotFoundException, Exception {
		licenseApplicationServiceRepo.deleteByLicenseId(license.getId());
		LicenceApplicationServiceDetail service = new LicenceApplicationServiceDetail();
		for (Long serviceId : data) {
			Optional<LicenceServiceDetail> licenseService = licenseDetailRepository.findById(serviceId);

			if (!licenseService.isPresent()) {

				throw new DataNotFoundException("SERVICE NOT FOUND = " + serviceId);
			}
			service = new LicenceApplicationServiceDetail();
			service.setLicenseId(license.getId());
			service.setService(licenseService.get());
			licenseApplicationServiceRepo.saveAndFlush(service);
		}
	}

	@Override
	@Transactional
	public void saveCoverageAreas(Set<Long> data, Licence license) throws DataNotFoundException, Exception {
		licenseApplicationCoverageRepo.deleteByLicenseId(license.getId());
		LicenceApplicationCoverage coverage = new LicenceApplicationCoverage();
		for (Long locationId : data) {

			Optional<GeoLocation> location = geoLocationRepository.findById(locationId);
			if (!location.isPresent()) {

				throw new DataNotFoundException("LOCATION NOT FOUND = " + locationId);
			}

			coverage = new LicenceApplicationCoverage();
			coverage.setLicenseId(license.getId());
			coverage.setLocation(location.get());
			licenseApplicationCoverageRepo.saveAndFlush(coverage);
		}
	}

	@Override
	@Transactional
	public LicenceApplicationEntity saveApplicantEntity(LicenceeEntity entity) throws Exception {
		LicenceApplicationEntity applicantEntity = new LicenceApplicationEntity();
		applicantEntity.setName(entity.getName());
		applicantEntity.setPhone(entity.getPhone());
		applicantEntity.setEmail(entity.getEmail());
		applicantEntity.setFax(entity.getFax());
		applicantEntity.setWebsite(entity.getWebsite());
		applicantEntity.setPhysicalAddress(entity.getPhysicalAddress());
		applicantEntity.setPostalAddress(entity.getPostalAddress());
		applicantEntity.setPostalCode(entity.getPostalCode());
		applicantEntity.setCountryID(entity.getCountryID());
		applicantEntity.setRegionID(entity.getRegionID());
		applicantEntity.setDistrictID(entity.getDistrictID());
		applicantEntity.setWardID(entity.getWardID());
		applicantEntity.setCategory(entity.getCategory());
		applicantEntity.setRegCertNo(entity.getRegCertNo());
		applicantEntity.setTinNo(entity.getTinNo());
		applicantEntity.setBusinessLicenceNo(entity.getBusinessLicenceNo());
		applicantEntity.setReferingEntityId(entity.getId());
		applicantEntity = licenseApplicationEntityRepo.saveAndFlush(applicantEntity);

		// populate entity shareholders for the application if available
		for (LicenceeEntityShareholder shareholder : entity.getShareholders()) {

			LicenceApplicationShareholder sh = new LicenceApplicationShareholder();
			sh.setFullname(shareholder.getFullname());
			sh.setNationality(shareholder.getNationality());
			sh.setShares(shareholder.getShares());
			sh.setLicenceEntity(applicantEntity.getId());
			licenseApplicationEntityShareholderRepo.saveAndFlush(sh);
		}
		// applicantEntity.setShareholders(shareholders);

		return applicantEntity;
	}

	@Override
	public Response<List<PayableFeesDto>> getLicenceCategoryFeeStructure(Long id) {
		Response<List<PayableFeesDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENCE CATEGORY FEES RETRIEVED SUCCESSFULLY", null);
		try {
			List<LicenceProduct> products = licenseProductRepository.findByLicenseCategoryAndActivePortal(id, true);

			if (products.size() == 0) {

				response.setMessage("PRODUCT NOT ASSIGNED TO CATEGORY");
				return response;
			}

			// get active product fees
			List<FeeStructure> feeStructures = feeStructureRepository.findByFeeableAndActive(products.get(0),
					true);

			List<PayableFeesDto> fees = new ArrayList();
			for (FeeStructure fee : feeStructures) {
				PayableFeesDto f = new PayableFeesDto();
				f.setId(fee.getId());
				f.setFeeTypeId(fee.getFeeType().getId());
				f.setFeeType(fee.getFeeType().getName());
				f.setCurrencyId(fee.getFeeCurrency().getId());
				f.setCurrencyName(fee.getFeeCurrency().getName());
				f.setFeeAmount(fee.getFeeAmount());

				fees.add(f);
			}

			response.setData(fees);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<List<LicencePortalMinDto>> getLicenseBillingByApplicant() {
		Response<List<LicencePortalMinDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"APPLICANT APPLICATIONS RETRIEVED SUCCESSFULLY", null);
		try {
			LimsUser user = utility.getUser();

			if (user.getUserEntity() == null) {
				log.info("userid =" + user.getId());

				log.info("userentity =" + user.getUserEntity());

				response.setMessage("APPLICANT APPLICATIONS NOT FOUND");
				return response;
			}
			List<Long> licenceids = new ArrayList<>();
			// entity applications
			entityApplicationRepository.findByApplicantEntityId(user.getUserEntity().getId()).forEach(entityapp -> {
				licenceids.add(entityapp.getId());

			});

			Set<Licence> licences = licenceRepo.findByApplicantEntity(user.getUserEntity().getId());

			licences.forEach(entry -> {
				licenceids.add(entry.getId());
			});

			log.info("licenseIds =" + licenceids.size());
			// retrieve all bills with these license ids
			List<Billing> billings = billingRepository.findByLicenceIdInOrderByIdDesc(licenceids);

			List<LicencePortalMinDto> data = new ArrayList<LicencePortalMinDto>();
			for (Billing billing : billings) {

				LicencePortalMinDto dt = new LicencePortalMinDto();

				if (billing.getIssuedDate() != null) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

					Instant i = billing.getIssuedDate().atZone(ZoneId.systemDefault()).toInstant();

					java.util.Date date = Date.from(i);
					dt.setInvoicedate(sdformat.format(date));
				}
				dt.setGepgFlag(billing.getGepgStatus());

				if (billing.getExpireDate() != null) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

					Instant i = billing.getExpireDate().atZone(ZoneId.systemDefault()).toInstant();

					java.util.Date date = Date.from(i);
					dt.setControlNoExpireDate(sdformat.format(date));
				}

				dt.setAmountInWords(moneyInWords.getMoneyIntoWords(billing.getAmount()).toUpperCase());
				if (billing.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {
					Licence licence = licenceRepo.findById(billing.getLicenceId()).get();
					dt.setEntity(licenceService.composeLicenceApplicationEntity(licence.getApplicantEntity()));

					dt.setId(licence.getId());
					dt.setIsDraft(licence.getIsDraft());
					dt.setIssueDate(licence.getIssuedDate());
					dt.setExpireDate(licence.getExpireDate());
					dt.setLicenseState(licence.getLicenseState().toString());
					dt.setApplicationState(licence.getApplicationState().toString());
					// check if billable is license or entity
					dt.setProduct(licence.getLicenseProduct().getDisplayName());
					dt.setStatus(statusRepo.getOne(licence.getStatus().getId()).getDisplayName());
					// extract category hierachy
					List<LicenceCategory> categoryHierachy = licenseCategoryService
							.getListOfLicenceCategoryTopHierachyByCategoryId(
									licence.getLicenseProduct().getLicenseCategory().getId());
					int categoryLevel1 = categoryHierachy.size() - 2;
					if (categoryHierachy.size() > 0) {

						dt.setCoverage(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(0)));
					}

					if (categoryLevel1 > 0) {

						dt.setCategory(licenseCategoryService
								.composeLicenseCategoryMinDto(categoryHierachy.get(categoryHierachy.size() - 2)));
					}

					int subCategoryLevel = categoryHierachy.size() - 3;
					if (subCategoryLevel > 0) {

						dt.setSubCategory(licenseCategoryService
								.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevel)));
					}

					dt.setSubmittedAt(licence.getSubmittedAt());
					dt.setCreatedAt(licence.getCreatedAt());
				}

				if (billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY_APPLICATION)
						&& entityApplicationRepository.existsById(billing.getLicenceId())) {

					EntityApplication entityApplication = entityApplicationRepository.findById(billing.getLicenceId())
							.get();

					ApplicantEntityDto applicantEntityDto = new ApplicantEntityDto();
					entityApplication.getApplicantEntity();

					applicantEntityDto
							.setBusinessLicenceNo(entityApplication.getApplicantEntity().getBusinessLicenceNo());
					applicantEntityDto.setCategoryName(entityApplication.getApplicantEntity().getCategory().getName());

					applicantEntityDto.setEmail(entityApplication.getApplicantEntity().getEmail());
					applicantEntityDto.setFax(entityApplication.getApplicantEntity().getFax());
					applicantEntityDto.setId(entityApplication.getApplicantEntity().getId());
					applicantEntityDto.setName(entityApplication.getApplicantEntity().getName());
					applicantEntityDto.setPhone(entityApplication.getApplicantEntity().getPhone());
					applicantEntityDto.setPhysicalAddress(entityApplication.getApplicantEntity().getPhysicalAddress());
					applicantEntityDto.setPostalCode(entityApplication.getApplicantEntity().getPostalCode());
					applicantEntityDto.setRegCertNo(entityApplication.getApplicantEntity().getRegCertNo());

					applicantEntityDto.setTinNo(entityApplication.getApplicantEntity().getTinNo());

					applicantEntityDto.setWebsite(entityApplication.getApplicantEntity().getWebsite());

					dt.setEntity(applicantEntityDto);
					if (entityApplication.getEntityProduct() != null) {

						dt.setProduct(entityApplication.getEntityProduct().getDisplayName());
					}

				}

				// add billing details
				dt.setAmount(billing.getAmount());
				dt.setBillId(billing.getBillingNumber());
				dt.setCurrencyName(billing.getCurrency().getCode());
				dt.setCurrencyId(billing.getCurrency().getId());
				dt.setBillStatus(billing.getStatus().toString());
				dt.setControlNumber(billing.getControlNumber());
				List<BillingChargesDto> billingCharges = new ArrayList<>();

				billingChargesRepository.findByBilling(billing).forEach(charge -> {
					BillingChargesDto billingChargesDto = new BillingChargesDto();
					billingChargesDto.setActive(charge.getActive());
					if (billing.getRate() != null || billing.getRate() != 0.0) {
						Double amount = billing.getRate() * charge.getAmount();
						billingChargesDto.setAmount(amount);
					}
					billingChargesDto.setBillingId(charge.getBilling().getId());
					billingChargesDto.setFeeType(charge.getFeeType());
					billingChargesDto.setId(charge.getId());
					billingChargesDto.setFeeTypeId(charge.getFeeId());
					billingChargesDto.setStatus(charge.getStatus());
					billingChargesDto.setCurrency(billing.getCurrency().getCode());
					billingCharges.add(billingChargesDto);

				});
				dt.setBillingCharges(billingCharges);

				data.add(dt);
			}

			if (data.size() == 0) {

				response.setMessage("APPLICANT APPLICATIONS NOT FOUND");
			}

			response.setData(data);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<List<BillingChargesDto>> getBillingChargesByInvoiceNumber(String invoinceNumber) {
		Response<List<BillingChargesDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"BILL CHARGES RETRIEVED SUCCESSFULLY", null);
		List<BillingChargesDto> charges = new ArrayList<>();

		log.info("INVOICE NUMBER " + invoinceNumber);
		Billing billing = billingRepository.findByBillingNumber(invoinceNumber);
		if (billing != null) {
			billingChargesRepository.findByBilling(billing).forEach(charge -> {
				BillingChargesDto billingChargesDto = new BillingChargesDto();
				billingChargesDto.setActive(charge.getActive());
				if (billing.getRate() != null || billing.getRate() != 0.0) {
					Double amount = billing.getRate() * charge.getAmount();
					billingChargesDto.setAmount(amount);
				}
				billingChargesDto.setBillingId(charge.getBilling().getId());
				billingChargesDto.setFeeType(charge.getFeeType());
				billingChargesDto.setFeeTypeId(charge.getFeeId());
				billingChargesDto.setId(charge.getId());
				billingChargesDto.setStatus(charge.getStatus());
				billingChargesDto.setCurrency(billing.getCurrency().getCode());

				charges.add(billingChargesDto);

			});
			response.setData(charges);
		} else {
			response.setStatus(false);
			response.setMessage("NOT BILLING CHARGES FOUND");
		}

		return response;
	}

	@Override
	public Response<LicencePortalMinDto> getLicenseBillingByInvoiceNumber(String invoinceNumber) {
		Response<LicencePortalMinDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"APPLICANT BILL HAVE BEEN  RETRIEVED SUCCESSFULLY", null);
		try {

			// retrieve all bills with these license ids
			Billing billing = billingRepository.findByBillingNumber(invoinceNumber);

			if (billing != null) {

				LicencePortalMinDto dt = new LicencePortalMinDto();

				if (billing.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {

					Licence licence = licenceRepo.findById(billing.getLicenceId()).get();
					dt.setEntity(licenceService.composeLicenceApplicationEntity(licence.getApplicantEntity()));

					dt.setId(licence.getId());
					dt.setIsDraft(licence.getIsDraft());
					dt.setIssueDate(licence.getIssuedDate());
					dt.setExpireDate(licence.getExpireDate());
					dt.setApplicationState(licence.getApplicationState().toString());
					dt.setLicenseState(licence.getLicenseState().toString());
					dt.setProduct(licence.getLicenseProduct().getDisplayName());
					dt.setStatus(statusRepo.getOne(licence.getStatus().getId()).getDisplayName());

					// extract category hierachy
					List<LicenceCategory> categoryHierachy = licenseCategoryService
							.getListOfLicenceCategoryTopHierachyByCategoryId(
									licence.getLicenseProduct().getLicenseCategory().getId());
					int categoryLevel1 = categoryHierachy.size() - 2;
					if (categoryHierachy.size() > 0) {

						dt.setCoverage(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(0)));
					}

					if (categoryLevel1 > 0) {

						dt.setCategory(licenseCategoryService
								.composeLicenseCategoryMinDto(categoryHierachy.get(categoryHierachy.size() - 2)));
					}

					int subCategoryLevel = categoryHierachy.size() - 3;
					if (subCategoryLevel > 0) {

						dt.setSubCategory(licenseCategoryService
								.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevel)));
					}

					dt.setSubmittedAt(licence.getSubmittedAt());
					dt.setCreatedAt(licence.getCreatedAt());

				}

				if (billing.getIssuedDate() != null) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

					Instant i = billing.getIssuedDate().atZone(ZoneId.systemDefault()).toInstant();

					java.util.Date date = Date.from(i);
					dt.setInvoicedate(sdformat.format(date));
				}
				dt.setAmountInWords(moneyInWords.getMoneyIntoWords(billing.getAmount()).toUpperCase());

				if (billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY_APPLICATION)
						&& entityApplicationRepository.existsById(billing.getLicenceId())) {

					EntityApplication entityApplication = entityApplicationRepository.findById(billing.getLicenceId())
							.get();

					ApplicantEntityDto applicantEntityDto = new ApplicantEntityDto();
					entityApplication.getApplicantEntity();

					applicantEntityDto
							.setBusinessLicenceNo(entityApplication.getApplicantEntity().getBusinessLicenceNo());
					applicantEntityDto.setCategoryName(entityApplication.getApplicantEntity().getCategory().getName());

					applicantEntityDto.setEmail(entityApplication.getApplicantEntity().getEmail());
					applicantEntityDto.setFax(entityApplication.getApplicantEntity().getFax());
					applicantEntityDto.setId(entityApplication.getApplicantEntity().getId());
					applicantEntityDto.setName(entityApplication.getApplicantEntity().getName());
					applicantEntityDto.setPhone(entityApplication.getApplicantEntity().getPhone());
					applicantEntityDto.setPhysicalAddress(entityApplication.getApplicantEntity().getPhysicalAddress());
					applicantEntityDto.setPostalCode(entityApplication.getApplicantEntity().getPostalCode());
					applicantEntityDto.setRegCertNo(entityApplication.getApplicantEntity().getRegCertNo());

					applicantEntityDto.setTinNo(entityApplication.getApplicantEntity().getTinNo());

					applicantEntityDto.setWebsite(entityApplication.getApplicantEntity().getWebsite());

					dt.setEntity(applicantEntityDto);
					if (entityApplication.getEntityProduct() != null) {

						dt.setProduct(entityApplication.getEntityProduct().getDisplayName());
					}

				}

				// add billing details
				dt.setAmount(billing.getAmount());
				dt.setBillId(billing.getBillingNumber());
				dt.setCurrencyName(billing.getCurrency().getCode());
				dt.setCurrencyId(billing.getCurrency().getId());
				dt.setBillStatus(billing.getStatus().toString());
				dt.setControlNumber(billing.getControlNumber());

				List<BillingChargesDto> billingCharges = new ArrayList<>();

				billingChargesRepository.findByBilling(billing).forEach(charge -> {
					BillingChargesDto billingChargesDto = new BillingChargesDto();
					billingChargesDto.setActive(charge.getActive());
					if (billing.getRate() != null || billing.getRate() != 0.0) {
						Double amount = billing.getRate() * charge.getAmount();
						billingChargesDto.setAmount(amount);
					}
					billingChargesDto.setBillingId(charge.getBilling().getId());
					billingChargesDto.setFeeType(charge.getFeeType());
					billingChargesDto.setFeeTypeId(charge.getFeeId());
					billingChargesDto.setId(charge.getId());
					billingChargesDto.setStatus(charge.getStatus());
					billingChargesDto.setCurrency(billing.getCurrency().getCode());
					billingCharges.add(billingChargesDto);

				});
				dt.setBillingCharges(billingCharges);
				response.setData(dt);

			} else {

				response.setMessage("APPLICANT BILLING NOT FOUND");
				response.setCode(ResponseCode.NO_RECORD_FOUND);
			}

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	@Transactional
	public void saveIndividualLicenceDetails(IndividualLicenseApplicationDto data, Licence license)
			throws DataNotFoundException, Exception {
		Optional<IndividualLicenceApplicationDetail> exists = individualDetailRepository.findByLicense(license);
		IndividualLicenceApplicationDetail licenseDetail = new IndividualLicenceApplicationDetail();
		if (exists.isPresent()) {

			licenseDetail = exists.get();
		}

		licenseDetail.setLicense(license);
		licenseDetail.setIncludeSpectrum(data.getIncludeSpectrum());
		licenseDetail.setIncludeSpectrumRequired(data.getIncludeSpectrumRequired());
		licenseDetail.setInvestmentCost(data.getInvestmentCost());
		licenseDetail.setInvestmentCostCurrency(data.getInvestmentCostCurrency());
		licenseDetail.setRequestDescription(data.getRequestDescription());
		licenseDetail.setOtherRelevantInfo(data.getOtherRelevantInfo());
		licenseDetail.setCommencementDate(data.getCommencementDate());

		if (data.getContent() != null) {

			licenseDetail.setBeamingSatelliteLatitude(data.getContent().getBeamingSatelliteLatitude());
			licenseDetail.setBeamingSatelliteLongitude(data.getContent().getBeamingSatelliteLongitude());
			licenseDetail.setBeamingSatelliteLocation(data.getContent().getBeamingSatelliteLocation());
			licenseDetail.setFacilityOwnerCategory(data.getContent().getFacilityOwnerCategory());
			licenseDetail.setSatelliteUplinkRequired(data.getContent().getSatelliteUplinkRequired());
			licenseDetail.setTransmitterFacilityLesser(data.getContent().getTransmitterFacilityLesser());

			if (data.getContent().getResources().size() > 0) {

				for (Long resourceId : data.getContent().getResources()) {

					Optional<ListOfValue> existing = listOfValueRepo.findById(resourceId);
					if (!existing.isPresent()) {

						continue;
					}

					IndividualLicenceApplicationContentResource resource = individualContentResourceRepo
							.findFirstByIndividualIdAndResource(licenseDetail.getId(), existing.get());
					if (resource == null) {

						resource = new IndividualLicenceApplicationContentResource();
					}

					resource.setIndividualId(licenseDetail.getId());
					resource.setResource(existing.get());
					individualContentResourceRepo.save(resource);
				}
			}
		}
		licenseDetail = individualDetailRepository.saveAndFlush(licenseDetail);
	}

	@Override
	public Response<List<LicencePortalMinDto>> getLicenseBillingByApplicantBasedOnControlNoGivenOrNot(
			ControlNumberAvailability billingStatusEnum) {
		Response<List<LicencePortalMinDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"APPLICANT BILLING RETRIEVED SUCCESSFULLY", null);

		List<BillingStatusEnum> status = new ArrayList<>();
		if (billingStatusEnum.equals(ControlNumberAvailability.WITHOUTCONTROLNUMBER)) {
			status.add(BillingStatusEnum.BILLED);
			status.add(BillingStatusEnum.NOTPAID);

		} else {

			status.add(BillingStatusEnum.PAID);
			status.add(BillingStatusEnum.PENDING);
		}

		try {
			LimsUser user = utility.getUser();

			if (user.getUserEntity() == null) {

				response.setMessage("APPLICANT APPLICATIONS NOT FOUND");
				return response;
			}
			List<Long> licenceids = new ArrayList<>();
			entityApplicationRepository.findByApplicantEntityId(user.getUserEntity().getId()).forEach(entityapp -> {
				licenceids.add(entityapp.getId());

			});

			Set<Licence> licences = licenceRepo.findByApplicantEntity(user.getUserEntity().getId());

			licences.forEach(entry -> {
				licenceids.add(entry.getId());
			});
			// retrieve all bills with these license ids
			List<Billing> billings = billingRepository.findByLicenceIdInAndStatusIn(licenceids, status);

			List<LicencePortalMinDto> data = new ArrayList<LicencePortalMinDto>();
			for (Billing billing : billings) {

				LicencePortalMinDto dt = new LicencePortalMinDto();

				if (billing.getIssuedDate() != null) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

					Instant i = billing.getIssuedDate().atZone(ZoneId.systemDefault()).toInstant();

					java.util.Date date = Date.from(i);
					dt.setInvoicedate(sdformat.format(date));
				}

				if (billing.getExpireDate() != null) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

					Instant i = billing.getExpireDate().atZone(ZoneId.systemDefault()).toInstant();

					java.util.Date date = Date.from(i);
					dt.setControlNoExpireDate(sdformat.format(date));
				}

				dt.setAmountInWords(moneyInWords.getMoneyIntoWords(billing.getAmount()).toUpperCase());
				if (billing.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {
					Licence licence = licenceRepo.findById(billing.getLicenceId()).get();
					dt.setEntity(licenceService.composeLicenceApplicationEntity(licence.getApplicantEntity()));
					dt.setId(licence.getId());
					dt.setIsDraft(licence.getIsDraft());
					dt.setIssueDate(licence.getIssuedDate());
					dt.setExpireDate(licence.getExpireDate());
					dt.setLicenseState(licence.getLicenseState().toString());
					dt.setApplicationState(licence.getApplicationState().toString());
					dt.setProduct(licence.getLicenseProduct().getDisplayName());
					dt.setStatus(statusRepo.getOne(licence.getStatus().getId()).getDisplayName());
					// extract category hierachy
					List<LicenceCategory> categoryHierachy = licenseCategoryService
							.getListOfLicenceCategoryTopHierachyByCategoryId(
									licence.getLicenseProduct().getLicenseCategory().getId());
					int categoryLevel1 = categoryHierachy.size() - 2;
					if (categoryHierachy.size() > 0) {

						dt.setCoverage(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(0)));
					}

					if (categoryLevel1 > 0) {

						dt.setCategory(licenseCategoryService
								.composeLicenseCategoryMinDto(categoryHierachy.get(categoryHierachy.size() - 2)));
					}

					int subCategoryLevel = categoryHierachy.size() - 3;
					if (subCategoryLevel > 0) {

						dt.setSubCategory(licenseCategoryService
								.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevel)));
					}

					dt.setSubmittedAt(licence.getSubmittedAt());
					dt.setCreatedAt(licence.getCreatedAt());
				}

				if (billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY_APPLICATION)
						&& entityApplicationRepository.existsById(billing.getLicenceId())) {

					EntityApplication entityApplication = entityApplicationRepository.findById(billing.getLicenceId())
							.get();
					if (entityApplication.getEntityProduct() != null) {

						dt.setProduct(entityApplication.getEntityProduct().getDisplayName());
					}

				}

				// add billing details
				dt.setAmount(billing.getAmount());
				dt.setBillId(billing.getBillingNumber());
				dt.setCurrencyName(billing.getCurrency().getCode());
				dt.setBillStatus(billing.getStatus().toString());
				dt.setControlNumber(billing.getControlNumber());
				List<BillingChargesDto> billingCharges = new ArrayList<>();

				billingChargesRepository.findByBilling(billing).forEach(charge -> {
					BillingChargesDto billingChargesDto = new BillingChargesDto();
					billingChargesDto.setActive(charge.getActive());
					if (billing.getRate() != null || billing.getRate() != 0.0) {
						Double amount = billing.getRate() * charge.getAmount();
						billingChargesDto.setAmount(amount);
					}
					billingChargesDto.setBillingId(charge.getBilling().getId());
					billingChargesDto.setFeeType(charge.getFeeType());
					billingChargesDto.setId(charge.getId());
					billingChargesDto.setFeeTypeId(charge.getFeeId());
					billingChargesDto.setStatus(charge.getStatus());
					billingChargesDto.setCurrency(billing.getCurrency().getCode());
					billingCharges.add(billingChargesDto);

				});
				dt.setBillingCharges(billingCharges);

				data.add(dt);
			}

			if (data.size() == 0) {

				response.setMessage("APPLICANT APPLICATIONS NOT FOUND");
			}

			response.setData(data);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<BillingReceiptDto> getBillingReceiptByInvoiceNumber(String invoinceNumber) {

		Response<BillingReceiptDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"APPLICANT BILL HAVE BEEN  RETRIEVED SUCCESSFULLY", null);
		try {

			// retrieve all bills with these license ids
			Billing billing = billingRepository.findByBillingNumber(invoinceNumber);

			if (billing != null) {

				BillingReceiptDto dt = new BillingReceiptDto();
				if (billing.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {
					Licence licence = licenceRepo.findById(billing.getLicenceId()).get();
					dt.setEntity(licenceService.composeLicenceApplicationEntity(licence.getApplicantEntity()));

					String feelistName = null;

					List<BillingCharges> charges = billingChargesRepository.findByBilling(billing);
					for (BillingCharges charge : charges) {
						if (feeStructureRepository.existsById(charge.getFeeId())) {
							if (feelistName == null) {
								feelistName = feeStructureRepository.findById(charge.getFeeId()).get().getName();
							} else {
								feelistName = feelistName + ","
										+ feeStructureRepository.findById(charge.getFeeId()).get().getName();
							}
						}
					}
					String remark = feelistName + " for " + licence.getLicenseProduct().getDisplayName();
					dt.setRemark(remark);

				}

				if (billing.getPayDate() != null) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

					Instant i = billing.getPayDate().atZone(ZoneId.systemDefault()).toInstant();

					java.util.Date date = Date.from(i);
					dt.setPayDate(sdformat.format(date));
				}
				dt.setAmountInWords(moneyInWords.getMoneyIntoWords(billing.getAmount()).toUpperCase());
				dt.setAmount(billing.getAmount());
				dt.setControlNumber(billing.getControlNumber());
				dt.setCurrency(billing.getCurrency().getCode());
				dt.setReceiptNumber(billing.getReceiptNumber());

				if (billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY_APPLICATION)
						&& entityApplicationRepository.existsById(billing.getLicenceId())) {

					EntityApplication entityApplication = entityApplicationRepository.findById(billing.getLicenceId())
							.get();

					ApplicantEntityDto applicantEntityDto = new ApplicantEntityDto();
					entityApplication.getApplicantEntity();

					applicantEntityDto
							.setBusinessLicenceNo(entityApplication.getApplicantEntity().getBusinessLicenceNo());
					applicantEntityDto.setCategoryName(entityApplication.getApplicantEntity().getCategory().getName());

					applicantEntityDto.setEmail(entityApplication.getApplicantEntity().getEmail());
					applicantEntityDto.setFax(entityApplication.getApplicantEntity().getFax());
					applicantEntityDto.setId(entityApplication.getApplicantEntity().getId());
					applicantEntityDto.setName(entityApplication.getApplicantEntity().getName());
					applicantEntityDto.setPhone(entityApplication.getApplicantEntity().getPhone());
					applicantEntityDto.setPhysicalAddress(entityApplication.getApplicantEntity().getPhysicalAddress());
					applicantEntityDto.setPostalCode(entityApplication.getApplicantEntity().getPostalCode());
					applicantEntityDto.setRegCertNo(entityApplication.getApplicantEntity().getRegCertNo());

					applicantEntityDto.setTinNo(entityApplication.getApplicantEntity().getTinNo());

					applicantEntityDto.setWebsite(entityApplication.getApplicantEntity().getWebsite());

					dt.setEntity(applicantEntityDto);

					String feelistName = null;

					List<BillingCharges> charges = billingChargesRepository.findByBilling(billing);
					for (BillingCharges charge : charges) {
						if (feeStructureRepository.existsById(charge.getFeeId())) {
							if (feelistName == null) {
								feelistName = feeStructureRepository.findById(charge.getFeeId()).get().getName();
							} else {
								feelistName = feelistName + ","
										+ feeStructureRepository.findById(charge.getFeeId()).get().getName();
							}
						}
					}
					if (entityApplication.getEntityProduct() != null) {

						String remark = feelistName + " for " + entityApplication.getEntityProduct().getDisplayName();
						dt.setRemark(remark);

					}

				}
				response.setData(dt);

			} else {

				response.setMessage("APPLICANT BILLING NOT FOUND");
				response.setCode(ResponseCode.NO_RECORD_FOUND);
			}

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;

	}

	@Override
	public Response<List<PresentationPortalDto>> viewPresentations() {
		Response<List<PresentationPortalDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"PRESENTATIONS DETAILS RETRIEVED SUCCESSFULLY", null);
		try {
			LimsUser actor = utility.getUser();
			List<LicencePresentation> presentations = presentationRepo
					.findApplicantPresentations(actor.getUserEntity().getId());
			List<PresentationPortalDto> data = new ArrayList();

			if (presentations == null) {

				response.setMessage("PRESENTATIONS NOT FOUND");
				return response;
			}

			for (LicencePresentation presentation : presentations) {

				PresentationPortalDto dt = new PresentationPortalDto();
				dt.setLicenceApplicationNumber(presentation.getLicence().getApplicationNumber());
				dt.setLicenceId(presentation.getLicence().getId());
				dt.setStatus(presentation.getStatus());
				dt.setPresentationDate(presentation.getPresentationDate());
				dt.setRemark(presentation.getRemark());

				// extract category hierachy
				List<LicenceCategory> categoryHierachy = licenseCategoryService
						.getListOfLicenceCategoryTopHierachyByCategoryId(
								presentation.getLicence().getLicenseProduct().getLicenseCategory().getId());

				int categoryLevel1 = categoryHierachy.size() - 2;

				if (categoryHierachy.size() > 0) {

					dt.setCoverage(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(0)));
				}

				if (categoryLevel1 > 0) {

					dt.setCategory(licenseCategoryService
							.composeLicenseCategoryMinDto(categoryHierachy.get(categoryHierachy.size() - 2)));
				}

				int subCategoryLevel = categoryHierachy.size() - 3;
				if (subCategoryLevel > 0) {

					dt.setSubCategory(licenseCategoryService
							.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevel)));
				}

				AttachmentMaxDto attach = attachmentService.getAttachmentMax(presentation);
				if (attach != null) {

					dt.setUri(attach.getUri());
				}

				data.add(dt);
			}

			response.setData(data);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return response;
	}

	@Override
	public ActivityPortalDto composeActivity(TaskActivity licenceActivity) {

		ActivityPortalDto activity = new ActivityPortalDto();
		activity.setActivityName(licenceActivity.getActivityName());
		activity.setDecision(licenceActivity.getDecision());
		activity.setComments(licenceActivity.getComments());
		activity.setCreatedAt(licenceActivity.getCreatedAt());

		return activity;
	}

	@Override
	public Response<SaveResponseDto> savePresentation(PresentationPortalRequestDto data) {
		Response<SaveResponseDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"PRESENTATION SAVED SUCCESSFULLY", new SaveResponseDto(HttpStatus.CREATED.value(),
						HttpStatus.CREATED.toString(), System.currentTimeMillis()));
		try {
			LimsUser actor = utility.getUser();

			Optional<Licence> existingLicence = licenceRepo.findById(data.getLicenseId());
			if (!existingLicence.isPresent()) {

				throw new DataNotFoundException("LICENCE NOT FOUND");
			}

			Licence licence = existingLicence.get();

			Optional<TaskTrack> existing = trackRepo.findByTrackableIdAndTrackableTypeAndActorIdAndIsActed(
					licence.getId(), licence.getTrackableType(), actor.getId(), Boolean.FALSE);
			// check to see if activity is submitted by the required actor
			if (!existing.isPresent()) {

				throw new OperationNotAllowedException("user not the assigned actor [" + actor.getId() + "]");
			}

			TaskTrack track = existing.get();
			track.setIsActed(Boolean.TRUE);
			track.setUpdatedAt(LocalDateTime.now());
			track.setCompletedAt(LocalDateTime.now());
			track = trackRepo.save(track);

			taskService.setTrackingData(track);

			Optional<WorkflowStepDecision> existingStepDecision = stepDecisionRepo
					.findByStepAndDecision(track.getWorkflowStep().getId(), data.getDecision());

			if (!existingStepDecision.isPresent()) {

				throw new OperationNotAllowedException("DECISION NOT CONFIGURED FOR THE STEP");
			}

			WorkflowStepDecision stepDecision = existingStepDecision.get();

			TaskActivity activity = new TaskActivity();

			// save activity data
			activity.setActivityName(track.getWorkflowStep().getName());
			activity.setDecision(data.getDecision());
			activity.setTrack(track);
			activity.setComments(data.getComment());
			activity.setTrackable(licence);
			activity = activityRepo.saveAndFlush(activity);

			// save attachments if any
			if (data.getAttachments().size() > 0) {

				WorkflowStep step = workflowService.getPreviousWorkflowStep(track.getWorkflowId(),
						track.getWorkflowStep().getStepNumber());

				Optional<LicencePresentation> presentation = licencePresentationRepo
						.findFirstByLicenceIdAndWorkflowIdAndWorkflowStepIdAndActive(track.getTrackable().getId(),
								step.getWorkflow(), step.getId(), Boolean.FALSE);
				if (presentation.isPresent()) {

					attachmentService.saveAttachments(data.getAttachments(), presentation.get());
				}
			}

			// save application status
			licence.setDecision(data.getDecision().toString());
			licence.setStatus(stepDecision.getLicenceStatus());
			licence.setUpdatedAt(LocalDateTime.now());
			licenceRepo.save(licence);

			// save status history
			statusHistoryService.saveStatusHistory(licence, stepDecision.getLicenceStatus());

			// get decision
			Integer isAdvancing = utility.interpreteDecision(data.getDecision());

			TaskActionDto action = new TaskActionDto();
			action.setId(track.getId());
			action.setDecision(data.getDecision());
			action.setComment(data.getComment());

			if (isAdvancing == returnFlag) {

				// go back 1 step
				taskService.returnTaskTracker(action, track);

			} else if (isAdvancing == advanceFlag) {

				// advance 1 step forward
				taskService.advanceTaskTracker(action, track);

			} else {
				// nothing to do
			}

			// process applicant notification if any
			if (stepDecision.getNotificationTemplate() != null) {

				// call method to process notification
				taskService.composeNotification(track, stepDecision, Boolean.TRUE);
			}

		} catch (OperationNotAllowedException e) {

			throw new OperationNotAllowedException(e.getLocalizedMessage());

		} catch (DataNotFoundException e) {

			throw new DataNotFoundException(e.getLocalizedMessage());
		} catch (Exception e) {

			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public String getWorkflowTypeCode(LicenceApplicationStateEnum applicationState) {
		String response = null;
		if (LicenceApplicationStateEnum.NEW.equals(applicationState)) {

			response = newLicenceWorkflowTypeCode;
		} else if (LicenceApplicationStateEnum.RENEW.equals(applicationState)) {

			response = renewLicenceWorkflowTypeCode;
		} else if (LicenceApplicationStateEnum.UPGRADE.equals(applicationState)) {

			response = upgradeLicenceWorkflowTypeCode;
		} else if (LicenceApplicationStateEnum.TRANSFER.equals(applicationState)) {

			response = transferOfOwnershipLicenceWorkflowTypeCode;
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public Response<SaveResponseDto> licenceCancellation(LicenceCancellationDto data) {
		Response<SaveResponseDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENCE CANCELLATION INITIATED SUCCESSFULLY", new SaveResponseDto(HttpStatus.CREATED.value(),
						HttpStatus.CREATED.toString(), System.currentTimeMillis()));
		try {

			// verify licence existance
			Optional<Licence> licenceExistance = licenceRepo.findById(data.getLicenceId());
			if (!licenceExistance.isPresent()) {

				throw new DataNotFoundException("LICENCE NOT FOUND");
			}

			Licence licence = licenceExistance.get();

			if (licence.getLicenseState() != LicenceStateEnum.ACTIVE
					&& licence.getLicenseState() != LicenceStateEnum.SUSPENDED) {

				throw new OperationNotAllowedException("LICENCE NOT ACTIVE/SUSPENDED");
			}

			licence.setApplicationState(LicenceApplicationStateEnum.CANCELLATION);
			licence.setUpdatedAt(LocalDateTime.now());
			licenceRepo.save(licence);

			// save activity data
			TaskActivity activity = new TaskActivity();
			activity.setActivityName("Licence Cancellation Initiation");
			activity.setComments(data.getReason());
			activity.setDecision(WorkflowDecisionEnum.COMPLETE);
			activityRepo.save(activity);

			taskService.intiateLicenceTrack(licence, Boolean.FALSE, cancellationLicenceWorkflowTypeCode);

		} catch (OperationNotAllowedException e) {

			throw new OperationNotAllowedException(e.getLocalizedMessage());
		} catch (DataNotFoundException e) {

			throw new DataNotFoundException(e.getLocalizedMessage());
		} catch (Exception e) {

			e.printStackTrace();
			throw new GeneralException("INTERNAL_SERVER_ERROR");
		}

		return response;
	}

	@Override
	@Transactional
	public void saveSpectrumAcquired(List<SpectrumValueDto> data, Long licenceId) throws Exception {

		acquiredSpectrumRepo.deactivateAcquiredSpectrum(licenceId);
		for (SpectrumValueDto dt : data) {
			LicenceAcquiredSpectrum value = acquiredSpectrumRepo.findFirstByLicenceIdAndLowerBandAndUpperBand(licenceId,
					dt.getLowerBand(), dt.getUpperBand());
			if (value == null) {

				value = new LicenceAcquiredSpectrum();
			} else {

				value.setUpdatedAt(LocalDateTime.now());
			}

			value.setLowerBand(dt.getLowerBand());
			value.setUpperBand(dt.getUpperBand());
			value.setLicenceId(licenceId);
			value.setActive(Boolean.TRUE);

			acquiredSpectrumRepo.save(value);
		}
	}

	@Override
	public Response<List<LicencePortalMinDto>> getLicenseResubmittedByApplicant() {
		Response<List<LicencePortalMinDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENCE APPLICATIONS RETRIEVED SUCCESSFULLY", null);
		try {
			LimsUser user = utility.getUser();

			if (user.getUserEntity() == null) {

				response.setMessage("APPLICANT ENTITY DETAILS NOT FOUND");
				return response;
			}

			Set<Licence> licences = licenceRepo.findByApplicantEntityAndDecision(user.getUserEntity().getId(),
					WorkflowDecisionEnum.RESUBMIT.toString());

			if (licences.isEmpty()) {

				response.setMessage("LICENCE APPLICATIONS NOT FOUND");
				return response;
			}

			List<LicencePortalMinDto> data = new ArrayList<LicencePortalMinDto>();
			for (Licence licence : licences) {
				LicencePortalMinDto dt = new LicencePortalMinDto();
                                dt.setApplicationRefNumber(licence.getApplicationNumber());
				dt.setId(licence.getId());
				dt.setIsDraft(licence.getIsDraft());
				dt.setIssueDate(licence.getIssuedDate());
				dt.setExpireDate(licence.getExpireDate());
				dt.setLicenseState(licence.getLicenseState().toString());
				dt.setApplicationState(licence.getApplicationState().toString());
				dt.setProduct(licence.getLicenseProduct().getDisplayName());
				dt.setStatus(statusRepo.getOne(licence.getStatus().getId()).getDisplayName());
				dt.setComments(licence.getComments());

				// extract category hierachy
				List<LicenceCategory> categoryHierachy = licenseCategoryService
						.getListOfLicenceCategoryTopHierachyByCategoryId(
								licence.getLicenseProduct().getLicenseCategory().getId());
				int categoryLevel1 = categoryHierachy.size() - 2;
				if (categoryHierachy.size() > 0) {

					dt.setCoverage(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(0)));
				}

				if (categoryLevel1 > 0) {

					dt.setCategory(licenseCategoryService
							.composeLicenseCategoryMinDto(categoryHierachy.get(categoryHierachy.size() - 2)));
				}

				int subCategoryLevel = categoryHierachy.size() - 3;
				if (subCategoryLevel > 0) {

					dt.setSubCategory(licenseCategoryService
							.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevel)));
				}

				dt.setSubmittedAt(licence.getSubmittedAt());
				dt.setCreatedAt(licence.getCreatedAt());

				data.add(dt);
			}

			if (data.size() == 0) {

				response.setMessage("APPLICANT APPLICATIONS NOT FOUND");
			}

			response.setData(data);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}
}
