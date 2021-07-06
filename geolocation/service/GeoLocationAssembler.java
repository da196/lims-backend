package tz.go.tcra.lims.geolocation.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import tz.go.tcra.lims.entity.GeoLocation;
import tz.go.tcra.lims.geolocation.controller.GeoLocationController;

@Component
public class GeoLocationAssembler implements RepresentationModelAssembler<GeoLocation, EntityModel<GeoLocation>> {

	@SneakyThrows
	@Override
	public EntityModel<GeoLocation> toModel(GeoLocation geoLocation) {
		return EntityModel.of(geoLocation,
				linkTo(methodOn(GeoLocationController.class).findGeoLocationById(geoLocation.getId())).withSelfRel());
	}

	public CollectionModel<EntityModel<GeoLocation>> toCollectionModel(Iterable<? extends GeoLocation> geoLocations) {

		return StreamSupport.stream(geoLocations.spliterator(), false).map(this::toModel)
				.collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
	}
}
