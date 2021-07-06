package tz.go.tcra.lims.geolocation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.entity.GeoLocation;
import tz.go.tcra.lims.geolocation.dto.GeoLocationDto;
import tz.go.tcra.lims.geolocation.dto.GeoLocationMinDto;
import tz.go.tcra.lims.geolocation.repository.GeoLocationRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

@Slf4j
@Service
public class GeoLocationServiceImpl implements GeoLocationService {

	@Autowired
	private GeoLocationRepository geoLocationRepository;

	@Autowired
	private GeoLocationAssembler geoLocationAssembler;

	@Autowired
	private PagedResourcesAssembler<GeoLocation> pagedResourcesAssembler;

	@Override
	public Response<EntityModel<GeoLocation>> saveGeoLocation(GeoLocationDto geoLocationDto) {
		try {
			if (geoLocationRepository.existsByNameOrCodeAndActive(geoLocationDto.getName(), geoLocationDto.getCode(),
					true)) {
				return new Response<>(ResponseCode.DUPLICATE, false, "There is duplicate entry", null);

			} else {

				GeoLocation geoLocation = new GeoLocation();

				geoLocation.setActive(true);
				geoLocation.setApproved(true);
				geoLocation.setCode(geoLocationDto.getCode());
				geoLocation.setCreatedAt(LocalDateTime.now());
				geoLocation.setName(geoLocationDto.getName());
				if (geoLocationRepository.existsById(geoLocationDto.getParentId())) {
					geoLocation.setParent(geoLocationRepository.getOne(geoLocationDto.getParentId()));
				}
				geoLocation.setUniqueID(UUID.randomUUID());
				geoLocation.setType(geoLocationDto.getType());

				EntityModel<GeoLocation> entityModel = new EntityModel<>(
						geoLocationRepository.saveAndFlush(geoLocation));
				return new Response<>(ResponseCode.SUCCESS, true, "Data saved Successfully", entityModel);
			}
		} catch (Exception e) {
			log.error("Could not save Geolocation because: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!", null);
		}
	}

	@Override
	public Response<EntityModel<GeoLocation>> updateGeoLocation(GeoLocationDto geoLocationDto, Long id) {

		try {
			if (geoLocationRepository.existsByIdAndActive(id, true)) {

				GeoLocation geoLocation = geoLocationRepository.findByIdAndActive(id, true);
				geoLocation.setCode(geoLocationDto.getCode());
				geoLocation.setName(geoLocationDto.getName());
				if (geoLocationRepository.existsById(geoLocationDto.getParentId())) {
					geoLocation.setParent(geoLocationRepository.getOne(geoLocationDto.getParentId()));
				}
				geoLocation.setType(geoLocationDto.getType());
				geoLocation.setUpdatedAt(LocalDateTime.now());

				EntityModel<GeoLocation> entityModel = new EntityModel<>(
						geoLocationRepository.saveAndFlush(geoLocation));
				return new Response<>(ResponseCode.SUCCESS, true, "Data updated Successfully", entityModel);
			} else {
				return new Response<>(ResponseCode.NO_RECORD_FOUND, false, "There is no entry Found", null);
			}
		} catch (Exception e) {
			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!",
					geoLocationAssembler.toModel(null));
		}
	}

	@Override
	public Response<EntityModel<GeoLocation>> findGeoLocationById(Long id) {

		try {
			if (geoLocationRepository.existsByIdAndActive(id, true)) {
				GeoLocation geoLocation = geoLocationRepository.findByIdAndActive(id, true);

				return new Response<>(ResponseCode.SUCCESS, true, "Successful..",
						geoLocationAssembler.toModel(geoLocation));
			} else {
				return new Response<>(ResponseCode.NO_RECORD_FOUND, false, "Requested data could not be found!", null);

			}
		} catch (Exception e) {
			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!",
					geoLocationAssembler.toModel(null));
		}
	}

	@Override
	public Response<EntityModel<GeoLocation>> deleteGeoLocationById(Long id) {
		try {
			if (geoLocationRepository.existsByIdAndActive(id, true)) {
				GeoLocation geoLocation = geoLocationRepository.findByIdAndActive(id, true);

				if (geoLocation.getGeoLocations().isEmpty() == true) {

					geoLocation.setActive(false);
					geoLocation.setUpdatedAt(LocalDateTime.now());

					EntityModel<GeoLocation> entityModel = new EntityModel<>(
							geoLocationRepository.saveAndFlush(geoLocation));
					return new Response<>(ResponseCode.SUCCESS, true, "Data deleted Successfully", entityModel);
				} else {
					return new Response<>(ResponseCode.UNAUTHORIZED, false, "Requested data is Parent to other data!",
							null);
				}

			} else {
				return new Response<>(ResponseCode.NO_RECORD_FOUND, false, "Requested data could not be found!", null);
			}
		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!", null);
		}
	}

	@Override
	public Response<CollectionModel<EntityModel<GeoLocation>>> getListOfGeoLocation(String keyword, Pageable pageable) {
		try {

			Page<GeoLocation> geoLocationlist = null;
			if (keyword.equalsIgnoreCase("All")) {
				geoLocationlist = geoLocationRepository.findByActive(true, pageable);
			} else {

				geoLocationlist = geoLocationRepository.findByKeywordActive(keyword, true, pageable);
			}

			if (geoLocationlist == null) {

				return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
						pagedResourcesAssembler.toModel(geoLocationlist));
			}
			return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",

					pagedResourcesAssembler.toModel(geoLocationlist));

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURED: " + e.getMessage());
			e.printStackTrace();

			return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED", null);
		}
	}

//	private Response<CollectionModel<EntityModel<GeoLocation>>> getCollectionModelResponse(List<GeoLocation> geoLocationList) {
//		List<EntityModel<GeoLocation>> geoLocations = geoLocationList.stream() //
//				.map(geoLocationAssembler::toModel) //
//				.collect(Collectors.toList());
//
//		CollectionModel<EntityModel<GeoLocation>> iGeoLocations = geoLocationAssembler
//				.toCollectionModel(geoLocations);
//
//		return new Response<>(ResponseCode.SUCCESS, true, "Data updated Successfully", iGeoLocations);
//	}

	@Override
	public Response<CollectionModel<EntityModel<GeoLocation>>> getListOfGeoLocationByParent(GeoLocation parent) {
		try {

			List<GeoLocation> geoLocationlist = geoLocationRepository.findByParent(parent);

			if (geoLocationlist.size() == 0) {

				return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
						geoLocationAssembler.toCollectionModel(geoLocationlist));
			}

			return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
					geoLocationAssembler.toCollectionModel(geoLocationlist));

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURED: " + e.getMessage());
			e.printStackTrace();

			return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED", null);
		}
	}

	@Override
	public GeoLocationMinDto composeGeoLocationMinDto(GeoLocation data) {
		GeoLocationMinDto response = new GeoLocationMinDto();
		try {

			response.setId(data.getId());
			response.setCode(data.getCode());
			response.setName(data.getName());

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return response;
	}

}
