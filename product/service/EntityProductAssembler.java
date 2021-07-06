package tz.go.tcra.lims.product.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import tz.go.tcra.lims.product.entity.EntityProduct;
import tz.go.tcra.lims.product.controller.EntityProductController;

/**
 * @author DonaldSj
 */

@Component
public class EntityProductAssembler
		implements RepresentationModelAssembler<EntityProduct, EntityModel<EntityProduct>> {

	@SneakyThrows
	@Override
	public EntityModel<EntityProduct> toModel(EntityProduct entity) {
		return EntityModel.of(entity,
				linkTo(methodOn(EntityProductController.class).findEntityProductById(entity.getId()))
						.withSelfRel());
	}

	public CollectionModel<EntityModel<EntityProduct>> toCollectionModel(
			Iterable<? extends EntityProduct> entity) {
		return StreamSupport.stream(entity.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
