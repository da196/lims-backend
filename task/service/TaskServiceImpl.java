/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import tz.go.tcra.lims.attachments.dto.AttachmentTypeMinDto;
import tz.go.tcra.lims.attachments.repository.AttachmentRepository;
import tz.go.tcra.lims.attachments.repository.AttachmentTypeRepository;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.communications.dto.CommunicationChannel;
import tz.go.tcra.lims.communications.entity.NotificationsIn;
import tz.go.tcra.lims.communications.service.NotificationsInService;
import tz.go.tcra.lims.entity.Attachment;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.entity.LicenceApplicationServiceDetail;
import tz.go.tcra.lims.entity.LicenceApplicationShareholder;
import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.miscellaneous.entity.Form;
import tz.go.tcra.lims.entity.FormFinding;
import tz.go.tcra.lims.feestructure.entity.Feeable;
import tz.go.tcra.lims.miscellaneous.entity.FormItem;
import tz.go.tcra.lims.product.entity.ProductWorkflow;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.entity.TaskWorkflow;
import tz.go.tcra.lims.task.entity.LicencePresentation;
import tz.go.tcra.lims.task.dto.ActivityAttachmentDto;
import tz.go.tcra.lims.task.dto.ActorMinDto;
import tz.go.tcra.lims.task.dto.TaskActionDto;
import tz.go.tcra.lims.task.dto.TaskActivityDto;
import tz.go.tcra.lims.task.dto.FormFindingDto;
import tz.go.tcra.lims.task.dto.FormFindingItemMinDto;
import tz.go.tcra.lims.task.dto.FormFindingMaxDto;
import tz.go.tcra.lims.task.dto.TrackDto;
import tz.go.tcra.lims.task.dto.TaskDetailDto;
import tz.go.tcra.lims.licence.repository.LicenceApplicationServiceRepository;
import tz.go.tcra.lims.miscellaneous.dto.ApplicantPublicNoticeDto;
import tz.go.tcra.lims.miscellaneous.dto.DocumentDto;
import tz.go.tcra.lims.miscellaneous.dto.MinistryConsultationLetterDto;
import tz.go.tcra.lims.miscellaneous.dto.PublicNoticeDto;
import tz.go.tcra.lims.miscellaneous.entity.ConsultationLetterConfig;
import tz.go.tcra.lims.miscellaneous.enums.FormFlagEnum;
import tz.go.tcra.lims.miscellaneous.enums.LicenceStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.MinistryEnum;
import tz.go.tcra.lims.miscellaneous.enums.NumberQueTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowStepTypeEnum;
import tz.go.tcra.lims.miscellaneous.repository.ConsultationLetterConfigRepository;
import tz.go.tcra.lims.miscellaneous.service.DocumentService;
import tz.go.tcra.lims.payment.dto.BillingControlNumberRequestDto;
import tz.go.tcra.lims.payment.service.BillingService;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.uaa.entity.UserRole;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityRepository;
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.uaa.repository.UserRoleRepository;
import tz.go.tcra.lims.uaa.service.RoleService;
import tz.go.tcra.lims.uaa.service.UserService;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.utils.exception.OperationNotAllowedException;
import tz.go.tcra.lims.utils.service.NumberQueService;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;
import tz.go.tcra.lims.workflow.entity.WorkflowStepDecision;
import tz.go.tcra.lims.workflow.repository.WorkflowStepDecisionRepository;
import tz.go.tcra.lims.workflow.service.WorkflowService;
import tz.go.tcra.lims.miscellaneous.repository.FormItemRepository;
import tz.go.tcra.lims.miscellaneous.repository.FormRepository;
import tz.go.tcra.lims.task.repository.FormFindingRepository;
import tz.go.tcra.lims.task.repository.LicencePresentationRepository;
import tz.go.tcra.lims.miscellaneous.dto.IndividualLicenceCertificateDto;
import tz.go.tcra.lims.miscellaneous.enums.FormFeedbackTypeEnum;
import tz.go.tcra.lims.miscellaneous.repository.FormItemOptionRepository;
import tz.go.tcra.lims.miscellaneous.service.FormService;
import tz.go.tcra.lims.workflow.entity.Workflow;
import tz.go.tcra.lims.product.repository.ProductWorkflowRepository;
import tz.go.tcra.lims.feestructure.repository.FeeStructureRepository;
import tz.go.tcra.lims.licence.dto.LicenceMaxDto;
import tz.go.tcra.lims.licence.repository.LicenceApplicationEntityShareholderRepository;
import tz.go.tcra.lims.licence.service.LicenseCategoryService;
import tz.go.tcra.lims.licence.service.LicenseService;
import tz.go.tcra.lims.licencee.dto.EntityApplicationMaxDto;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.licencee.repository.EntityApplicationRepository;
import tz.go.tcra.lims.licencee.repository.ShareholderChangeApplicationDetailRepository;
import tz.go.tcra.lims.licencee.service.EntityApplicationService;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;
import tz.go.tcra.lims.task.dto.TaskMinDto;
import tz.go.tcra.lims.task.repository.TaskWorkflowRepository;
import tz.go.tcra.lims.task.entity.Trackable;
import tz.go.tcra.lims.task.repository.TaskActivityRepository;
import tz.go.tcra.lims.task.repository.TaskTrackRepository;
import tz.go.tcra.lims.licence.repository.LicenceRepository;
import tz.go.tcra.lims.licencee.entity.ShareholderChangeApplicationDetail;
import tz.go.tcra.lims.utils.EnglishNumberToWords;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService{

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
    
    @Autowired
    private PagedResourcesAssembler<TaskMinDto> pagedResourcesAssembler;
    
    @Autowired
    private TaskTrackRepository trackRepo;
    
    @Autowired
    private TaskActivityRepository activityRepo;
    
    @Autowired
    private FormFindingRepository formFindingRepo;
    
    @Autowired
    private FormRepository formRepo;
    
    @Autowired
    private FormItemRepository formItemRepo;
    
    
    @Autowired
    private FormItemOptionRepository formItemOptionRepo;
    
    @Autowired
    private LicenceRepository licenceRepo;
    
    @Autowired
    private TaskWorkflowRepository taskWorkflowRepo;
    
    @Autowired
    private ProductWorkflowRepository productWorkflowRepo;
    
    @Autowired
    private UserRepository userRepo;
        
    @Autowired
    private WorkflowStepDecisionRepository stepDecisionRepo;
            
    @Autowired
    private UserRoleRepository userRoleRepo;
    
    @Autowired
    private FeeStructureRepository feeStructureRepo;
    
    @Autowired
    private LicenceeEntityRepository licenceeEntityRepo;
    
    @Autowired
    private ConsultationLetterConfigRepository consultationLetterConfigRepo;
    
    @Autowired
    private AttachmentTypeRepository attachmentTypeRepo;
    
    @Autowired
    private AttachmentRepository attachmentRepo;
    
    @Autowired
    private LicenceApplicationServiceRepository licenceServiceRepo;
    
    @Autowired
    private LicencePresentationRepository licencePresentationRepo;
    
    @Autowired
    private EntityApplicationRepository entityApplicationRepo;
    
    @Autowired
    private ShareholderChangeApplicationDetailRepository changeOfShareholderDetailRepo;
    
    @Autowired
    private LicenceApplicationEntityShareholderRepository licenseApplicationEntityShareholderRepo;
    
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private NotificationsInService notificationService;
    
    @Autowired
    private LicenseService licenceService;
    
    @Autowired
    private BillingService billingService;
    
    @Autowired
    private NumberQueService numberQueService;
    
    @Autowired
    private DocumentService documentService;
    
    @Autowired
    private LicenseCategoryService categoryService;
    
    @Autowired
    private FormService formService;
    
    @Autowired
    private TaskStatusHistroyService statusHistoryService;
    
    @Autowired
    private EntityApplicationService entityApplicationService;
    
    @Autowired
    private AppUtility utility;
    
    @Autowired
    private EnglishNumberToWords numberToWords;
    
    @Value("${lims.licence.new.workflow.type.code}")
    private String newLicenceWorkflowTypeCode;
    
    @Value("${lims.licence.renew.workflow.type.code}")
    private String renewLicenceWorkflowTypeCode;
    
    @Value("${lims.licence.upgrade.workflow.type.code}")
    private String upgradeLicenceWorkflowTypeCode;
    
    @Value("${lims.licence.transferofownership.workflow.type.code}")
    private String transferOfOwnershipLicenceWorkflowTypeCode;
    
    @Value("${lims.changeofshareholder.workflow.type.code}")
    private String changeOfShareholderWorkflowTypeCode;
    
    @Value("${lims.licence.cancellation.workflow.type.code}")
    private String cancellationLicenceWorkflowTypeCode;
    
    @Value("${lims.licence.track.advance.flag.int}")
    private Integer advanceFlag;
    
    @Value("${lims.licence.track.return.flag.int}")
    private Integer returnFlag;
    
    @Value("${lims.licence.track.reject.flag.int}")
    private Integer rejectFlag;
    
    @Value("${lims.licence.track.resubmit.flag.int}")
    private Integer resubmitFlag;
    
    @Value("${lims.applicant.role.id}")
    private Long applicantRole;
    
    @Value("${lims.licence.staff.group.email.address}")
    private String staffEmail;
    
    @Value("${lims.public.notice.attachment.type}")
    private Long publicNoticeAttachmentType;
    
    @Value("${lims.consultation.letter.attachment.type}")
    private Long consultationLetterAttachmentType;
    
    @Value("${lims.licence.certificate.attachment.type}")
    private Long licenceCertificateAttachmentType;
    
    @Value("${lims.content.category.id}")
    private Long contentCategoryId;
    
    @Value("${lims.licence.certificate.custodian.name}")
    private String custodianName;
    
    @Value("${lims.licence.certificate.custodian.title}")
    private String custodianTitle;
    
    Long applicantProperty;
    
    Feeable feableProperty;
    
    Trackable trackableProperty;
    
    Licence licenceProperty;
    
    EntityApplication entityApplicationProperty;
    
    LicenceeEntity licenceeEntityProperty;
    
    @Override
    public Response<CollectionModel<EntityModel<TaskMinDto>>> getAllTasks(int page, int size, String sortName,String sortType) {
        Response<CollectionModel<EntityModel<TaskMinDto>>> response=new Response<>(ResponseCode.SUCCESS,true,"TASKS RETRIEVED SUCCESSFULLY",null);
        try{
        
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortName).descending());
            if ("ASC".equals(sortType.toUpperCase())) {

                    pageable = PageRequest.of(page, size, Sort.by(sortName).ascending());
            }

            Page<TaskMinDto> data=trackRepo.findAllTasks(pageable);
            
            if(!data.hasContent()){
            
                response.setMessage("TASKS NOT FOUND");
                return response;
            }
            
            response.setData(pagedResourcesAssembler.toModel(data));
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<TaskMinDto>>> getMyTasks(int page, int size, String sortName,String sortType) {
        Response<CollectionModel<EntityModel<TaskMinDto>>> response=new Response<>(ResponseCode.SUCCESS,true,"TASKS RETRIEVED SUCCESSFULLY",null);
        try{
        
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortName).descending());
            if ("ASC".equals(sortType.toUpperCase())) {

                pageable = PageRequest.of(page, size, Sort.by(sortName).ascending());
            }

            LimsUser actor=utility.getUser();
            Page<TaskMinDto> data=trackRepo.findMyTasks(actor.getId(),pageable);
            
            if(!data.hasContent()){
            
                response.setMessage("TASKS NOT FOUND");
                return response;
            }
            
            response.setData(pagedResourcesAssembler.toModel(data));
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<ActorMinDto> getNextActorsByCurrentWorkflowAndStepNumber(Long workflowId,Integer stepNumber) {
        List<ActorMinDto> response=new ArrayList();
        try{
        
            WorkflowStep step=workflowService.getNextWorkflowStep(workflowId, stepNumber);
            
            if(step == null){
            
                return response;
            }
            
            List<ActorMinDto> actors=userRepo.findActorsByRole(step.getCurrentRole().getId());
            
            if(actors.size() == 0){
            
                return response;
            }
            
            response=actors;
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<TaskActivityDto> getTaskActivities(Trackable trackable) {
        List<TaskActivityDto> response=new ArrayList();
        try{ 
            
            List<TaskActivity> licenceActivities=activityRepo.findByTrackable(trackable);
            for(TaskActivity licenceActivity : licenceActivities){
                
                TaskActivityDto activity=new TaskActivityDto();
                activity.setId(licenceActivity.getId());
                activity.setActivityName(licenceActivity.getActivityName());
                activity.setDecision(licenceActivity.getDecision());
                activity.setComments(licenceActivity.getComments());
                activity.setAttachments(attachmentService.getAttachmentsMax(licenceActivity));
                activity.setCreatedAt(licenceActivity.getCreatedAt());
                activity.setUpdatedAt(licenceActivity.getUpdatedAt());
                
                Optional<ActorMinDto> actor=userRepo.findActorById(licenceActivity.getTrack().getActorId());
                if(actor.isPresent()){
                    
                    activity.setActor(actor.get());
                }
                
                if(licenceActivity.getFormId() > 0){
                
                    activity.setForm(this.getLicenseFormFindings(licenceActivity.getId()));
                }
                
                response.add(activity);
            }
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public List<TrackDto> getTaskTracks(Trackable trackable) {
        List<TrackDto> response=new ArrayList();
        try{
            
            List<TaskTrack> tracks=trackRepo.findByTrackableOrderByCreatedAtDesc(trackable);
            for(TaskTrack taskTrack : tracks){
                
                TrackDto track=new TrackDto();
                track.setId(taskTrack.getId());
                track.setIsActed(taskTrack.getIsActed());
                track.setStepName(taskTrack.getWorkflowStep().getName());
                track.setDueDate(taskTrack.getDueDate());
                track.setCreatedAt(taskTrack.getCreatedAt());
                track.setUpdatedAt(taskTrack.getUpdatedAt());
                track.setCompletedAt(taskTrack.getCompletedAt());
                
                if(taskTrack.getActorId() == 0){                    
                    continue;
                }
                
                ActorMinDto actor=userService.getActorMinDto(taskTrack.getActorId());
                
                if(actor != null){
                    
                    track.setActor(actor.getFirstName()+" "+actor.getMiddleName()+" "+actor.getLastName());
                    track.setActorRole(roleService.getRoleMinDto(taskTrack.getActorRoleId()));
                }
                
                response.add(track);
            }
            
        }catch(Exception e){
            
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    @Transactional
    public Response<TaskActivity> saveActivity(TaskActionDto data) {
        Response<TaskActivity> response=new Response<>(ResponseCode.SUCCESS,true,"ACTIVITY SAVED SUCCESSFULLY",null);
        try{
            LimsUser actor=utility.getUser();

            Optional<TaskTrack> existing=trackRepo.findByIdAndIsActedAndActorIdOrderByCreatedAtDesc(data.getId(), Boolean.FALSE,actor.getId());
            //check to see if activity is submitted by the required actor
            if(!existing.isPresent()){

                throw new OperationNotAllowedException("USER NOT THE ASSIGNED ACTOR ["+actor.getId()+"]");
            }

            TaskTrack track=existing.get();
            
            Optional<WorkflowStepDecision> existingStepDecision=stepDecisionRepo.findByStepAndDecision(track.getWorkflowStep().getId(), data.getDecision());

            if(!existingStepDecision.isPresent()){

                throw new OperationNotAllowedException("DECISION NOT CONFIGURED FOR THE STEP");
            }

            WorkflowStepDecision stepDecision=existingStepDecision.get(); 
            if(data.getDecision() != WorkflowDecisionEnum.SAVE){
            
                track.setCompletedAt(LocalDateTime.now());
                track.setIsActed(Boolean.TRUE);
                track.setUpdatedAt(LocalDateTime.now());
                track=trackRepo.saveAndFlush(track);
            }else{
            
                track.setUpdatedAt(LocalDateTime.now());
                track=trackRepo.saveAndFlush(track);
            }
                
            this.setTrackingData(track);
            
            TaskActivity activity=new TaskActivity();            
            //retrieve activity for the tracking info
            TaskActivity existingActivity=activityRepo.findFirstByTrack(track);                
            if(existingActivity != null){

                activity=existingActivity;
                activity.setUpdatedAt(LocalDateTime.now());
            }
            
            //save activity data            
            activity.setActivityName(track.getWorkflowStep().getName());
            activity.setDecision(data.getDecision());
            activity.setTrack(track);
            activity.setComments(data.getComment());
            activity.setTrackable(trackableProperty);
            activity=activityRepo.saveAndFlush(activity);

            response.setData(activity);

            //save findings if form filled
            if(data.getFindings().size() > 0){

                Long formId=this.saveFormFindings(data.getFindings(), activity.getId());
                activity.setFormId(formId);
                activityRepo.save(activity);
            }
            
            if(licenceProperty != null){
                
                //save application status
                licenceProperty.setDecision(data.getDecision().toString());
                licenceProperty.setStatus(stepDecision.getLicenceStatus());
                licenceProperty.setUpdatedAt(LocalDateTime.now());
                licenceProperty=licenceRepo.save(licenceProperty);  
            }
            
            if(entityApplicationProperty != null){
            
                //save application status
                entityApplicationProperty.setDecision(data.getDecision().toString());
                entityApplicationProperty.setStatus(stepDecision.getLicenceStatus());
                entityApplicationProperty.setUpdatedAt(LocalDateTime.now());
                entityApplicationProperty=entityApplicationRepo.save(entityApplicationProperty);
            }
            
            //save status history
            statusHistoryService.saveStatusHistory(trackableProperty, stepDecision.getLicenceStatus());
                
            //get decision
            Integer isAdvancing=utility.interpreteDecision(data.getDecision());

            if(isAdvancing == returnFlag){

                //go back 1 step
                this.returnTaskTracker(data, track);

            }else if(isAdvancing == advanceFlag){

                //advance 1 step forward
                this.advanceTaskTracker(data, track);

            }else if(isAdvancing == rejectFlag){

                //end tracking here with a rejected status            
                this.rejectTaskTracker(track);

            }else if(isAdvancing == resubmitFlag){

                //trigger applicantProperty resubmission
                 this.resubmitTracker(track,data.getComment());

            }else{
                //nothing to do
            }
            
            //process applicantProperty notification if any
            if(stepDecision.getNotificationTemplate() != null){

                //call method to process notification
                this.composeNotification(track, stepDecision, Boolean.TRUE);
            }
            
            this.composeNotification(track, stepDecision, Boolean.FALSE);
            
        }catch(OperationNotAllowedException e){
        
            throw new OperationNotAllowedException(e.getLocalizedMessage());
            
        }catch(DataNotFoundException e){
        
            throw new DataNotFoundException(e.getLocalizedMessage());
        }catch(Exception e){
        
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    @Transactional
    public Boolean intiateLicenceTrack(Licence licence,Boolean isPayable,String licenceWorkflowTypeCode) throws DataNotFoundException,Exception {
        Boolean response=Boolean.TRUE;                       
        Optional<ProductWorkflow> workflowExisting = productWorkflowRepo
                                    .findByProductableAndWorkflowTypeCode(licence.getLicenseProduct(), licenceWorkflowTypeCode,Boolean.TRUE);

        if (!workflowExisting.isPresent()) {

            throw new DataNotFoundException("PRODUCT WORKFLOWS NOT FOUND");
        }

        ProductWorkflow productWorkflow=workflowExisting.get();

        //save licenceProperty tracking            
        TaskTrack track=new TaskTrack();

        //get the first step in a workflow
        WorkflowStep step=workflowService.getNextWorkflowStep(productWorkflow.getWorkflow().getId(),0);
        List<Long> feeIds=new ArrayList();
        if(step.getStepType() == WorkflowStepTypeEnum.PAYMENT){

            if(isPayable){

                feeIds = feeStructureRepo
                                    .findFeeIdByFeeableAndApplicableStateAndActive(licence.getLicenseProduct(), step.getApplicableState(),true);

                if(feeIds.size() == 0){

                    throw new DataNotFoundException("FEES FOR THE LICENCE PRODUCT NOT FOUND");
                }
            }else{

                step=workflowService.getNextWorkflowStep(step.getWorkflow(),step.getStepNumber());
                //get step actor
                UserRole userRole=userService.getUserRole(step.getCurrentRole().getId());
                track.setActorRoleId(userRole.getRoleID());
                track.setActorId(userRole.getUserID());
            }

        }else if(step.getStepType() == WorkflowStepTypeEnum.DOCUMENT_GENERATION){

            DocumentDto document=new DocumentDto();
            documentService.generateDocument(document, step.getAttachmentType().getId());

        }else{

            //get step actor
            UserRole userRole=userService.getUserRole(step.getCurrentRole().getId());
            track.setActorRoleId(userRole.getRoleID());
            track.setActorId(userRole.getUserID());
        }

        track.setWorkflowId(productWorkflow.getWorkflow().getId());
        track.setTrackable(licence);
        track.setIsActed(Boolean.FALSE);
        track.setIsAssociated(Boolean.FALSE);
        track.setWorkflowStep(step);
        track.setTrackName(step.getName());

        track=trackRepo.save(track);

        //assign licenceProperty to a workflow
        TaskWorkflow taskWorkflow = new TaskWorkflow();
        taskWorkflow.setTrackable(track.getTrackable());
        taskWorkflow.setWorkflowId(productWorkflow.getWorkflow().getId());
        taskWorkflow=taskWorkflowRepo.saveAndFlush(taskWorkflow);

        if(feeIds.size() > 0){

            BillingControlNumberRequestDto bill=new BillingControlNumberRequestDto();
            bill.setFeeIds(feeIds);
            bill.setLicenceId(licence.getId());
            bill.setBillable(BillingAttachedToEnum.LICENCE);
            billingService.initiateBill(bill);
        }
        
        return response;
    }

    @Override
    public FormFindingMaxDto getLicenseFormFindings(Long activityId) {
        FormFindingMaxDto response=new FormFindingMaxDto();
        try{ 
            //get activity details
            Optional<TaskActivity> existingActivity=activityRepo.findById(activityId);
                        
            if(!existingActivity.isPresent()){
            
                return null;
            }
            
            TaskActivity activity=existingActivity.get();
            Optional<Form> existing=formRepo.findById(activity.getFormId());
            if(!existing.isPresent()){
            
                return null;
            }
            
            Form fm=existing.get();            
            response=new FormFindingMaxDto();
            response.setId(fm.getId());
            response.setCode(fm.getCode());
            response.setName(fm.getName());
            response.setDescription(fm.getDescription());            
            response.setType(fm.getFormType());
            
            //get all form level1s
            List<FormItem> level1s=formItemRepo.findByFormIdAndFlag(fm.getId(),FormFlagEnum.SECTION);
            List<FormFindingItemMinDto> level1Items=new ArrayList();
            for(FormItem level1 : level1s){
                
                FormFindingItemMinDto level1Item=new FormFindingItemMinDto();
                level1Item.setId(level1.getId());
                level1Item.setName(level1.getName());
                level1Item.setDisplayName(level1.getDisplayName());
                level1Item.setFlag(level1.getFlag());
                level1Item.setFeedbackType(level1.getFeedbackType());
                
                if(level1.getFeedbackType() == FormFeedbackTypeEnum.FIXED){
                
                    level1Item.setOptions(formItemOptionRepo.findByFormItemId(level1.getId(), Boolean.TRUE));
                }
                
                if(level1.getFeedbackType() != FormFeedbackTypeEnum.NONE){
                        
                    Optional<FormFinding> finding=formFindingRepo.findFirstByFormItemIdAndActivityId(level1.getId(), activityId);
                    if(finding.isPresent()){
                        level1Item.setFinding(finding.get().getFindings());
                        level1Item.setRemarks(finding.get().getRemarks());
                    }
                    
                }
                
                List<FormFindingItemMinDto> level2Items=new ArrayList();
                
                //get all level1 subsection
                List<FormItem> level2s=formItemRepo.findByParent(level1.getId());
                for(FormItem level2 : level2s){
                
                    FormFindingItemMinDto level2Item=new FormFindingItemMinDto();
                    level2Item.setId(level2.getId());
                    level2Item.setName(level2.getName());
                    level2Item.setDisplayName(level2.getDisplayName());
                    level2Item.setFlag(level2.getFlag());
                    level2Item.setFeedbackType(level2.getFeedbackType());
                
                    if(level2.getFeedbackType() == FormFeedbackTypeEnum.FIXED){

                        level2Item.setOptions(formItemOptionRepo.findByFormItemId(level2.getId(), Boolean.TRUE));
                    }
                
                    if(level2.getFeedbackType() != FormFeedbackTypeEnum.NONE){
                        
                        Optional<FormFinding> finding=formFindingRepo.findFirstByFormItemIdAndActivityId(level2.getId(), activityId);
                        if(finding.isPresent()){
                            level2Item.setFinding(finding.get().getFindings());
                            level2Item.setRemarks(finding.get().getRemarks());
                        }
                        
                    }
                    //get all the subsection level3s
                    List<FormFindingItemMinDto> level3Items=new ArrayList();
                    List<FormItem> level3s=formItemRepo.findByParent(level2.getId());
                    for(FormItem level3 : level3s){
                    
                        FormFindingItemMinDto level3Item=new FormFindingItemMinDto();
                        level3Item.setId(level3.getId());
                        level3Item.setName(level3.getName());
                        level3Item.setDisplayName(level3.getDisplayName());
                        level3Item.setFlag(level3.getFlag());
                        level3Item.setFeedbackType(level3.getFeedbackType());
                
                        if(level3.getFeedbackType() == FormFeedbackTypeEnum.FIXED){

                            level3Item.setOptions(formItemOptionRepo.findByFormItemId(level3.getId(), Boolean.TRUE));
                        }
                        
                        if(level3.getFeedbackType() != FormFeedbackTypeEnum.NONE){
                        
                            Optional<FormFinding> finding=formFindingRepo.findFirstByFormItemIdAndActivityId(level3.getId(), activityId);
                            if(finding.isPresent()){
                                level3Item.setFinding(finding.get().getFindings());
                                level3Item.setRemarks(finding.get().getRemarks());
                            }
                            
                        }
                        
                            
                        //get all the level3 level4s
                        List<FormFindingItemMinDto> level4Items=new ArrayList();
                        List<FormItem> level4s=formItemRepo.findByParent(level3.getId());
                        for(FormItem level4 : level4s){
                        
                            FormFindingItemMinDto level4Item=new FormFindingItemMinDto();
                            level4Item.setId(level4.getId());
                            level4Item.setName(level4.getName());
                            level4Item.setDisplayName(level4.getDisplayName());
                            level4Item.setFlag(level4.getFlag());
                            level4Item.setFeedbackType(level4.getFeedbackType());
                
                            if(level4.getFeedbackType() == FormFeedbackTypeEnum.FIXED){

                                level4Item.setOptions(formItemOptionRepo.findByFormItemId(level4.getId(), Boolean.TRUE));
                            }
                            
                            //get findings
                            if(level4.getFeedbackType() != FormFeedbackTypeEnum.NONE){
                                
                                Optional<FormFinding> finding=formFindingRepo.findFirstByFormItemIdAndActivityId(level4.getId(), activityId);
                                if(finding.isPresent()){
                                    level4Item.setFinding(finding.get().getFindings());
                                    level4Item.setRemarks(finding.get().getRemarks());
                                }
                                
                            }
                            
                            level4Items.add(level4Item);
                        }
                        level3Item.setItems(level4Items);
                        
                        level3Items.add(level3Item);
                    }
                    
                    level2Item.setItems(level3Items);
                    
                    level2Items.add(level2Item);
                }
                
                level1Item.setItems(level2Items);
                
                level1Items.add(level1Item);
            }
            
            response.setItems(level1Items);
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public Long saveFormFindings(List<FormFindingDto> findings,Long activityId) throws DataNotFoundException, Exception {
        Long response=0L;
        for(FormFindingDto finding : findings){
                
            Optional<FormItem> existingItem=formItemRepo.findById(finding.getId());

            if(!existingItem.isPresent()){

                throw new DataNotFoundException("FORM ITEM NOT FOUND ["+finding.getId()+"]");
            }
            response=existingItem.get().getFormId();
            
           
            FormFinding fnd=new FormFinding();
            
            Optional<FormFinding> existingFinding=formFindingRepo.findFirstByFormItemIdAndActivityId(finding.getId(), activityId);
            if(existingFinding.isPresent()){
            
                fnd=existingFinding.get();
                fnd.setUpdatedAt(LocalDateTime.now());
            }
            
            fnd.setFormItemId(finding.getId());
            fnd.setActivityId(activityId);
            fnd.setFindings(finding.getFinding());
            fnd.setRemarks(finding.getRemarks());

            formFindingRepo.saveAndFlush(fnd);
        }
        
        return response;
    }

    @Override
    @Transactional
    public Boolean advanceTaskTracker(TaskActionDto action,TaskTrack previousTrack) throws OperationNotAllowedException,DataNotFoundException, Exception {
        Boolean response=Boolean.FALSE;
        //get next step
        WorkflowStep nextStep=workflowService.getNextWorkflowStep(previousTrack.getWorkflowId(), previousTrack.getWorkflowStep().getStepNumber());
        
        if(nextStep == null){            
            
            return this.closeTaskTrack(previousTrack);
        }
        
        //check if task is an association
        if(previousTrack.getIsAssociated()){
        
            //check to see if there exists any actor in the association have pending tasks 
            if(trackRepo.existsByTrackableIdAndTrackableTypeAndAssociationIdAndIsActed(trackableProperty.getId(),trackableProperty.getTrackableType(), previousTrack.getAssociationId(), Boolean.FALSE)){
            
                return Boolean.TRUE;
            }
        }
        
        List<Long> actors=new ArrayList();
        if(action.getDecision().equals(WorkflowDecisionEnum.ASSIGN)){
            //check to see if users have been assigned
            if(action.getActors().size() == 0){
            
                throw new OperationNotAllowedException("ACTORS LIST IS REQUIRED");
            }
            
            actors=action.getActors();
        }else if(action.getDecision().equals(WorkflowDecisionEnum.INVITE)){
        
            //find applicantProperty and assign task
            if(nextStep.getCurrentRole() != null){
            
                actors.add(applicantProperty);
                this.savePresentation(action.getPresentationDate(),action.getComment(),action.getDecision(), previousTrack);
            }
            
        }else if(action.getDecision().equals(WorkflowDecisionEnum.AGREE)){
        
            //find applicantProperty and assign task
            if(nextStep.getCurrentRole() != null){
            
                actors.add(applicantProperty);
            }
            
        }else if(action.getDecision().equals(WorkflowDecisionEnum.ACKNOWLEDGE)){
        
            //get the previous track step
            Optional<TaskTrack> trk=trackRepo.findFirstByTrackableIdAndTrackableTypeAndIdLessThanAndIsActedOrderByIdDesc(trackableProperty.getId(),trackableProperty.getTrackableType(),previousTrack.getId(),Boolean.TRUE);
            if(trk.isPresent()){
                this.savePresentation(null,null,action.getDecision(), trk.get());
            }
            
            //find actors by current role id
            if(nextStep.getCurrentRole() != null){
            
                actors=userRoleRepo.getUserIdByRoleIDAndActive(nextStep.getCurrentRole().getId(),Boolean.TRUE);
            
                if(actors.size() == 0){

                    throw new DataNotFoundException("ACTORS NOT FOUND");
                }
            }
            
        }else{
        
            //find actors by current role id
            if(nextStep.getCurrentRole() != null){
            
                actors=userRoleRepo.getUserIdByRoleIDAndActive(nextStep.getCurrentRole().getId(),Boolean.TRUE);
            
                if(actors.size() == 0){

                    throw new DataNotFoundException("ACTORS NOT FOUND");
                }
            }
        }
        
        if(actors.size() == 0){
        
            TaskTrack track=new TaskTrack();
            track.setTrackable(previousTrack.getTrackable());
            track.setWorkflowId(nextStep.getWorkflow());
            track.setWorkflowStep(nextStep);
            track.setTrackName(nextStep.getName());
            track.setAssigningActorId(previousTrack.getActorId());
            track.setAssigningActorRoleId(previousTrack.getAssigningActorRoleId());
            track=trackRepo.save(track);
            
            if(nextStep.getStepType() == WorkflowStepTypeEnum.DOCUMENT_GENERATION){
            
                //check if document type has been configured
                if(nextStep.getAttachmentType() == null){
                
                    throw new OperationNotAllowedException("ATTACHMENT TYPE NOT CONFIGURED FOR THE STEP");
                }
                
                DocumentDto document=this.composeDocumentDto(track, nextStep.getAttachmentType().getId());
                String uri=documentService.generateDocument(document, nextStep.getAttachmentType().getId());
                
                //get activity for the previous track
                TaskActivity activity=activityRepo.findFirstByTrack(previousTrack);
                
                AttachmentType attachmentType=attachmentTypeRepo.getOne(nextStep.getAttachmentType().getId());
                
                //save attachment with activity attachable
                Attachment attachment=new Attachment();
                attachment.setUri(uri);
                attachment.setAttachmentName(nextStep.getName());
                attachment.setAttachmentType(attachmentType);
                attachment.setAttachable(activity);
                
                attachmentRepo.save(attachment);
                
//                complete activity
                track.setIsActed(Boolean.TRUE);
                track.setUpdatedAt(LocalDateTime.now());
                track=trackRepo.save(track);
                
//                call this method to advance the licenceProperty to the next step                
                this.advanceTaskTracker(action, track);
                
            }else if(nextStep.getStepType() == WorkflowStepTypeEnum.PAYMENT){
                
                
                if(track.getWorkflowStep().getApplicableState().compareTo(ApplicableStateEnum.ACCOUNT_INITIATION) != 0){
                    
                    List<Long> feeIds = feeStructureRepo
					.findFeeIdByFeeableAndApplicableStateAndActive(feableProperty, track.getWorkflowStep().getApplicableState(),true);
                
                    if(feeIds.size() == 0){

                        throw new DataNotFoundException("APPLICABLE FEES FOR THE PRODUCT NOT FOUND");
                    }

                    //raise biil
                    BillingControlNumberRequestDto bill=new BillingControlNumberRequestDto();
                    bill.setFeeIds(feeIds);
                    bill.setLicenceId(trackableProperty.getId());

                    if(trackableProperty.getTrackableType().equals(TrackableTypeEnum.ENTITY_APPLICATION)){

                        bill.setBillable(BillingAttachedToEnum.ENTITY_APPLICATION);
                    }else if(trackableProperty.getTrackableType().equals(TrackableTypeEnum.LICENCE)){

                        bill.setBillable(BillingAttachedToEnum.LICENCE);
                    }

                    billingService.initiateBill(bill);
                }
                
            }else{
            
                throw new OperationNotAllowedException("ACTORS NOT FOUND");
            }
            
            return Boolean.TRUE;
        }
        
        Boolean isAssociated=Boolean.FALSE;
        int associationId=0;
        if(actors.size() > 1){
        
            isAssociated=Boolean.TRUE;
            Random rand = new Random();
            associationId=rand.nextInt(10000000);
        }
        
        Date dueDate=action.getDueDate();
        if(dueDate == null){
            
            Calendar c=Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, nextStep.getDueDays());
            dueDate=c.getTime();
            
        }else{
            
            this.checkDueDate(nextStep, dueDate);
        
        }
        
        
        
        
        
        for(Long actor : actors){
            
            TaskTrack track=new TaskTrack();
            track.setActorId(actor);
            track.setActorRoleId(nextStep.getCurrentRole().getId());
            track.setIsAssociated(isAssociated);
            track.setAssociationId(associationId);
            track.setTrackable(trackableProperty);
            track.setWorkflowId(nextStep.getWorkflow());
            track.setWorkflowStep(nextStep);
            track.setTrackName(nextStep.getName());
            track.setAssigningActorId(previousTrack.getActorId());
            track.setAssigningActorRoleId(previousTrack.getAssigningActorRoleId());
            track.setDueDate(dueDate);
            trackRepo.save(track);        
        }
        
        response=Boolean.TRUE;
        
        return response;
    }

    @Override
    public Boolean returnTaskTracker(TaskActionDto action,TaskTrack previousTrack) throws DataNotFoundException, Exception {
        Boolean response=Boolean.FALSE;
        
        //get the previous workflow step
        WorkflowStep prevStep=workflowService.getPreviousWorkflowStep(previousTrack.getWorkflowId(), previousTrack.getWorkflowStep().getStepNumber());
        if(prevStep == null){
            //update licenceProperty workflow status
            List<TaskWorkflow> licenceWorkflows=taskWorkflowRepo.findByTrackableIdAndTrackableTypeAndActive(trackableProperty.getId(),trackableProperty.getTrackableType(), Boolean.TRUE);
            TaskWorkflow licenceWorkflow=licenceWorkflows.get(0);
            licenceWorkflow.setActive(Boolean.FALSE);
            taskWorkflowRepo.save(licenceWorkflow);
            
            return Boolean.TRUE;
        }
        
        //get the previous assignees
        List<TaskTrack> previousTracks=trackRepo.findByWorkflowIdAndWorkflowStepAndTrackableIdAndTrackableTypeAndIsActed(prevStep.getWorkflow(), prevStep,trackableProperty.getId(),trackableProperty.getTrackableType(), Boolean.TRUE);
        
        if(prevStep.getStepType() == WorkflowStepTypeEnum.DOCUMENT_GENERATION ||
                prevStep.getStepType() == WorkflowStepTypeEnum.PAYMENT){
        
            this.returnTaskTracker(action, previousTracks.get(0));
        }
        
        //get the previous track step
        Optional<TaskTrack> trk1=trackRepo.findFirstByTrackableIdAndTrackableTypeAndIdLessThanAndIsActedOrderByIdDesc(trackableProperty.getId(),trackableProperty.getTrackableType(),previousTrack.getId(),Boolean.TRUE);
        if(trk1.isPresent()){

            this.savePresentation(null,null,action.getDecision(), trk1.get());
        }
        
        Boolean isAssociated=Boolean.FALSE;
        int associationId=0;
        if(previousTracks.size() > 1){
        
            isAssociated=Boolean.TRUE;
            Random rand = new Random();
            associationId=rand.nextInt(10000000);
        }
        
        Date dueDate=action.getDueDate();
        if(dueDate == null){
            
            Calendar c=Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, prevStep.getDueDays());
            dueDate=c.getTime();
            
        }else{
        
            this.checkDueDate(prevStep, dueDate);
        }
        
        
        for(TaskTrack trk : previousTracks){
            
            TaskTrack track=new TaskTrack();
            track.setActorId(trk.getActorId());
            track.setActorRoleId(prevStep.getCurrentRole().getId());
            track.setIsAssociated(isAssociated);
            track.setAssociationId(associationId);
            track.setTrackable(trackableProperty);
            track.setWorkflowId(prevStep.getWorkflow());
            track.setWorkflowStep(prevStep);
            track.setTrackName(prevStep.getName());
            track.setDueDate(dueDate);
            trackRepo.save(track);        
        }
        
        response=Boolean.TRUE;
        
        return response;
    }

    @Override
    public Boolean rejectTaskTracker(TaskTrack previousTrack) throws DataNotFoundException, Exception {
        Boolean response=Boolean.FALSE;
        
        //update licenceProperty workflow status
        List<TaskWorkflow> licenceWorkflows=taskWorkflowRepo.findByTrackableIdAndTrackableTypeAndActive(trackableProperty.getId(),trackableProperty.getTrackableType(), Boolean.TRUE);
        TaskWorkflow licenceWorkflow=licenceWorkflows.get(0);
        licenceWorkflow.setActive(Boolean.FALSE);
        taskWorkflowRepo.save(licenceWorkflow);
        response=Boolean.TRUE;
        
        if(licenceProperty != null){
        
            licenceProperty.setOperationalStatus(null);
            licenceRepo.save(licenceProperty);
        }
        
        return response;
    }

    @Override
    public Boolean resubmitTracker(TaskTrack previousTrack,String comments) throws DataNotFoundException, Exception {
        Boolean response=Boolean.FALSE;
        
        List<TaskWorkflow> licenceWorkflows=taskWorkflowRepo.findByTrackableIdAndTrackableTypeAndActive(trackableProperty.getId(),trackableProperty.getTrackableType(), Boolean.TRUE);
        TaskWorkflow licenceWorkflow=licenceWorkflows.get(0);
        licenceWorkflow.setActive(Boolean.FALSE);
        taskWorkflowRepo.save(licenceWorkflow);
        
        if(licenceProperty != null){
            
            //update application draft status
            licenceProperty.setIsDraft(Boolean.TRUE);
            licenceProperty.setComments(comments);
            licenceProperty=licenceRepo.save(licenceProperty);
        }
        
        if(entityApplicationProperty != null){
            
            //update application draft status
            entityApplicationProperty.setIsDraft(Boolean.TRUE);
            entityApplicationProperty.setComments(comments);
            entityApplicationProperty=entityApplicationRepo.save(entityApplicationProperty);
        }
        
        return response;
    }

    @Override
    public void composeNotification(TaskTrack track, WorkflowStepDecision decision,Boolean isApplicant) {
        try{
            String message=null;
            String productName=null;
            String status=null;
            String applicant=null;
            String phone=null;
            String email=null;
            
            if(licenceProperty != null){

                productName=licenceProperty.getLicenseProduct().getName(); 
                status=licenceProperty.getStatus().getDisplayName();
                applicant=licenceeEntityProperty.getUser().getFirstName()+" "+licenceeEntityProperty.getUser().getLastName();
                phone=licenceeEntityProperty.getPhone();
                email=licenceeEntityProperty.getEmail();
            }
            
            if(entityApplicationProperty != null){

                productName=entityApplicationProperty.getEntityProduct().getName();
                status=entityApplicationProperty.getStatus().getDisplayName();
                
                Optional<LimsUser> usr=userRepo.findById(entityApplicationProperty.getApplicant());
                
                applicant=usr.get().getFirstName()+" "+usr.get().getLastName();
                phone=usr.get().getPhone();
                email=entityApplicationProperty.getApplicantEntity().getEmail();
            }
                        
            NotificationsIn notification=null;
            
            if(decision.getNotificationTemplate().getEmailTemplate() != null && isApplicant){
            
                message=String.format(decision.getNotificationTemplate().getEmailTemplate(),applicant,productName);
                //email message
                notification=new NotificationsIn();
                notification.setContact(email);
                notification.setSubject(productName+"["+status+"]");
                notification.setMessage(message);
                notification.setChannel(CommunicationChannel.EMAIL);
                notificationService.saveNotificationsIn(notification);
            }
            
            if(decision.getNotificationTemplate().getTextTemplate() != null && isApplicant){
                
                message=String.format(decision.getNotificationTemplate().getTextTemplate(),applicant,productName);
                //text message   
                notification=new NotificationsIn();
                notification.setContact(phone);
                notification.setSubject("TCRA");
                notification.setMessage(message);                
                notification.setChannel(CommunicationChannel.SMS);
                notificationService.saveNotificationsIn(notification);
            }
            
            if(decision.getNotificationTemplate().getStaffEmailTemplate() != null && !isApplicant){
            
                Optional<ActorMinDto> actor=userRepo.findActorById(track.getActorId());
                
                if(actor.isPresent()){
                
                    message=String.format(decision.getNotificationTemplate().getStaffEmailTemplate(),actor.get().getFirstName()+" "+actor.get().getLastName(),productName);
                    //email message
                    notification=new NotificationsIn();
                    notification.setContact(staffEmail);
                    notification.setSubject("TCRA LICENCE UPDATE");
                    notification.setMessage(message);
                    notification.setChannel(CommunicationChannel.EMAIL);
                    notificationService.saveNotificationsIn(notification);
                }
            }
            
        }catch(Exception e){
            
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Response<TaskDetailDto> viewTask(Long trackId) {
        Response<TaskDetailDto> response=new Response<>(ResponseCode.SUCCESS,true,"TASK DETAILS VIEWED SUCCESSFULLY",null);
        try{
            LimsUser actor=utility.getUser();
            
            Optional<TaskTrack> existingTrack=trackRepo.findByIdAndIsActedAndActorIdOrderByCreatedAtDesc(trackId, Boolean.FALSE,actor.getId());
            if(!existingTrack.isPresent()){
            
                response.setMessage("TRACK DATA NOT FOUND");
                return response;
            }
            
            TaskTrack track=existingTrack.get();
            TaskDetailDto taskDetails=new TaskDetailDto();
            if(track.getTrackable().getTrackableType().equals(TrackableTypeEnum.LICENCE)){
            
                Response<LicenceMaxDto> licence=licenceService.findLicenseById(track.getTrackable().getId());
                taskDetails.setLicence(licence.getData());
            }
            
            if(track.getTrackable().getTrackableType().equals(TrackableTypeEnum.ENTITY_APPLICATION)){
                
                Response<EntityApplicationMaxDto> entityApplication=entityApplicationService.findEntityApplicationById(track.getTrackable().getId());
                taskDetails.setEntityApplication(entityApplication.getData());
            }
            
            taskDetails.setId(track.getId());
            taskDetails.setTaskName(track.getWorkflowStep().getName());
            
            
            List<WorkflowDecisionEnum> decisions=stepDecisionRepo.findDecisionEnumByStepAndActive(track.getWorkflowStep().getId(), true);
            
            if(decisions.contains(WorkflowDecisionEnum.ASSIGN)){
            
                List<ActorMinDto> actors=this.getNextActorsByCurrentWorkflowAndStepNumber(track.getWorkflowId(), track.getWorkflowStep().getStepNumber());
                if(actors.size() > 0){
                    
                    taskDetails.setActors(actors);
                }
            }
            
            taskDetails.setDecisions(decisions);
            taskDetails.setDueDate(track.getDueDate());
            taskDetails.setTrackableType(track.getTrackableType());
            taskDetails.setCreatedAt(track.getCreatedAt());
            
            //check for activity details and return data if available
            TaskActivity activity=activityRepo.findFirstByTrack(track);
            
            if(activity != null){
            
                taskDetails.setActivity(this.composeTaskActivity(activity));
            }
            
            //check if any attachment type is configured for the activity
            if(track.getWorkflowStep().getAttachmentType() != null){
            
                taskDetails.setAttachmentType(this.composeAttachmentTypeMinDto(track.getWorkflowStep().getAttachmentType()));
            }
            
            //check if any attachment type is configured for the activity
            if(track.getWorkflowStep().getForm() != null){
            
                taskDetails.setForm(formService.getFormDetails(track.getWorkflowStep().getForm().getId()).getData());
            }
            
            response.setData(taskDetails);
            
        }catch(Exception e){
        
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    @Transactional
    public void receivePaymentNotification(Long trackableId,String trackableType) {
        try{

            if(trackableType.equals(TrackableTypeEnum.ENTITY_APPLICATION.toString())){
            
                Optional<EntityApplication> existingApplication=entityApplicationRepo.findById(trackableId);
                if(!existingApplication.isPresent()){

                    throw new DataNotFoundException("ENTITY APPLICATION NOT FOUND");
                }
                trackableProperty=existingApplication.get();
            }
            
            if(trackableType.equals(TrackableTypeEnum.LICENCE.toString())){
            
                Optional<Licence> existingLicense=licenceRepo.findById(trackableId);
                if(!existingLicense.isPresent()){

                    throw new DataNotFoundException("LICENCE NOT FOUND");
                } 
                trackableProperty=existingLicense.get();
            }
            
            Optional<TaskTrack> existingTrack=trackRepo.findFirstByTrackableIdAndTrackableTypeAndIsActedOrderByCreatedAtDesc(trackableProperty.getId(),trackableProperty.getTrackableType(), Boolean.FALSE);
            if(!existingTrack.isPresent()){
            
                throw new DataNotFoundException("TRACK DATA NOT FOUND");
            }
            
            TaskTrack track=existingTrack.get();
            
            this.setTrackingData(track);
            
            if(track.getActorId() > 0){
            
                throw new OperationNotAllowedException("ILLEGAL OPERATION");
            }
            
            track.setIsActed(Boolean.TRUE);
            track.setUpdatedAt(LocalDateTime.now());
            track.setCompletedAt(LocalDateTime.now());
            track=trackRepo.save(track);
            
            if(track.getWorkflowStep().getStepNumber() == 1 && 
                    trackableProperty.getTrackableType().equals(TrackableTypeEnum.LICENCE)){
                
                //que licenceProperty for generation of application number
                numberQueService.queLicence(licenceProperty,NumberQueTypeEnum.APPLICATION);
            }
            
            TaskActionDto action=new TaskActionDto();
            action.setDecision(WorkflowDecisionEnum.PAYMENT);
            action.setId(track.getId());
            
            //progress the licenceProperty to the next step
            this.advanceTaskTracker(action, track); 
            
            Optional<WorkflowStepDecision> existingStepDecision=stepDecisionRepo.findByStepAndDecision(track.getWorkflowStep().getId(), action.getDecision());
        
            if(!existingStepDecision.isPresent()){

                throw new OperationNotAllowedException("DECISION NOT CONFIGURED FOR THE STEP");
            }
        
            WorkflowStepDecision stepDecision=existingStepDecision.get();
            
            if(licenceProperty != null){
            
                //save application status
                licenceProperty.setDecision(stepDecision.getDecision().toString());
                licenceProperty.setStatus(stepDecision.getLicenceStatus());
                licenceProperty.setUpdatedAt(LocalDateTime.now());
                licenceProperty=licenceRepo.save(licenceProperty);
            }
            
            if(entityApplicationProperty != null){
            
                //save application status
                entityApplicationProperty.setDecision(stepDecision.getDecision().toString());
                entityApplicationProperty.setStatus(stepDecision.getLicenceStatus());
                entityApplicationProperty.setUpdatedAt(LocalDateTime.now());
                entityApplicationProperty=entityApplicationRepo.save(entityApplicationProperty);
            }
            
            //save status history
            statusHistoryService.saveStatusHistory(trackableProperty, stepDecision.getLicenceStatus());
            
//            compose notification to be sent to the applicant
            if(track.getWorkflowStep().isApplicantNotify()){
            
                this.composeNotification(track, stepDecision, Boolean.TRUE);                
            }
            
            this.composeNotification(track, stepDecision, Boolean.FALSE);
            
        }catch(Exception e){
            
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Response<TaskActivity> saveActivityAttachment(ActivityAttachmentDto data) {
        Response<TaskActivity> response=new Response<>(ResponseCode.SUCCESS,true,"ACTIVITY ATTACHMENT SAVED SUCCESSFULLY",null);
        try{            
            Optional<TaskActivity> existingActivity=activityRepo.findById(data.getActivityId());
            
            if(!existingActivity.isPresent()){
            
                throw new DataNotFoundException("ACTIVITY NOT FOUND");
            }
            
            TaskActivity activity=existingActivity.get();
            
            AttachmentDto attachment=new AttachmentDto();
            attachment.setFiles(data.getUri());
            attachment.setAttachmentType(data.getAttachmentType());
            
            attachmentService.saveAttachment(attachment, activity);
            
            response.setData(activity);
            
        }catch(DataNotFoundException e){
        
            throw new DataNotFoundException(e.getLocalizedMessage());
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL_SERVER_ERROR");
        }
        
        return response;
    }

    @Override
    public DocumentDto composeDocumentDto(TaskTrack track, Long attachmentType) throws DataNotFoundException,Exception {
        DocumentDto response=new DocumentDto();
        LocalDateTime now = LocalDateTime.now();
        List<ApplicantPublicNoticeDto> applicants=new ArrayList();
        //get category level 2
        LicenceCategory category=categoryService.getLevel1LicenceCategoryByProduct(licenceProperty.getLicenseProduct());
       
        String shareholders="";
        String shares="";
        String services="";
        for(LicenceApplicationShareholder shareholder : licenceProperty.getApplicantEntity().getShareholders()){
            
            shareholders +=shareholder.getFullname()+" ("+shareholder.getNationality()+")\n";
            shares +=shareholder.getShares()+"\n";
        }
        
        if(attachmentType == publicNoticeAttachmentType){
        
            PublicNoticeDto pb=new PublicNoticeDto();
            ApplicantPublicNoticeDto applicant=new ApplicantPublicNoticeDto();
            applicant.setApplicantEntityName(licenceProperty.getApplicantEntity().getName());
            applicant.setLicenceCategory(category.getDisplayName());
            applicant.setShareholders(shareholders);
            applicant.setShares(shares);

            applicants.add(applicant);
        
            pb.setApplicants(applicants);
        
            response.setPublicNotice(pb);
            
        }else if(attachmentType == consultationLetterAttachmentType){
        
            Set<LicenceApplicationServiceDetail> serviceDetails=licenceServiceRepo.findByLicenseId(applicantRole);
            for(LicenceApplicationServiceDetail serviceDetail:serviceDetails){
                services+=serviceDetail.getService().getName()+"\n";
            }
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");   
            MinistryConsultationLetterDto lt=new MinistryConsultationLetterDto();
            ApplicantPublicNoticeDto applicant=new ApplicantPublicNoticeDto();
            applicant.setApplicantEntityName(licenceProperty.getApplicantEntity().getName());
            applicant.setLicenceServices(services);
            applicant.setLicenceCategory(category.getDisplayName());
            applicant.setShareholders(shareholders);
            applicant.setShares(shares);
            
            applicants.add(applicant);
            
            lt.setApplicants(applicants);
            lt.setDirectorGeneralName(custodianName);
            
            ConsultationLetterConfig config=null;
            if(category.getId() == contentCategoryId){
            
                Optional<ConsultationLetterConfig> existing=consultationLetterConfigRepo.findByFlag(MinistryEnum.HABARI);
                if(!existing.isPresent()){
                
                    throw new DataNotFoundException("CONFIGURATION NOT FOUND");
                }
                config=existing.get();
                
            }else{
            
                Optional<ConsultationLetterConfig> existing=consultationLetterConfigRepo.findByFlag(MinistryEnum.MAWASILIANO);
                if(!existing.isPresent()){
                
                    throw new DataNotFoundException("CONFIGURATION NOT FOUND");
                }
                config=existing.get();
            }
            
            lt.setDestinationAddress(config.getAddress());
            lt.setMinistryName(config.getMinistryName());
            lt.setMawasilianoHabari(config.getActivity());
            lt.setDate(dtf.format(now));
            
            response.setLetter(lt);
        }else if(attachmentType == licenceCertificateAttachmentType){
        
            Integer duration=licenceProperty.getLicenseProduct().getDuration();
            String durationString=numberToWords.convert(duration).toUpperCase()+" ("+duration+")";
                
            DateTimeFormatter datedtf = DateTimeFormatter.ofPattern("dd"); 
            DateTimeFormatter monthdtf = DateTimeFormatter.ofPattern("MMMM"); 
            DateTimeFormatter yeardtf = DateTimeFormatter.ofPattern("yyyy"); 
            
            int date=Integer.parseInt(now.format(datedtf));
            String dateSuffix=utility.getDayOfMonthSuffix(date);
            String month=now.format(monthdtf);
            String year=now.format(yeardtf);
            
            IndividualLicenceCertificateDto cert=new IndividualLicenceCertificateDto();
            cert.setDuration(durationString);
            cert.setApplicantEntityName(licenceProperty.getApplicantEntity().getName().toUpperCase());
            cert.setLicenceNumber(licenceProperty.getLicenceNumber());
            cert.setProductName(licenceProperty.getLicenseProduct().getDisplayName().toUpperCase());
            cert.setIssueDate1(date+dateSuffix.toUpperCase()+" "+month.toUpperCase()+", "+year);
            cert.setIssueDate2(date+dateSuffix+" day of "+month.toUpperCase()+", "+year);
            cert.setPostalAddress(licenceProperty.getApplicantEntity().getPostalAddress());
            cert.setCustodianName(custodianName);
            cert.setCustodianTitle(custodianTitle);
            
            response.setCertificate(cert);
        }
        
        return response;
    }

    @Override
    public TaskActivityDto getTaskActivity(Long activityId) {
        TaskActivityDto activity=new TaskActivityDto();
        Optional<TaskActivity> existing=activityRepo.findById(activityId);
        
        if(!existing.isPresent()){
        
            return null;
        }
        
        TaskActivity licenceActivity=existing.get();
        
        activity.setId(licenceActivity.getId());
        activity.setActivityName(licenceActivity.getActivityName());
        activity.setDecision(licenceActivity.getDecision());
        activity.setComments(licenceActivity.getComments());
        activity.setAttachments(attachmentService.getAttachmentsMax(licenceActivity));
        activity.setCreatedAt(licenceActivity.getCreatedAt());
        activity.setUpdatedAt(licenceActivity.getUpdatedAt());

        Optional<ActorMinDto> actor=userRepo.findActorById(licenceActivity.getTrack().getActorId());
        if(actor.isPresent()){

            activity.setActor(actor.get());
        }

        if(licenceActivity.getFormId() > 0){

            activity.setForm(this.getLicenseFormFindings(licenceActivity.getId()));
        }
        
        return activity;
    }

    @Override
    public TaskActivityDto composeTaskActivity(TaskActivity licenceActivity) {
        TaskActivityDto activity=new TaskActivityDto();
        
        activity.setId(licenceActivity.getId());
        activity.setActivityName(licenceActivity.getActivityName());
        activity.setDecision(licenceActivity.getDecision());
        activity.setComments(licenceActivity.getComments());
        activity.setAttachments(attachmentService.getAttachmentsMax(licenceActivity));
        activity.setCreatedAt(licenceActivity.getCreatedAt());
        activity.setUpdatedAt(licenceActivity.getUpdatedAt());

        Optional<ActorMinDto> actor=userRepo.findActorById(licenceActivity.getTrack().getActorId());
        if(actor.isPresent()){

            activity.setActor(actor.get());
        }

        if(licenceActivity.getFormId() > 0){

            activity.setForm(this.getLicenseFormFindings(licenceActivity.getId()));
        }
        
        return activity;
    }

    @Override
    public AttachmentTypeMinDto composeAttachmentTypeMinDto(AttachmentType attachmentType) {
        AttachmentTypeMinDto response=new AttachmentTypeMinDto();
        try{
            
            response.setId(attachmentType.getId());
            response.setName(attachmentType.getName());
            response.setDescription(attachmentType.getDescription());
            
        }catch(Exception e){
        
            e.printStackTrace();
            return null;
        }
        
        return response;
    }

    @Override
    public LicencePresentation savePresentation(LocalDateTime date,String remark,WorkflowDecisionEnum decision, TaskTrack track) throws Exception{
        LicencePresentation presentation=new LicencePresentation();
        Optional<LicencePresentation> existingPresentation=licencePresentationRepo.findFirstByLicenceIdAndWorkflowIdAndWorkflowStepIdAndActive(licenceProperty.getId(), track.getWorkflowId(), track.getWorkflowStep().getId(),false);
        if(existingPresentation.isPresent()){

            presentation=existingPresentation.get();
            presentation.setUpdatedAt(LocalDateTime.now());
        }

        if(decision.equals(WorkflowDecisionEnum.INVITE)){
        
            presentation.setPresentationDate(date);
            presentation.setWorkflowId(track.getWorkflowId());
            presentation.setWorkflowStepId(track.getWorkflowStep().getId());
            presentation.setLicence(licenceProperty);
            presentation.setRemark(remark);
        }
        
        if(decision.equals(WorkflowDecisionEnum.ACKNOWLEDGE)){
        
            presentation.setActive(Boolean.TRUE);
        }
        
        presentation.setStatus(decision);

        presentation=licencePresentationRepo.saveAndFlush(presentation);
        return presentation;
    }

    @Override
    @Transactional
    public Boolean closeTaskTrack(TaskTrack track) throws Exception{
        Boolean response=Boolean.TRUE;
        Workflow workflow=workflowService.getWorkflow(track.getWorkflowId());
        Date issueDate=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(issueDate);
        c.add(Calendar.YEAR, licenceProperty.getLicenseProduct().getDuration());
        Date expiryDate = c.getTime();
        
        if(workflow.getWorkflowType().getCode().equals(newLicenceWorkflowTypeCode)){
            
            numberQueService.queLicence(licenceProperty,NumberQueTypeEnum.LICENCE);
                        
            licenceProperty.setDecision(WorkflowDecisionEnum.GRANTED.toString());
            licenceProperty.setIssuedDate(issueDate);
            licenceProperty.setExpireDate(expiryDate);
            licenceProperty.setLicenseState(LicenceStateEnum.ACTIVE);
                        
        }else if(workflow.getWorkflowType().getCode().equals(renewLicenceWorkflowTypeCode)){
        
            //on renewing assign same licenceProperty number as that issued previously  
            Optional<Licence> oldLicence=licenceRepo.findById(licenceProperty.getReferenceLicenceId());
            if(!oldLicence.isPresent()){

                throw new DataNotFoundException("LICENCE DATA NOT AVAILABLE");
            }

            c.setTime(oldLicence.get().getExpireDate());
            c.add(Calendar.DAY_OF_MONTH, 1);
            issueDate=c.getTime();
            
            c.setTime(issueDate);
            c.add(Calendar.YEAR,licenceProperty.getLicenseProduct().getDuration());
            expiryDate = c.getTime();
            
            licenceProperty.setDecision(WorkflowDecisionEnum.GRANTED.toString());
            licenceProperty.setIssuedDate(issueDate);
            licenceProperty.setExpireDate(expiryDate);
            licenceProperty.setLicenseState(LicenceStateEnum.ACTIVE);
            licenceProperty.setLicenceNumber(oldLicence.get().getLicenceNumber());
        
        }else if(workflow.getWorkflowType().getCode().equals(upgradeLicenceWorkflowTypeCode)){
                    
            //get the parent licenceProperty
            Optional<Licence> parentLicenceExisting=licenceRepo.findById(licenceProperty.getReferenceLicenceId());
            if(parentLicenceExisting.isPresent()){
            
                Licence parentLicence=parentLicenceExisting.get();
                parentLicence.setLicenseState(LicenceStateEnum.UPGRADED);
                parentLicence.setUpdatedAt(LocalDateTime.now());
                parentLicence.setOperationalStatus(null);
                licenceRepo.save(parentLicence);
                
                licenceProperty.setExpireDate(parentLicence.getExpireDate()); 
                licenceProperty.setLicenceNumber(parentLicence.getLicenceNumber());
            }
            
            licenceProperty.setDecision(WorkflowDecisionEnum.GRANTED.toString());
            licenceProperty.setIssuedDate(issueDate);
            licenceProperty.setExpireDate(expiryDate);
            licenceProperty.setLicenseState(LicenceStateEnum.ACTIVE);
            
            numberQueService.queLicence(licenceProperty,NumberQueTypeEnum.LICENCE);
            
        }else if(workflow.getWorkflowType().getCode().equals(transferOfOwnershipLicenceWorkflowTypeCode)){
        
            //get the parent licenceProperty
            Optional<Licence> parentLicenceExisting=licenceRepo.findById(licenceProperty.getReferenceLicenceId());
            if(parentLicenceExisting.isPresent()){
            
                Licence parentLicence=parentLicenceExisting.get();
                parentLicence.setLicenseState(LicenceStateEnum.TRANSFERED);
                parentLicence.setUpdatedAt(LocalDateTime.now());
                parentLicence.setOperationalStatus(null);
                licenceRepo.save(parentLicence);
                
                licenceProperty.setExpireDate(parentLicence.getExpireDate()); 
                licenceProperty.setLicenceNumber(parentLicence.getLicenceNumber());
            }
            
            licenceProperty.setDecision(WorkflowDecisionEnum.GRANTED.toString());
            licenceProperty.setIssuedDate(new Date());
            licenceProperty.setLicenseState(LicenceStateEnum.ACTIVE);            
        }
        
        if(workflow.getWorkflowType().getCode().equals(cancellationLicenceWorkflowTypeCode)){
        
            licenceProperty.setDecision(WorkflowDecisionEnum.CANCELLED.toString());
            licenceProperty.setLicenseState(LicenceStateEnum.CANCELLED);            
            licenceProperty.setCancelledAt(LocalDateTime.now()); 
            licenceProperty.setOperationalStatus(null);
        }
        
        licenceProperty.setUpdatedAt(LocalDateTime.now());
        licenceProperty=licenceRepo.save(licenceProperty);//save licence property data
        
        if(workflow.getWorkflowType().getCode().equals(changeOfShareholderWorkflowTypeCode)){
        
            //change current shareholders for the entity
            List<ShareholderChangeApplicationDetail> datz=changeOfShareholderDetailRepo.findShareholderDtoByApplicationId(entityApplicationProperty.getId());
            List<ShareholderDto> shareholders=new ArrayList();
            for(ShareholderChangeApplicationDetail dt : datz){
            
                ShareholderDto sh=new ShareholderDto();
                sh.setFullname(dt.getFullname());
                sh.setNationality(dt.getNationality());
                sh.setShares(dt.getShares());
                sh.setAttachments(attachmentService.getAttachments(dt));
                
                shareholders.add(sh);
            }
            
            userService.saveShareholders(shareholders, entityApplicationProperty.getApplicantEntity().getId());
            
            //change shareholders for all the active/suspended licences
            List<LicenceStateEnum> states=new ArrayList();
            states.add(LicenceStateEnum.ACTIVE);
            states.add(LicenceStateEnum.SUSPENDED);
            
            //get all active/suspended licences for the entity with shareholder change
            List<Licence> licences=licenceRepo.findByLicenceeEntityIdAndLicenseState(entityApplicationProperty.getApplicantEntity().getId(),states);
            for(Licence licence : licences){
                
                //deactivate all licenceProperty application shareholders
                licenseApplicationEntityShareholderRepo.deActivateLicenceApplicationShareholders(Boolean.FALSE, licence.getApplicantEntity().getId());
                
                //save new licenceProperty application shareholders
                this.saveApplicantionEntityShareholders(shareholders, licence.getApplicantEntity().getId());
            }
            
            //update entity application status
            entityApplicationProperty.setActive(Boolean.FALSE);
            entityApplicationProperty.setUpdatedAt(LocalDateTime.now());
            entityApplicationProperty.setApprovedAt(LocalDateTime.now());            
            entityApplicationProperty=entityApplicationRepo.save(entityApplicationProperty);
        }
        
        //deactivate licenceProperty workflow activeness
        List<TaskWorkflow> taskWorkflows=taskWorkflowRepo.findByTrackableIdAndTrackableTypeAndActive(trackableProperty.getId(),trackableProperty.getTrackableType(), Boolean.TRUE);
        TaskWorkflow taskWorkflow=taskWorkflows.get(0);
        taskWorkflow.setActive(Boolean.FALSE);
        taskWorkflow.setUpdatedAt(LocalDateTime.now());
        taskWorkflowRepo.save(taskWorkflow);
        
        return response;
    }

    @Override
    public void setTrackingData(TaskTrack track) {
        TrackableTypeEnum trackableType=track.getTrackable().getTrackableType();
        if(trackableType.equals(TrackableTypeEnum.LICENCE)){
        
            Optional<Licence> licenceExists=licenceRepo.findById(track.getTrackable().getId());
            if(!licenceExists.isPresent()){

                throw new DataNotFoundException("LICENCE NOT FOUND");
            }
            
            applicantProperty=licenceExists.get().getApplicant();
            feableProperty=licenceExists.get().getLicenseProduct();
            trackableProperty=licenceExists.get();
            licenceProperty=licenceExists.get();
            Long licenceeEntityId=licenceExists.get().getApplicantEntity().getReferingEntityId();
            
            licenceeEntityProperty=licenceeEntityRepo.findLicenceeEntityById(licenceeEntityId).get();//initialize licencee entity propery
            
        }
        
        if(trackableType.equals(TrackableTypeEnum.ENTITY_APPLICATION)){
        
            Optional<EntityApplication> entityApplicationExists=entityApplicationRepo.findById(track.getTrackable().getId());
            if(!entityApplicationExists.isPresent()){

                throw new DataNotFoundException("ENTITY APPLICATION NOT FOUND");
            }
            
            applicantProperty=entityApplicationExists.get().getApplicant();
            feableProperty=entityApplicationExists.get().getEntityProduct();
            trackableProperty=entityApplicationExists.get();
            entityApplicationProperty=entityApplicationExists.get();
            licenceeEntityProperty=entityApplicationExists.get().getApplicantEntity();
            
        }
    }

    @Override
    public Boolean intiateEntityTrack(EntityApplication application, Boolean isPayable, String workflowTypeCode) throws DataNotFoundException,Exception{
        Boolean response=Boolean.TRUE;
        Optional<ProductWorkflow> workflowExisting = productWorkflowRepo
					.findByProductableAndWorkflowTypeCode(application.getEntityProduct(), workflowTypeCode,Boolean.TRUE);

        if (!workflowExisting.isPresent()) {

            throw new DataNotFoundException("PRODUCT WORKFLOWS NOT FOUND");
        }

        ProductWorkflow productWorkflow=workflowExisting.get();

        //save entityProperty tracking            
        TaskTrack track=new TaskTrack();

        //get the first step in a workflow
        WorkflowStep step=workflowService.getNextWorkflowStep(productWorkflow.getWorkflow().getId(),0);
        List<Long> feeIds=new ArrayList();
        if(step.getStepType() == WorkflowStepTypeEnum.PAYMENT){

            if(isPayable){

                feeIds = feeStructureRepo
                                    .findFeeIdByFeeableAndApplicableStateAndActive(application.getEntityProduct(), step.getApplicableState(),true);

                if(feeIds.size() == 0){

                    throw new DataNotFoundException("FEES FOR THE LICENCE PRODUCT NOT FOUND");
                }
            }else{

                step=workflowService.getNextWorkflowStep(step.getWorkflow(),step.getStepNumber());
                //get step actor
                UserRole userRole=userService.getUserRole(step.getCurrentRole().getId());
                track.setActorRoleId(userRole.getRoleID());
                track.setActorId(userRole.getUserID());
            }

        }else if(step.getStepType() == WorkflowStepTypeEnum.DOCUMENT_GENERATION){

            DocumentDto document=new DocumentDto();
            documentService.generateDocument(document, step.getAttachmentType().getId());

        }else{

            //get step actor
            UserRole userRole=userService.getUserRole(step.getCurrentRole().getId());
            track.setActorRoleId(userRole.getRoleID());
            track.setActorId(userRole.getUserID());
        }

        track.setWorkflowId(productWorkflow.getWorkflow().getId());           
        track.setTrackable(application);
        track.setIsActed(Boolean.FALSE);
        track.setIsAssociated(Boolean.FALSE);
        track.setWorkflowStep(step);
        track.setTrackName(step.getName());

        track=trackRepo.saveAndFlush(track);
        
        //assign entityApplicationProperty to a workflow
        TaskWorkflow taskWorkflow = new TaskWorkflow();
        taskWorkflow.setTrackable(track.getTrackable());
        taskWorkflow.setWorkflowId(productWorkflow.getWorkflow().getId());
        taskWorkflow=taskWorkflowRepo.saveAndFlush(taskWorkflow);

        if(feeIds.size() > 0){

            BillingControlNumberRequestDto bill=new BillingControlNumberRequestDto();
            bill.setFeeIds(feeIds);
            bill.setLicenceId(application.getId());
            bill.setBillable(BillingAttachedToEnum.ENTITY_APPLICATION);

            billingService.initiateBill(bill);
        }
        
        return response;
    }

    @Override
    public Boolean saveApplicantionEntityShareholders(List<ShareholderDto> shareholders, Long applicationEntityId) throws Exception {
        Boolean response=Boolean.TRUE;
        // populate entity shareholders for the application if available
        for (ShareholderDto shareholder : shareholders) {

                LicenceApplicationShareholder sh = new LicenceApplicationShareholder();
                sh.setFullname(shareholder.getFullname());
                sh.setNationality(shareholder.getNationality());
                sh.setShares(shareholder.getShares());
                sh.setLicenceEntity(applicationEntityId);
                licenseApplicationEntityShareholderRepo.save(sh);
        }
        
        return response;
    }

    @Override
    public void checkDueDate(WorkflowStep step,Date dueDate) throws OperationNotAllowedException {
        //check to see if dueDate set id greater than the step configured days
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,step.getDueDays());
        String deadlineDate=sdf.format(c.getTime());
        
        String allocatedDueDate=sdf.format(dueDate);
        
        if(allocatedDueDate.compareTo(deadlineDate) > 0){
        
            throw new OperationNotAllowedException("DUE DATE CANNOT BE GREATER THAN "+deadlineDate);
        }
    }
}
