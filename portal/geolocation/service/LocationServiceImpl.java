package tz.go.tcra.lims.portal.geolocation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.entity.GeoLocation;
import tz.go.tcra.lims.geolocation.dto.GeoLocationType;
import tz.go.tcra.lims.portal.geolocation.dto.LocationDto;
import tz.go.tcra.lims.portal.geolocation.repository.LocationRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.GeneralException;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Response<LocationDto> findGeoLocationById(Long id) {
        Response<LocationDto> response=new Response(ResponseCode.SUCCESS,true,"GEO LOCATION DETAILS RETRIEVED SUCCESSFULLY",null);
        try{        
            Optional<GeoLocation> geoLocation = locationRepository.findById(id);
            
            if (!geoLocation.isPresent()) {

                response.setMessage("GEO LOCATION DETAILS NOT FOUND");
                return response;
            }
            
            LocationDto location = new LocationDto();
            location.setId(geoLocation.get().getId());
            location.setCode(geoLocation.get().getCode());
            location.setName(geoLocation.get().getName());
            location.setLocationType(geoLocation.get().getType().toString());
            
            response.setData(location);
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        return response;
    }

    @Override
    public Response<List<LocationDto>> getGeoLocationByParent(Long parentId) {
        List<LocationDto> locations = new ArrayList<>();
        Optional<GeoLocation> geoLocation = locationRepository.findById(parentId);
        if (geoLocation.isPresent()) {

                List<GeoLocation> geoLocations = locationRepository.findByParent(geoLocation.get());
                for (GeoLocation geoLocate : geoLocations) {
                        LocationDto location = new LocationDto();
                        location.setId(geoLocate.getId());
                        location.setCode(geoLocate.getCode());
                        location.setName(geoLocate.getName());
                        location.setLocationType(geoLocate.getType().toString());
                        locations.add(location);
                }
                return new Response<List<LocationDto>>(ResponseCode.SUCCESS, true, "Successful..", locations);

        } else {
                return new Response<List<LocationDto>>(ResponseCode.SUCCESS, true, "Successful..", locations);
        }
    }

	@Override
	public Response<List<LocationDto>> findGeoLocationByType(GeoLocationType type) {

		List<LocationDto> locations = new ArrayList<>();
		List<GeoLocation> geoLocations = locationRepository.findByType(type);
		for (GeoLocation geoLocate : geoLocations) {
			LocationDto location = new LocationDto();
			location.setId(geoLocate.getId());
			location.setCode(geoLocate.getCode());
			location.setName(geoLocate.getName());
			location.setLocationType(geoLocate.getType().toString());
			locations.add(location);
		}
		return new Response<List<LocationDto>>(ResponseCode.SUCCESS, true, "Successful..", locations);
	}

    @Override
    public LocationDto getGeoLocationById(Long id) {        
        LocationDto response=new LocationDto();;
        try{        
            Optional<GeoLocation> geoLocation = locationRepository.findById(id);
            if (!geoLocation.isPresent()) {

                return response;  
            }
            
            response.setId(geoLocation.get().getId());
            response.setCode(geoLocation.get().getCode());
            response.setName(geoLocation.get().getName());
            response.setLocationType(geoLocation.get().getType().toString());
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

}
