package tz.go.tcra.lims.feestructure.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.feestructure.controller.FeeStructureController;

@Component
public class FeeStructureAssembler
		implements RepresentationModelAssembler<FeeStructure, EntityModel<FeeStructure>> {

	@SneakyThrows
	@Override
	public EntityModel<FeeStructure> toModel(FeeStructure licenseFeeStructure) {
		return EntityModel.of(licenseFeeStructure,
				linkTo(methodOn(FeeStructureController.class)
						.findFeeStructureById(licenseFeeStructure.getId())).withSelfRel(),
				linkTo(methodOn(FeeStructureController.class).getListOfAllFeeStructures("",null))
						.withRel("licenseFeeStructure"));
	}

	public CollectionModel<EntityModel<FeeStructure>> toCollectionModel(
			Iterable<? extends FeeStructure> licenseFeeStructures) {

		return StreamSupport.stream(licenseFeeStructures.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
