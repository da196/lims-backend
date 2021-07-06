package tz.go.tcra.lims.geolocation.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import tz.go.tcra.lims.entity.GeoLocation;
import tz.go.tcra.lims.geolocation.dto.GeoLocationDto;
import tz.go.tcra.lims.geolocation.dto.GeoLocationMinDto;
import tz.go.tcra.lims.utils.Response;

@Service
public interface GeoLocationService {

	Response<EntityModel<GeoLocation>> saveGeoLocation(GeoLocationDto geoLocationDto);

	Response<EntityModel<GeoLocation>> updateGeoLocation(GeoLocationDto geoLocationDto, Long id);

	Response<EntityModel<GeoLocation>> findGeoLocationById(Long id);

	Response<EntityModel<GeoLocation>> deleteGeoLocationById(Long id);

	Response<CollectionModel<EntityModel<GeoLocation>>> getListOfGeoLocation(String keyword, Pageable pageable);

	Response<CollectionModel<EntityModel<GeoLocation>>> getListOfGeoLocationByParent(GeoLocation parent);

	GeoLocationMinDto composeGeoLocationMinDto(GeoLocation data);
}
