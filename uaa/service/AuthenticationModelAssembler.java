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
import tz.go.tcra.lims.uaa.dto.AuthResponseDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class AuthenticationModelAssembler implements RepresentationModelAssembler<AuthResponseDto,EntityModel<AuthResponseDto>>{

    @Override
    public EntityModel<AuthResponseDto> toModel(AuthResponseDto entity) {
        
        return EntityModel.of(entity,
        linkTo(methodOn(UserController.class).details(entity.getId())).withSelfRel());
    }
    
}
