package tz.go.tcra.lims.task.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.task.dto.ActivityAttachmentDto;
import tz.go.tcra.lims.task.dto.TaskActionDto;
import tz.go.tcra.lims.task.dto.TaskDetailDto;
import tz.go.tcra.lims.task.dto.TaskMinDto;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.repository.TaskTrackRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.task.service.TaskService;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.workflow.repository.WorkflowStepDecisionRepository;


/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService service;
    
    @Autowired
    private AppUtility appUtility;
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_TASK_VIEW_ALL')")
    public Response<CollectionModel<EntityModel<TaskMinDto>>> getAllTasks(
                    @RequestParam(name = "page", defaultValue = "0") int page,
                    @RequestParam(name = "size", defaultValue = "15") int size,
                    @RequestParam(name = "sort_name", defaultValue = "id") String sortName,
                    @RequestParam(name = "sort_type", defaultValue = "DESC") String sortType) {

            return service.getAllTasks(page, size, sortName, sortType);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_TASK_VIEW_MY')")
    public Response<CollectionModel<EntityModel<TaskMinDto>>> getMyTask(
                    @RequestParam(name = "page", defaultValue = "0") int page,
                    @RequestParam(name = "size", defaultValue = "15") int size,
                    @RequestParam(name = "sort_name", defaultValue = "id") String sortName,
                    @RequestParam(name = "sort_type", defaultValue = "DESC") String sortType) {

            return service.getMyTasks(page, size, sortName, sortType);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasRole('ROLE_TASK_VIEW_DETAILS')")
    public Response<TaskDetailDto> getTaskDetails(@Min(1) @PathVariable("id") Long id) {

            return service.viewTask(id);
    }

	@PostMapping("/save-activity")
    @PreAuthorize("hasRole('ROLE_TASK_SAVE_ACTIVITY')")
    public Response<TaskActivity> saveActivity(@Valid @RequestBody TaskActionDto data){

        return service.saveActivity(data);
    }
    
    @PostMapping("/save-activity-attachment")
    @PreAuthorize("hasRole('ROLE_TASK_SAVE_ACTIVITY')")
    public Response<TaskActivity> saveActivityAttachment(@Valid @ModelAttribute ActivityAttachmentDto data){

        return service.saveActivityAttachment(data);
    }
    
    @GetMapping("/simulate-certificate-generation")
    public void simulateCertificate(){
        
        appUtility.testCertificate();
    }
}
