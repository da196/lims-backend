package tz.go.tcra.lims.portal.geolocation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.GeoLocation;
import tz.go.tcra.lims.geolocation.dto.GeoLocationType;

@Repository
public interface LocationRepository extends JpaRepository<GeoLocation, Long> {

	List<GeoLocation> findByParent(GeoLocation geoLocation);

	List<GeoLocation> findByActive(boolean b);

	List<GeoLocation> findByType(GeoLocationType type);
}
