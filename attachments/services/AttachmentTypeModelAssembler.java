package tz.go.tcra.lims.attachments.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import tz.go.tcra.lims.attachments.controller.AttachmentTypeController;
import tz.go.tcra.lims.entity.AttachmentType;

@Component
public class AttachmentTypeModelAssembler
		implements RepresentationModelAssembler<AttachmentType, EntityModel<AttachmentType>> {

	@SneakyThrows
	@Override
	public EntityModel<AttachmentType> toModel(AttachmentType attachmentType) {
		return EntityModel.of(attachmentType,
				linkTo(methodOn(AttachmentTypeController.class).findAttachmentTypeById(attachmentType.getId()))
						.withSelfRel());
	}

//    @Override
//    public CollectionModel<EntityModel<AttachmentType>> toCollectionModel(Iterable<? extends AttachmentType> attachmentTypes) {
//        return CollectionModel.of(null, linkTo(methodOn(AttachmentTypeController.class).getListOfAttachmentTypes()).withSelfRel());
//    }

	public CollectionModel<EntityModel<AttachmentType>> toCollectionModel(
			Iterable<? extends AttachmentType> attachmentTypes) {
		return StreamSupport.stream(attachmentTypes.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
