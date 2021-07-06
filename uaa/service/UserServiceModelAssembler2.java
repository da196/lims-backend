/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.uaa.controller.UserController;
import tz.go.tcra.lims.uaa.dto.UserMaxDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class UserServiceModelAssembler2 implements RepresentationModelAssembler<UserMaxDto,EntityModel<UserMaxDto>>{

    @Override
    public EntityModel<UserMaxDto> toModel(UserMaxDto entity) {
        return EntityModel.of(entity,
                        linkTo(methodOn(UserController.class).details(entity.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).list("ALL",null)).withRel("users")
        );
    }
    
}
