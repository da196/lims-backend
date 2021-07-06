/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.LicenceApplicationShareholder;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceApplicationEntityShareholderRepository extends JpaRepository<LicenceApplicationShareholder,Long>{
    
    @Modifying
    @Query("UPDATE LicenceApplicationShareholder e SET e.status=?1 WHERE e.licenceEntity=?2")
    void deActivateLicenceApplicationShareholders(Boolean status,Long licenceEntity);
}
