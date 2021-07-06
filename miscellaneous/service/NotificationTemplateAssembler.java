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
import tz.go.tcra.lims.miscellaneous.entity.NotificationTemplate;
import tz.go.tcra.lims.miscellaneous.controller.NotificationTemplateController;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class NotificationTemplateAssembler implements RepresentationModelAssembler<NotificationTemplate, EntityModel<NotificationTemplate>>{

    @Override
    public EntityModel<NotificationTemplate> toModel(NotificationTemplate entity) {
        return EntityModel.of(entity,
				linkTo(methodOn(NotificationTemplateController.class).findNotificationTemplateById(entity.getId())).withSelfRel(),
				linkTo(methodOn(NotificationTemplateController.class).getListOfAllNotificationTemplates(0, 15, "", "", "")).withRel("templates")
                );
    }

    @Override
    public CollectionModel<EntityModel<NotificationTemplate>> toCollectionModel(Iterable<? extends NotificationTemplate> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities); //To change body of generated methods, choose Tools | Templates.
    }
    
}
