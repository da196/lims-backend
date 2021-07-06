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
import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.licence.controller.LicenseCategoryController;

/**
 * @author DonaldSj
 */

@Component
public class LicenseCategoryAssembler
		implements RepresentationModelAssembler<LicenceCategory, EntityModel<LicenceCategory>> {

	@SneakyThrows
	@Override
	public EntityModel<LicenceCategory> toModel(LicenceCategory licenseCategory) {
		return EntityModel.of(licenseCategory,
                        linkTo(methodOn(LicenseCategoryController.class).findLicenseCategoryById(licenseCategory.getId()))
                                        .withSelfRel(),
                        linkTo(methodOn(LicenseCategoryController.class).getListOfLicenseCategories(licenseCategory.getId(), ""))
                                        .withRel("childCategories"),
                        linkTo(methodOn(LicenseCategoryController.class).getListOfLicenseCategory())
						.withRel("licenseCategory"));
	}

	public CollectionModel<EntityModel<LicenceCategory>> toCollectionModel(
			Iterable<? extends LicenceCategory> licenseCategories) {
		return StreamSupport.stream(licenseCategories.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
