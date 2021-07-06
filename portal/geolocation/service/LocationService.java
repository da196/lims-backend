package tz.go.tcra.lims.portal.geolocation.service;

import java.util.List;

import tz.go.tcra.lims.geolocation.dto.GeoLocationType;
import tz.go.tcra.lims.portal.geolocation.dto.LocationDto;
import tz.go.tcra.lims.utils.Response;

public interface LocationService {

    Response<LocationDto> findGeoLocationById(Long id);

    Response<List<LocationDto>> getGeoLocationByParent(Long parentId);

    Response<List<LocationDto>> findGeoLocationByType(GeoLocationType type);
    
    LocationDto getGeoLocationById(Long id);
}
