/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import tz.go.tcra.lims.workflow.controller.WorkflowController;
import tz.go.tcra.lims.workflow.entity.Workflow;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class WorkflowServiceModelAssembler implements RepresentationModelAssembler<Workflow, EntityModel<Workflow>> {

	@Override
	public EntityModel<Workflow> toModel(Workflow entity) {
		return EntityModel.of(entity,
				linkTo(methodOn(WorkflowController.class).findWorkflowById(entity.getId())).withSelfRel()

		);
	}

	@Override
	public CollectionModel<EntityModel<Workflow>> toCollectionModel(Iterable<? extends Workflow> entities) {
		return StreamSupport.stream(entities.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}

}
