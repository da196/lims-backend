package tz.go.tcra.lims.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.product.entity.LicenceProduct;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceProductRepository extends JpaRepository<LicenceProduct, Long> {

	boolean existsByNameOrCodeAndActive(String name, String code, boolean active);

	boolean existsByIdAndActive(Long id, boolean active);

	LicenceProduct findByIdAndActive(Long id, boolean active);

	List<LicenceProduct> findByActive(boolean active);

	Optional<LicenceProduct> findByLicenseCategory(LicenceCategory licenseCategory);

	@Query("SELECT e FROM LicenceProduct e WHERE e.licenseCategory.id=?1 AND e.active=?2")
	List<LicenceProduct> findByLicenseCategoryAndActivePortal(Long licenseCategoryId, Boolean active);

	Page<LicenceProduct> findByActive(boolean b, Pageable pageable);

	@Query("SELECT b FROM LicenceProduct b WHERE  " + "CONCAT(b.name,b.code,b.displayName,b.active)" + " LIKE %?1%")
	Page<LicenceProduct> findByKeywordActive(String keyword, boolean b, Pageable pageable);
}
