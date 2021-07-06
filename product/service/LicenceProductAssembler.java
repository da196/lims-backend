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
import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.product.controller.LicenseProductController;

/**
 * @author DonaldSj
 */

@Component
public class LicenceProductAssembler
		implements RepresentationModelAssembler<LicenceProduct, EntityModel<LicenceProduct>> {

	@SneakyThrows
	@Override
	public EntityModel<LicenceProduct> toModel(LicenceProduct licenseProduct) {
		return EntityModel.of(licenseProduct,
				linkTo(methodOn(LicenseProductController.class).findLicenseProductById(licenseProduct.getId()))
						.withSelfRel());
	}

	public CollectionModel<EntityModel<LicenceProduct>> toCollectionModel(
			Iterable<? extends LicenceProduct> licenseCategories) {
		return StreamSupport.stream(licenseCategories.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
