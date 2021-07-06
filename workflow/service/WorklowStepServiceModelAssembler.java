/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.service;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;

/**
 *
 * @author emmanuel.mfikwa
 */
public class WorklowStepServiceModelAssembler implements RepresentationModelAssembler<WorkflowStep,EntityModel<WorkflowStep>>{

    @Override
    public EntityModel<WorkflowStep> toModel(WorkflowStep entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CollectionModel<EntityModel<WorkflowStep>> toCollectionModel(Iterable<? extends WorkflowStep> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities); //To change body of generated methods, choose Tools | Templates.
    }
    
}
