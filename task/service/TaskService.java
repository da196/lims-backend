/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.attachments.dto.AttachmentTypeMinDto;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.task.entity.LicencePresentation;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.dto.ActivityAttachmentDto;
import tz.go.tcra.lims.task.dto.ActorMinDto;
import tz.go.tcra.lims.task.dto.TaskActionDto;
import tz.go.tcra.lims.task.dto.TaskActivityDto;
import tz.go.tcra.lims.task.dto.FormFindingDto;
import tz.go.tcra.lims.task.dto.FormFindingMaxDto;
import tz.go.tcra.lims.task.dto.TrackDto;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.task.dto.TaskDetailDto;
import tz.go.tcra.lims.miscellaneous.dto.DocumentDto;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;
import tz.go.tcra.lims.task.dto.TaskMinDto;
import tz.go.tcra.lims.task.entity.Trackable;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.OperationNotAllowedException;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;
import tz.go.tcra.lims.workflow.entity.WorkflowStepDecision;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface TaskService {
    
    Response<CollectionModel<EntityModel<TaskMinDto>>> getAllTasks(int page, int size, String sortName,String sortType);
    Response<CollectionModel<EntityModel<TaskMinDto>>> getMyTasks(int page, int size, String sortName,String sortType);
    List<ActorMinDto> getNextActorsByCurrentWorkflowAndStepNumber(Long workflow,Integer stepNumber);
    List<TaskActivityDto> getTaskActivities(Trackable trackable);
    List<TrackDto> getTaskTracks(Trackable trackable);
    Response<TaskActivity> saveActivity(TaskActionDto data);
    Boolean intiateLicenceTrack(Licence licence,Boolean isPayable,String licenceWorkflowTypeCode) throws DataNotFoundException,Exception;
    FormFindingMaxDto getLicenseFormFindings(Long activityId);
    Long saveFormFindings(List<FormFindingDto> findings,Long activityId) throws DataNotFoundException,Exception;
    Boolean advanceTaskTracker(TaskActionDto action,TaskTrack previousTrack) throws OperationNotAllowedException,DataNotFoundException,Exception;
    Boolean returnTaskTracker(TaskActionDto action,TaskTrack previousTrack) throws DataNotFoundException,Exception;
    Boolean rejectTaskTracker(TaskTrack previousTrack) throws DataNotFoundException,Exception;
    Boolean resubmitTracker(TaskTrack previousTrack,String comments) throws DataNotFoundException,Exception;
    void composeNotification(TaskTrack track,WorkflowStepDecision decision,Boolean isApplicant);
    Response<TaskDetailDto> viewTask(Long trackId);
    void receivePaymentNotification(Long trackableId,String trackableType);
    Response<TaskActivity> saveActivityAttachment(ActivityAttachmentDto data);
    DocumentDto composeDocumentDto(TaskTrack track,Long attachmentType) throws DataNotFoundException,Exception;
    TaskActivityDto getTaskActivity(Long activityId);
    TaskActivityDto composeTaskActivity(TaskActivity activity);
    AttachmentTypeMinDto composeAttachmentTypeMinDto(AttachmentType attachmentType);
    LicencePresentation savePresentation(LocalDateTime date,String remark,WorkflowDecisionEnum decision,TaskTrack track) throws Exception;
    Boolean closeTaskTrack(TaskTrack track) throws Exception;
    void setTrackingData(TaskTrack track);
    Boolean intiateEntityTrack(EntityApplication application,Boolean isPayable,String workflowTypeCode) throws DataNotFoundException,Exception;
    Boolean saveApplicantionEntityShareholders(List<ShareholderDto> shareholders,Long applicationEntityId) throws Exception;
    void checkDueDate(WorkflowStep step,Date dueDate) throws OperationNotAllowedException;
}
