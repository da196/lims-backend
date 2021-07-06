package tz.go.tcra.lims.workflow.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.workflow.entity.WorkflowType;
import tz.go.tcra.lims.workflow.service.WorkflowTypeService;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "v1/workflow-types")
public class WorkflowTypeController {

    @Autowired
    private WorkflowTypeService service;
    
    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_WORKFLOW_TYPE_VIEW_ALL')")
    public Response<List<WorkflowType>> getListWorkflowType() {

        return service.getAllWorkflowTypes();
    }

    @GetMapping("/list-active")
    @PreAuthorize("hasRole('ROLE_WORKFLOW_TYPE_VIEW_ALL')")
    public Response<List<WorkflowType>> getListOfActiveWorkflowType() {

        return service.getWorkflowTypesByActive(true);
    }
}