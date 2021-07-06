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
import tz.go.tcra.lims.licencee.dto.LicenceeEntityMinDto;

import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.portal.application.dto.LicenceeEntityMinMinPortalDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceeEntityRepository extends JpaRepository<LicenceeEntity, Long> {

	public Optional<LicenceeEntity> findLicenceeEntityById(Long id);

	@Query("SELECT e FROM LicenceeEntity e WHERE e.user.id=?1")
	public Optional<LicenceeEntity> findLicenceeEntityByUser(Long id);

	public boolean existsByIdAndActive(Long entityId, boolean b);

	public LicenceeEntity findByIdAndActive(Long entityId, boolean b);
        
        @Query("SELECT new tz.go.tcra.lims.licencee.dto.LicenceeEntityMinDto("
                + "e.id,"
                + "e.name,"
                + "e.phone,"
                + "e.email,"
                + "e.fax,"
                + "e.website,"
                + "e.physicalAddress,"
                + "e.postalAddress,"
                + "e.postalCode,"
                + "e.category.name,"
                + "e.active) FROM LicenceeEntity e")
        Page<LicenceeEntityMinDto> findAllEntities(Pageable pageable);
        
        @Query("SELECT new tz.go.tcra.lims.licencee.dto.LicenceeEntityMinDto("
                + "e.id,"
                + "e.name,"
                + "e.phone,"
                + "e.email,"
                + "e.fax,"
                + "e.website,"
                + "e.physicalAddress,"
                + "e.postalAddress,"
                + "e.postalCode,"
                + "e.category.name,"
                + "e.active) FROM LicenceeEntity e WHERE "
                + "CONCAT(e.name,e.email,e.category.name,e.postalCode,e.postalAddress,e.website) LIKE %?1%")
        Page<LicenceeEntityMinDto> findAllSearchByKeyword(String keyword,Pageable pageable);
        
        @Query("SELECT new tz.go.tcra.lims.licencee.dto.LicenceeEntityMinDto("
                + "e.id,"
                + "e.name,"
                + "e.phone,"
                + "e.email,"
                + "e.fax,"
                + "e.website,"
                + "e.physicalAddress,"
                + "e.postalAddress,"
                + "e.postalCode,"
                + "e.category.name,"
                + "e.active) FROM LicenceeEntity e WHERE e.active=?1")
        Page<LicenceeEntityMinDto> findAllEntitiesByStatus(Boolean status,Pageable pageable);
        
        @Query("SELECT new tz.go.tcra.lims.licencee.dto.LicenceeEntityMinDto("
                + "e.id,"
                + "e.name,"
                + "e.phone,"
                + "e.email,"
                + "e.fax,"
                + "e.website,"
                + "e.physicalAddress,"
                + "e.postalAddress,"
                + "e.postalCode,"
                + "e.category.name,"
                + "e.active) FROM LicenceeEntity e WHERE "
                + "CONCAT(e.name,e.email,e.category.name,e.postalCode,e.postalAddress,e.website) LIKE %?1% AND e.active=?2")
        Page<LicenceeEntityMinDto> findAllSearchByKeywordAndStatus(String keyword,Boolean status,Pageable pageable);
        
        @Query("SELECT new tz.go.tcra.lims.portal.application.dto.LicenceeEntityMinMinPortalDto(e.id,e.name) FROM LicenceeEntity e WHERE e.active=?1")
        List<LicenceeEntityMinMinPortalDto> findAllEntitiesPortalByStatus(Boolean status);
}
