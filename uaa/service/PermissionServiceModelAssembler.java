/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.uaa.controller.PermissionController;
import tz.go.tcra.lims.uaa.entity.Permission;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class PermissionServiceModelAssembler implements RepresentationModelAssembler<Permission,EntityModel<Permission>>{

    @Override
    public EntityModel<Permission> toModel(Permission entity) {
        
        return EntityModel.of(entity,
                        linkTo(methodOn(PermissionController.class).details(entity.getId())).withSelfRel(),
                        linkTo(methodOn(PermissionController.class).list(0,15,"id","DESC")).withRel("permissions")
        );
    }

    @Override
    public CollectionModel<EntityModel<Permission>> toCollectionModel(Iterable<? extends Permission> entities) {
        
        return StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
    
}
