/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.licencee.dto.EntityApplicationMinDto;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMinPortalDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface EntityApplicationRepository extends JpaRepository<EntityApplication, Long> {

	@Query("SELECT new tz.go.tcra.lims.licencee.dto.EntityApplicationMinDto(" + "e.id," + "e.applicantEntity.name,"
			+ "e.isDraft," + "e.status.name," + "e.entityProduct.id," + "e.entityProduct.name," + "e.createdAt,"
			+ "e.approvedAt) FROM EntityApplication e")
	Page<EntityApplicationMinDto> findAllApplications(Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.licencee.dto.EntityApplicationMinDto(" + "e.id," + "e.applicantEntity.name,"
			+ "e.isDraft," + "e.status.name," + "e.entityProduct.id," + "e.entityProduct.name," + "e.createdAt,"
			+ "e.approvedAt) FROM EntityApplication e WHERE "
			+ "CONCAT(e.applicantEntity.name,e.entityProduct.name,e.createdAt,e.approvedAt,e.status.name) LIKE %?1%")
	Page<EntityApplicationMinDto> findAllApplicationsSearchByKeyword(String keyword, Pageable pageable);

	@Query("SELECT e FROM EntityApplication e WHERE e.applicantEntity.id=?1 AND e.id=?2")
	Optional<EntityApplication> findByApplicantEntityIdAndId(Long applicantEntityId, Long id);

	@Query("SELECT new tz.go.tcra.lims.portal.application.dto.EntityApplicationMinPortalDto(" + "e.id,"
			+ "e.applicantEntity.name," + "e.isDraft," + "e.status.displayName," + "e.entityProduct.id,"
			+ "e.entityProduct.name," + "e.comments," + "e.createdAt," + "e.approvedAt) FROM EntityApplication e "
			+ "WHERE e.applicantEntity.id=?1 " + "ORDER BY e.createdAt DESC")
	List<EntityApplicationMinPortalDto> findByApplicantEntityId(Long applicantEntityId);

	@Query("SELECT new tz.go.tcra.lims.portal.application.dto.EntityApplicationMinPortalDto(" + "e.id,"
			+ "e.applicantEntity.name," + "e.isDraft," + "e.status.displayName," + "e.entityProduct.id,"
			+ "e.entityProduct.name," + "e.comments," + "e.createdAt," + "e.approvedAt) FROM EntityApplication e "
			+ "WHERE e.applicantEntity.id=?1 AND e.decision=?2 " + "ORDER BY e.createdAt DESC")
	List<EntityApplicationMinPortalDto> findByApplicantEntityIdAndDecision(Long applicantEntityId, String decision);

	List<EntityApplication> findByApplicantEntity(LicenceeEntity userEntity);
}
