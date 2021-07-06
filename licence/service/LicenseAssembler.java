package tz.go.tcra.lims.licence.service;

import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.entity.Licence;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author DonaldSj
 */

@Component
public class LicenseAssembler implements RepresentationModelAssembler<Licence, EntityModel<Licence>> {

	@SneakyThrows
	@Override
	public EntityModel<Licence> toModel(Licence license) {
		return EntityModel.of(license
//				linkTo(methodOn(LicenseController.class).findLicenseById(license.getAttachableId())).withSelfRel()
//				linkTo(methodOn(LicenseController.class).getListOfAllLicenses()).withRel("licenseCategory")
                                );
	}

	public CollectionModel<EntityModel<Licence>> toCollectionModel(Iterable<? extends Licence> licenses) {
		return StreamSupport.stream(licenses.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
