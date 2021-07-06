/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.miscellaneous.controller.NotificationTemplateController;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class StatusAssembler implements RepresentationModelAssembler<Status, EntityModel<Status>>{

    @Override
    public EntityModel<Status> toModel(Status entity) {
        return EntityModel.of(entity,
				linkTo(methodOn(NotificationTemplateController.class).findNotificationTemplateById(entity.getId())).withSelfRel(),
				linkTo(methodOn(NotificationTemplateController.class).getListOfAllNotificationTemplates(0, 15, "", "", "")).withRel("status")
                );
    }

    @Override
    public CollectionModel<EntityModel<Status>> toCollectionModel(Iterable<? extends Status> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities); //To change body of generated methods, choose Tools | Templates.
    }
    
}
