/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.payment.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import tz.go.tcra.lims.payment.entity.Billing;

/**
 *
 * @author emmanuel.mfikwa
 */
@Component
public class BillingServiceModelAssembler implements RepresentationModelAssembler<Billing, EntityModel<Billing>> {

	@Override
	public EntityModel<Billing> toModel(Billing entity) {
		return EntityModel.of(entity);
	}

	@Override
	public CollectionModel<EntityModel<Billing>> toCollectionModel(Iterable<? extends Billing> entities) {
		return StreamSupport.stream(entities.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}

}
