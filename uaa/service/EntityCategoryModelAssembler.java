/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import tz.go.tcra.lims.licencee.controller.EntityCategoryController;
import tz.go.tcra.lims.licencee.entity.EntityCategory;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class EntityCategoryModelAssembler
		implements RepresentationModelAssembler<EntityCategory, EntityModel<EntityCategory>> {

	@SneakyThrows
	@Override
	public EntityModel<EntityCategory> toModel(EntityCategory entity) {

		return EntityModel.of(entity,
				linkTo(methodOn(EntityCategoryController.class).findById(entity.getId())).withSelfRel(),
				linkTo(methodOn(EntityCategoryController.class).getListOfEntityCategories())
						.withRel("entityCategories"));
	}

	@Override
	public CollectionModel<EntityModel<EntityCategory>> toCollectionModel(Iterable<? extends EntityCategory> entities) {

		return StreamSupport.stream(entities.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));

	}

}
