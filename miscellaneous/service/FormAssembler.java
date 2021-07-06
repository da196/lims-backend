/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.miscellaneous.entity.Form;
import tz.go.tcra.lims.miscellaneous.controller.FormController;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class FormAssembler implements RepresentationModelAssembler<Form, EntityModel<Form>>{

    @SneakyThrows
    @Override
    public EntityModel<Form> toModel(Form entity) {
        return EntityModel.of(entity,
				linkTo(methodOn(FormController.class).findFormById(entity.getId())).withSelfRel(),
				linkTo(methodOn(FormController.class).getListOfAllForms())
						.withRel("forms"));
    }

    @Override
    public CollectionModel<EntityModel<Form>> toCollectionModel(Iterable<? extends Form> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
    
}
