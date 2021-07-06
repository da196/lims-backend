package tz.go.tcra.lims.geolocation.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.GeoLocation;

@Repository
public interface GeoLocationRepository extends JpaRepository<GeoLocation, Long> {

	boolean existsByNameOrCodeAndActive(String name, String code, boolean b);

	boolean existsByIdAndActive(Long id, boolean b);

	GeoLocation findByIdAndActive(Long id, boolean b);

	List<GeoLocation> findByActive(boolean b);

	List<GeoLocation> findByParent(GeoLocation parent);

	boolean existsByNameAndActive(String name, boolean b);

	Page<GeoLocation> findByActive(boolean b, Pageable pageable);

	@Query("SELECT b FROM GeoLocation b WHERE  " + "CONCAT(b.name,b.code,b.parent.name,b.type,b.active)" + " LIKE %?1%")
	Page<GeoLocation> findByKeywordActive(String keyword, boolean b, Pageable pageable);

}
