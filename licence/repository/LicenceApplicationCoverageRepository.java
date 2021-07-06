/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.LicenceApplicationCoverage;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceApplicationCoverageRepository extends JpaRepository<LicenceApplicationCoverage,Long>{
    
    @Modifying
    @Query("DELETE FROM LicenceApplicationCoverage e WHERE e.licenseId=?1")
    void deleteByLicenseId(Long licenseId);
    
    Set<LicenceApplicationCoverage> findByLicenseId(Long licenseId);
}
