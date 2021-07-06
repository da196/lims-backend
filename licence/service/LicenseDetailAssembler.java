package tz.go.tcra.lims.licence.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import tz.go.tcra.lims.entity.LicenceServiceDetail;
import tz.go.tcra.lims.licence.controller.LicenseServiceDetailController;

/**
 * @author DonaldSj
 */

@Component
public class LicenseDetailAssembler
		implements RepresentationModelAssembler<LicenceServiceDetail, EntityModel<LicenceServiceDetail>> {

	@SneakyThrows
	@Override
	public EntityModel<LicenceServiceDetail> toModel(LicenceServiceDetail licenseDetail) {
		return EntityModel.of(licenseDetail,
				linkTo(methodOn(LicenseServiceDetailController.class).findLicenseDetailById(licenseDetail.getId()))
						.withSelfRel());
	}

	public CollectionModel<EntityModel<LicenceServiceDetail>> toCollectionModel(
			Iterable<? extends LicenceServiceDetail> licenseDetails) {
		return StreamSupport.stream(licenseDetails.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
