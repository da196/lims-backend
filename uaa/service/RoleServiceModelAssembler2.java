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
import tz.go.tcra.lims.uaa.controller.RoleController;
import tz.go.tcra.lims.uaa.dto.RoleMaxDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class RoleServiceModelAssembler2 implements RepresentationModelAssembler<RoleMaxDto,EntityModel<RoleMaxDto>>{

    @Override
    public EntityModel<RoleMaxDto> toModel(RoleMaxDto entity) {
        return EntityModel.of(entity,
                        linkTo(methodOn(RoleController.class).details(entity.getId())).withSelfRel(),
                        linkTo(methodOn(RoleController.class).list()).withRel("roles")
        );
    }
    
}
