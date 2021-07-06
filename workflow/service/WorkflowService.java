/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.workflow.dto.WorkflowDto;
import tz.go.tcra.lims.workflow.dto.WorkflowMinDto;
import tz.go.tcra.lims.workflow.entity.Workflow;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface WorkflowService {

	Response<CollectionModel<EntityModel<Workflow>>> getAllWorkflows(String keyword, Pageable pageable);

	Response<CollectionModel<EntityModel<Workflow>>> getWorkflowsByType(Long typeId, int page, int size,
			String sortName, String sortType);

	Response<EntityModel<Workflow>> saveWorkflow(WorkflowDto data, Long id);

	Response<EntityModel<Workflow>> activateDeactivateWorkflow(Long id);

	Response<EntityModel<Workflow>> getWorkflowById(Long id);

	Response<CollectionModel<EntityModel<Workflow>>> getWorkflowsByActive(String keyword, Pageable pageable);

	WorkflowMinDto composeWorkflowMinDto(Workflow data) throws Exception;

	WorkflowStep getNextWorkflowStep(Long workflowId, Integer stepNumber) throws Exception, DataNotFoundException;

	WorkflowStep getPreviousWorkflowStep(Long workflowId, Integer stepNumber) throws Exception, DataNotFoundException;
        
        Workflow getWorkflow(Long id);
        
        Response<WorkflowStep> activateDeactivateWorkflowStep(Long workflowStepId);
}
