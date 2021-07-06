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
import tz.go.tcra.lims.uaa.controller.RoleController;
import tz.go.tcra.lims.uaa.entity.Role;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class RoleServiceModelAssembler implements RepresentationModelAssembler<Role,EntityModel<Role>>{

    @Override
    public EntityModel<Role> toModel(Role entity) {
        return EntityModel.of(entity,
                        linkTo(methodOn(RoleController.class).details(entity.getId())).withSelfRel(),
                        linkTo(methodOn(RoleController.class).list()).withRel("roles")
        );
    }

    @Override
    public CollectionModel<EntityModel<Role>> toCollectionModel(Iterable<? extends Role> entities) {
        return StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
    
}
