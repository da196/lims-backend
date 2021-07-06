package tz.go.tcra.lims.miscellaneous.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.controller.ListOfValueController;

@Component
public class ListOfValueModelAssembler implements RepresentationModelAssembler<ListOfValue, EntityModel<ListOfValue>> {

	@SneakyThrows
	@Override
	public EntityModel<ListOfValue> toModel(ListOfValue listOfValue) {
		return EntityModel.of(listOfValue,
				linkTo(methodOn(ListOfValueController.class).findListOfValueById(listOfValue.getId())).withSelfRel(),
				linkTo(methodOn(ListOfValueController.class).getListOfListOfValues()).withRel("listOfValue"));
	}

//    @Override
//    public CollectionModel<EntityModel<AttachmentType>> toCollectionModel(Iterable<? extends AttachmentType> attachmentTypes) {
//        return CollectionModel.of(null, linkTo(methodOn(AttachmentTypeController.class).getListOfAttachmentTypes()).withSelfRel());
//    }

	public CollectionModel<EntityModel<ListOfValue>> toCollectionModel(Iterable<? extends ListOfValue> listOfValues) {
		return StreamSupport.stream(listOfValues.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
