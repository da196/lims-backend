package tz.go.tcra.lims.licence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.LicenceServiceDetail;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceDetailRepository extends JpaRepository<LicenceServiceDetail, Long> {

	boolean existsByNameOrCodeAndActive(String name, String code, boolean b);

	boolean existsByIdAndActive(Long id, boolean b);

	LicenceServiceDetail findByIdAndActive(Long id, boolean b);

	List<LicenceServiceDetail> findByActive(boolean b);

	Page<LicenceServiceDetail> findByActive(boolean b, Pageable pageable);

	@Query("SELECT b FROM LicenceServiceDetail b WHERE  " + "CONCAT(b.name,b.code,b.active)"
			+ " LIKE %?1%  AND active= ?2")
	Page<LicenceServiceDetail> findByKeywordAndActive(String keyword, boolean b, Pageable pageable);
}
