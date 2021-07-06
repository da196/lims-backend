package tz.go.tcra.lims.portal.geolocation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.geolocation.dto.GeoLocationType;
import tz.go.tcra.lims.portal.geolocation.dto.LocationDto;
import tz.go.tcra.lims.portal.geolocation.service.LocationService;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping("v1/p/locations")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping(value = "/findGeoLocationById/{locationId}")
	public Response<LocationDto> findGeoLocationById(@PathVariable("locationId") Long id) {
		return locationService.findGeoLocationById(id);
	}

	@GetMapping(value = "/getGeoLocationByParent/{parentId}")
	public Response<List<LocationDto>> getGeoLocationByParent(@PathVariable("parentId") Long parentId) {
		return locationService.getGeoLocationByParent(parentId);
	}

	@GetMapping(value = "/findGeoLocationByType/{type}")
	public Response<List<LocationDto>> findGeoLocationByType(@PathVariable("type") GeoLocationType type) {

		return locationService.findGeoLocationByType(type);

	}

}
