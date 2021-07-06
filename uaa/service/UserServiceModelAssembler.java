/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.uaa.controller.UserController;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class UserServiceModelAssembler implements RepresentationModelAssembler<LimsUser,EntityModel<LimsUser>>{

    @Override
    public EntityModel<LimsUser> toModel(LimsUser entity) {
        return EntityModel.of(entity,
                        linkTo(methodOn(UserController.class).details(entity.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).list("ALL",null)).withRel("users")
        );
    }

    @Override
    public CollectionModel<EntityModel<LimsUser>> toCollectionModel(Iterable<? extends LimsUser> entities) {
        return StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
    
}
