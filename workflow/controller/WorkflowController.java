package tz.go.tcra.lims.workflow.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.workflow.dto.WorkflowDto;
import tz.go.tcra.lims.workflow.entity.Workflow;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;
import tz.go.tcra.lims.workflow.service.WorkflowService;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "v1/workflows")
public class WorkflowController {

	@Autowired
	private WorkflowService service;

	@PostMapping("/save")
	@PreAuthorize("hasRole('ROLE_WORKFLOW_SAVE')")
	public Response<EntityModel<Workflow>> saveWorkflow(@Valid @RequestBody WorkflowDto data) {

		return service.saveWorkflow(data, 0L);
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ROLE_WORKFLOW_EDIT')")
	public Response<EntityModel<Workflow>> updateWorkflow(@Valid @RequestBody WorkflowDto data,
			@PathVariable("id") Long id) {

		return service.saveWorkflow(data, id);
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_WORKFLOW_VIEW')")
	public Response<EntityModel<Workflow>> findWorkflowById(@PathVariable("id") Long id) {

		return service.getWorkflowById(id);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_WORKFLOW_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<Workflow>>> getLisWorkflow(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return service.getAllWorkflows(keyword, pageable);
	}

	@PutMapping("/de-activate/{id}")
	@PreAuthorize("hasRole('ROLE_WORKFLOW_ACTIVATE_DEACTIVATE')")
	public Response<EntityModel<Workflow>> activateDeactivateWorkflowById(@PathVariable("id") Long id) {

		return service.activateDeactivateWorkflow(id);
	}
        
	@PutMapping("/step-de-activate/{id}")
	@PreAuthorize("hasRole('ROLE_WORKFLOW_ACTIVATE_DEACTIVATE')")
	public Response<WorkflowStep> activateDeactivateWorkflowStep(@PathVariable("id") Long id) {

		return service.activateDeactivateWorkflowStep(id);
	}

	@GetMapping("/list-active")
	@PreAuthorize("hasRole('ROLE_WORKFLOW_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<Workflow>>> getListActiveWorkflows(

			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return service.getWorkflowsByActive(keyword, pageable);
	}
}