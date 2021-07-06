package tz.go.tcra.lims.portal.application.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.feestructure.repository.FeeStructureRepository;
import tz.go.tcra.lims.feestructure.service.FeeStructureService;
import tz.go.tcra.lims.licencee.dto.NameChangeDto;
import tz.go.tcra.lims.licencee.dto.ShareholderChangeDto;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.licencee.entity.NameChangeApplicationDetail;
import tz.go.tcra.lims.licencee.entity.ShareholderChangeApplicationDetail;
import tz.go.tcra.lims.licencee.repository.EntityApplicationRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityRepository;
import tz.go.tcra.lims.licencee.repository.NameChangeApplicationDetailRepository;
import tz.go.tcra.lims.licencee.repository.ShareholderChangeApplicationDetailRepository;
import tz.go.tcra.lims.licencee.service.LicenceeEntityService;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.miscellaneous.enums.EntityApplicationTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.OperationalEnum;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;
import tz.go.tcra.lims.miscellaneous.repository.StatusRepository;
import tz.go.tcra.lims.payment.service.BillingService;
import tz.go.tcra.lims.portal.application.dto.TaskActionPortalDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMaxPortalDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMinPortalDto;
import tz.go.tcra.lims.portal.application.dto.LicenceeEntityMinMinPortalDto;
import tz.go.tcra.lims.portal.application.dto.LicenceeEntityMinPortalDto;
import tz.go.tcra.lims.product.entity.EntityProduct;
import tz.go.tcra.lims.product.repository.EntityProductRepository;
import tz.go.tcra.lims.task.dto.TaskActionDto;
import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.repository.TaskTrackRepository;
import tz.go.tcra.lims.task.service.TaskService;
import tz.go.tcra.lims.task.service.TaskStatusHistroyService;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.utils.exception.OperationNotAllowedException;

/**
 * @author DonaldSj
 */

@Service
@Slf4j
public class EntityApplicationPortalServiceImpl implements EntityApplicationPortalService {

    @Autowired
    private StatusRepository statusRepo;

    @Autowired
    private AppUtility utility;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private TaskTrackRepository trackRepo;

    @Autowired
    private LicenceeEntityRepository entityRepo;

    @Autowired
    private EntityApplicationRepository entityApplicationRepo;

    @Autowired
    private EntityProductRepository entityProductRepo;

    @Autowired
    private ShareholderChangeApplicationDetailRepository shareholderChangeApplicationDetailRepo;

    @Autowired
    private NameChangeApplicationDetailRepository nameChangeApplicationDetailRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FeeStructureRepository feeStructureRepo;
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private TaskStatusHistroyService statusHistoryService;

    @Autowired
    private LicenceeEntityService licenceeEntityService;
    
    @Autowired
    private FeeStructureService feeStructureService;

    @Value("${lims.licence.submitted.status.id}")
    private Long submittedStatusId;

    @Value("${lims.changeofshareholder.workflow.type.code}")
    private String changeOfShareholderWorkflowTypeCode;

    @Value("${lims.changeofname.workflow.type.code}")
    private String changeOfNameWorkflowTypeCode;
        
    @Override
    @Transactional
    public Response<EntityApplicationMaxPortalDto> saveEntityApplication(
            EntityApplicationDto data, 
            Long id,
            EntityApplicationTypeEnum applicationType) {
        Response<EntityApplicationMaxPortalDto> response = new Response<>(ResponseCode.SUCCESS, true,
                        "ENTITY APPLICATION SAVED SUCCESSFULLY", null);
        try {
            LimsUser applicant = utility.getUser();

            EntityApplication application = new EntityApplication();
            if (id > 0) {

                Optional<EntityApplication> applicationExisting = entityApplicationRepo.findById(id);
                if (!applicationExisting.isPresent()) {

                        throw new DataNotFoundException("APPLICATION NOT FOUND");
                }

                application = applicationExisting.get();

                if (!application.getIsDraft()) {

                        throw new OperationNotAllowedException("CANNOT EDIT SUBMITTED APPLICATION");
                }

                application.setUpdatedAt(LocalDateTime.now());
            }

            Optional<EntityProduct> productExistance = entityProductRepo.findFirstByApplicationTypeAndActive(applicationType, true);

            if (!productExistance.isPresent()) {

                throw new DataNotFoundException("PRODUCT NOT FOUND");
            }

            EntityProduct product=productExistance.get();

            application.setApplicant(applicant.getId());
            application.setApplicantEntity(applicant.getUserEntity());
            application.setIsDraft(Boolean.FALSE);
            application.setEntityProduct(product);
            application.setStatus(statusRepo.getOne(submittedStatusId));

            application = entityApplicationRepo.saveAndFlush(application);

            String workflowTypeCode=null;
            if(applicationType.equals(EntityApplicationTypeEnum.NAME_CHANGE)){

                workflowTypeCode=changeOfNameWorkflowTypeCode;
                this.saveNameChangeDetails(data.getChangeOfName(), application.getId());
                
                //update operational status of an entity
                applicant.getUserEntity().setOperationalStatus(OperationalEnum.OnEntityNameChange.toString());
                entityRepo.save(applicant.getUserEntity());
                
            }else if(applicationType.equals(EntityApplicationTypeEnum.SHAREHOLDER_CHANGE)){

                workflowTypeCode=changeOfShareholderWorkflowTypeCode;
                this.saveShareholderChangeDetails(data.getShareholderChange(), application.getId());
                
                //update operational status of an entity
                applicant.getUserEntity().setOperationalStatus(OperationalEnum.OnShareholderChange.toString());
                entityRepo.save(applicant.getUserEntity());
                
            }else{}

            if (data.getAttachments().size() > 0) {

                    attachmentService.saveAttachments(data.getAttachments(), application);
            }

            taskService.intiateEntityTrack(application, Boolean.TRUE, workflowTypeCode);// initiate
                                 
            statusHistoryService.saveStatusHistory(application, application.getStatus());// save task status history
            
            response=this.findEntityApplicationById(application.getId());
        } catch (DataNotFoundException e) {

                log.error(e.getLocalizedMessage());
                throw new DataNotFoundException(e.getLocalizedMessage());
        } catch (Exception e) {

                e.printStackTrace();
                throw new GeneralException("INTERNAL_SERVER_ERROR");
        }

        return response;
    }

    @Override
    public Response<EntityApplicationMaxPortalDto> findEntityApplicationById(Long id) {
        Response<EntityApplicationMaxPortalDto> response = new Response<>(ResponseCode.SUCCESS, true,
                        "ENTITY APPLICATION RETRIEVED SUCCESSFULLY", null);
        try {
            LimsUser user = utility.getUser();

            Optional<EntityApplication> existance = entityApplicationRepo
                            .findByApplicantEntityIdAndId(user.getUserEntity().getId(), id);
            if (!existance.isPresent()) {

                    response.setMessage("ENTITY APPLICATION NOT FOUND");
                    return response;
            }

            EntityApplication entityApplication = existance.get();
            EntityApplicationMaxPortalDto data = new EntityApplicationMaxPortalDto();
            data.setId(entityApplication.getId());
            data.setStatus(entityApplication.getStatus().getDisplayName());
            data.setApprovedAt(entityApplication.getApprovedAt());
            data.setCreatedAt(entityApplication.getCreatedAt());
            data.setIsDraft(entityApplication.getIsDraft());
            data.setCurrentDecision(entityApplication.getDecision());
            data.setCreator(userRepo.findApplicantMinDtoById(entityApplication.getApplicant()));
            data.setAttachments(attachmentService.getAttachmentsMax(entityApplication));
            data.setProduct(entityApplication.getEntityProduct().getDisplayName());
            data.setEntity(licenceeEntityService.composeLicenceeEntityMinDto(entityApplication.getApplicantEntity()));
            data.setStatusHistory(statusHistoryService.getStatusHistoryByTrackable(entityApplication));
            data.setBills(billingService.getLicenseBillingByLicenseid(entityApplication.getId(),BillingAttachedToEnum.ENTITY_APPLICATION));
            data.setShareholderChange(this.getShareholderChangeDetailsByApplicationId(entityApplication.getId()));
            data.setChangeOfName(this.getNameChangeByApplicationId(entityApplication.getId()));
            
            data.setFees(feeStructureService.findFeeStructureByFeeable(entityApplication.getEntityProduct()));
            
            response.setData(data);
        } catch (Exception e) {

                log.error(e.getMessage());
                e.printStackTrace();
                response.setMessage("ENTITY APPLICATION NOT FOUND");
        }

        return response;
    }

    @Override
    public Response<List<EntityApplicationMinPortalDto>> getEntityApplicationByApplicant() {
            Response<List<EntityApplicationMinPortalDto>> response = new Response<>(ResponseCode.SUCCESS, true,
                            "ENTITY APPLICATIONS RETRIEVED SUCCESSFULLY", null);
            try {
                    LimsUser user = utility.getUser();
                    List<EntityApplicationMinPortalDto> data = entityApplicationRepo
                                    .findByApplicantEntityId(user.getUserEntity().getId());

                    if (data.size() == 0) {

                            response.setMessage("ENTITY APPLCATIONS NOT FOUND");
                            return response;
                    }

                    response.setData(data);

            } catch (Exception e) {

                    e.printStackTrace();
                    response.setMessage("FAILURE TO RETRIEVE DATA");
            }

            return response;
    }

    @Override
    public Response<List<LicenceeEntityMinMinPortalDto>> getListOfEntities() {
		Response<List<LicenceeEntityMinMinPortalDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITIES RETRIEVED SUCCESSFULLY", null);
		try {
			List<LicenceeEntityMinMinPortalDto> data = entityRepo.findAllEntitiesPortalByStatus(true);

			if (data.size() == 0) {

				response.setMessage("ENTITIES NOT FOUND");
				return response;
			}

			response.setData(data);

		} catch (Exception e) {

			e.printStackTrace();
			response.setMessage("FAILURE TO RETRIEVE DATA");
		}

		return response;
	}

    @Override
    public Response<LicenceeEntityMinPortalDto> findLicenceeById(Long id) {
		Response<LicenceeEntityMinPortalDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITIES RETRIEVED SUCCESSFULLY", null);
		try {
			Optional<LicenceeEntity> existance = entityRepo.findById(id);

			if (existance.isPresent()) {

				response.setMessage("ENTITY NOT FOUND");
				return response;
			}

			LicenceeEntity entity = existance.get();
			LicenceeEntityMinPortalDto data = new LicenceeEntityMinPortalDto();
			data.setName(entity.getName());
			data.setEmail(entity.getEmail());
			data.setPhone(entity.getPhone());
			data.setFax(entity.getFax());
			data.setWebsite(entity.getWebsite());
			data.setPhysicalAddress(entity.getPhysicalAddress());
			data.setPostalAddress(entity.getPostalAddress());
			data.setPostalCode(entity.getPostalCode());
			data.setCategoryName(entity.getCategory().getName());

			response.setData(data);

		} catch (Exception e) {

			e.printStackTrace();
			response.setMessage("FAILURE TO RETRIEVE DATA");
		}

		return response;
	}

    @Override
    public Response<List<EntityApplicationMinPortalDto>> getPendingCertificateOfRegistraEntityApplicationsByApplicant() {
		Response<List<EntityApplicationMinPortalDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"PENDING ADENDUM ENTITY APPLICATIONS RETRIEVED SUCCESSFULLY", null);
		try {
			LimsUser user = utility.getUser();
			List<EntityApplicationMinPortalDto> data = entityApplicationRepo.findByApplicantEntityIdAndDecision(
					user.getUserEntity().getId(), WorkflowDecisionEnum.AGREE.toString());

			if (data.size() == 0) {

				response.setMessage("PENDING ADENDUM ENTITY APPLCATIONS NOT FOUND");
				return response;
			}

			response.setData(data);

		} catch (Exception e) {

			e.printStackTrace();
			response.setMessage("FAILURE TO RETRIEVE DATA");
		}

		return response;
	}

    @Override
    @Transactional
    public Response<SaveResponseDto> savePortalActivity(
			TaskActionPortalDto data, Long id) {
		Response<SaveResponseDto> response = new Response(ResponseCode.SUCCESS, true, "DATA SAVED SUCCESSFULLY",
				new SaveResponseDto(HttpStatus.CREATED.value(), HttpStatus.CREATED.toString(),
						System.currentTimeMillis()));
		try {

			LimsUser actor = utility.getUser();

			// get pending track
			Optional<TaskTrack> trackExistance = trackRepo
					.findFirstByTrackableIdAndTrackableTypeAndIsActedAndActorIdOrderByCreatedAtDesc(id,
							TrackableTypeEnum.ENTITY_APPLICATION, Boolean.FALSE, actor.getId());
			if (!trackExistance.isPresent()) {

                            throw new OperationNotAllowedException("TRACK DATA NOT FOUND");
			}
                        
			TaskTrack track = trackExistance.get();

			// compose task action object
			TaskActionDto action = new TaskActionDto();
			action.setComment(data.getComment());
			action.setId(track.getId());
			action.setDecision(data.getDecision());
			action.setActors(new ArrayList());
			action.setFindings(new ArrayList());

			Response<TaskActivity> activity = taskService.saveActivity(action);

			if (data.getAttachments().size() > 0) {

				attachmentService.saveAttachments(data.getAttachments(), activity.getData());
			}

		} catch (DataNotFoundException e) {

			log.error(e.getLocalizedMessage());
			throw new DataNotFoundException(e.getLocalizedMessage());

		} catch (OperationNotAllowedException e) {

			log.error(e.getLocalizedMessage());
			throw new OperationNotAllowedException(e.getLocalizedMessage());

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL_SERVER_ERROR");
		}

		return response;
	}

    @Override
    public ShareholderChangeDto getShareholderChangeDetailsByApplicationId(Long applicationId) {
        ShareholderChangeDto response=null;
        
        try{
        
            List<ShareholderChangeApplicationDetail> data=shareholderChangeApplicationDetailRepo.findShareholderDtoByApplicationId(applicationId);
            List<ShareholderDto> shareholders=new ArrayList();
            for(ShareholderChangeApplicationDetail dt : data){
            
                ShareholderDto sh=new ShareholderDto();
                sh.setFullname(dt.getFullname());
                sh.setNationality(dt.getNationality());
                sh.setShares(dt.getShares());
                sh.setAttachments(attachmentService.getAttachments(dt));
                
                shareholders.add(sh);
            }
            
            response=new ShareholderChangeDto();
            response.setShareholders(shareholders);
            
        }catch(Exception e){
        
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public NameChangeDto getNameChangeByApplicationId(Long applicationId) {
        NameChangeDto response=null;
        try{
        
            response=nameChangeApplicationDetailRepo.findNameChangeDtoByApplicationId(applicationId);
            
        }catch(Exception e){
        
            e.printStackTrace();
        }
        
        return response;
    }

//    @Override
//    public String getWorkflowTypeCode(EntityApplicationTypeEnum applicationType) {
//        String response=null;
//        if(applicationType.equals(EntityApplicationTypeEnum.NAME_CHANGE)){
//            
//            response=changeOfNameWorkflowTypeCode;
//            
//        }else if(applicationType.equals(EntityApplicationTypeEnum.SHAREHOLDER_CHANGE)){
//        
//            response=changeOfShareholderWorkflowTypeCode;
//        }
//        
//        return response;
//    }

    @Override
    public void saveShareholderChangeDetails(ShareholderChangeDto data, Long applicationId) throws DataNotFoundException, Exception {
        // check shareholders
        if (data.getShareholders().size() == 0) {

            throw new DataNotFoundException("SHAREHOLDERS DATA NOT FOUND");
        }

        shareholderChangeApplicationDetailRepo.deleteByApplicationId(applicationId);
        for (ShareholderDto sh : data.getShareholders()) {

            ShareholderChangeApplicationDetail shareholder = new ShareholderChangeApplicationDetail();
            shareholder.setFullname(sh.getFullname());
            shareholder.setNationality(sh.getNationality());
            shareholder.setShares(sh.getShares());
            shareholder.setApplicationId(applicationId);

            shareholderChangeApplicationDetailRepo.save(shareholder);
        }
    }

    @Override
    public void saveNameChangeDetails(NameChangeDto data, Long applicationId) throws DataNotFoundException, Exception {
        if (data.getName().trim().isEmpty()) {

            throw new DataNotFoundException("NAME IS REQUIRED");
        }

        nameChangeApplicationDetailRepo.deleteByApplicationId(applicationId);

        NameChangeApplicationDetail nameChange=new NameChangeApplicationDetail();
        nameChange.setName(data.getName());
        nameChangeApplicationDetailRepo.save(nameChange);
    }
}
