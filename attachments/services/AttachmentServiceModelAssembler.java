/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.attachments.services;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.attachments.controller.AttachmentController;
import tz.go.tcra.lims.entity.Attachment;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class AttachmentServiceModelAssembler implements RepresentationModelAssembler<Attachment,EntityModel<Attachment>>{

    @Override
    public EntityModel<Attachment> toModel(Attachment entity) {
        
        return EntityModel.of(entity,
//                        linkTo(methodOn(AttachmentController.class).findAttachmentById(entity.getId())).withSelfRel(),
                        linkTo(methodOn(AttachmentController.class).getListOfAttachments(0,15,"id","DESC")).withRel("attachments")
        );
    }

    @Override
    public CollectionModel<EntityModel<Attachment>> toCollectionModel(Iterable<? extends Attachment> entities) {
        return StreamSupport
                .stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
    
//    private EntityModel<Attachment> buildEnzymeModel(Attachment enzyme) {
//        return EnzymeModel.builder()
//                .ecNumber(enzyme.getEc())
//                .enzymeName(enzyme.getEnzymeName())
//                .enzymeFamily(enzyme.getEnzymeFamily())
//                .alternativeNames(enzyme.getAltNames())
//                .catalyticActivities(enzyme.getCatalyticActivities())
//                .cofactors(enzyme.getIntenzCofactors())
//                .associatedProteins(ProteinUtil.toCollectionModel(enzyme.getProteinGroupEntry()))
//                .build();
//    }
}
