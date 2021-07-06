/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.licencee.entity.LicenceeEntityShareholder;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceeEntityShareholderRepository extends JpaRepository<LicenceeEntityShareholder,Long>{
    
    List<LicenceeEntityShareholder> findByLicenceeEntity(Long entityId);
    
    @Query("SELECT e FROM LicenceeEntityShareholder e WHERE e.licenceeEntity=?1")
    List<LicenceeEntityShareholder> findCustomByLicenceeEntity(Long entityId);
    
    @Modifying
    void deleteByLicenceeEntity(Long entityId);
}
