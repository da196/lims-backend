package tz.go.tcra.lims.geolocation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.entity.GeoLocation;
import tz.go.tcra.lims.geolocation.dto.GeoLocationDto;
import tz.go.tcra.lims.geolocation.service.GeoLocationService;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping("v1/geo-locations")
public class GeoLocationController {

	@Autowired
	private GeoLocationService geoLocationService;

	@PostMapping(value = "/save")
	@PreAuthorize("hasRole('ROLE_GEOLOCATION_SAVE')")
	public Response<EntityModel<GeoLocation>> saveGeoLocation(@RequestBody GeoLocationDto geoLocationDto) {

		return geoLocationService.saveGeoLocation(geoLocationDto);

	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_GEOLOCATION_EDIT')")
	public Response<EntityModel<GeoLocation>> updateGeoLocation(@RequestBody GeoLocationDto geoLocationDto,
			@PathVariable("id") Long id) {

		return geoLocationService.updateGeoLocation(geoLocationDto, id);

	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_GEOLOCATION_VIEW')")
	public Response<EntityModel<GeoLocation>> findGeoLocationById(@PathVariable("id") Long id) {
		return geoLocationService.findGeoLocationById(id);

	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_GEOLOCATION_DELETE')")
	public Response<EntityModel<GeoLocation>> deleteGeoLocationById(Long id) {
		return geoLocationService.deleteGeoLocationById(id);

	}

	@GetMapping(value = "/list")
	@PreAuthorize("hasRole('ROLE_GEOLOCATION_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<GeoLocation>>> getListOfGeoLocation(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return geoLocationService.getListOfGeoLocation(keyword, pageable);

	}

	@GetMapping(value = "/getChildLocation")
	@PreAuthorize("hasRole('ROLE_GEOLOCATION_VIEW')")
	public Response<CollectionModel<EntityModel<GeoLocation>>> getListOfGeoLocationByParent(
			@RequestBody GeoLocation parent) {

		return geoLocationService.getListOfGeoLocationByParent(parent);

	}

}
