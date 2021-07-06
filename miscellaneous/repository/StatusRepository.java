package tz.go.tcra.lims.miscellaneous.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.miscellaneous.dto.StatusMinDto;

/**
 * @author DonaldSj
 */

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

	boolean existsByDisplayName(String name);

	@Query("SELECT new tz.go.tcra.lims.miscellaneous.dto.StatusMinDto(e.id,e.name,e.displayName) " + "FROM Status e "
			+ "WHERE e.active=?1 " + "ORDER BY e.name ASC")
	List<StatusMinDto> findByActive(Boolean active);

	@Query("SELECT e FROM Status e WHERE e.name=?1 OR e.displayName=?1")
	Page<Status> findByNameOrDisplayName(String name, Pageable page);

	@Query("SELECT b FROM Status b WHERE  " + "CONCAT(b.name,b.phase,b.displayName,b.group,b.description)"
			+ " LIKE %?1%")
	Page<Status> findAllByKeyword(String keyword, Pageable pageable);
}
