/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.LicenceCategoryServiceDetail;
import tz.go.tcra.lims.entity.LicenceCategory;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceCategoryServiceRepository extends JpaRepository<LicenceCategoryServiceDetail,Long>{
    
    @Modifying
    @Query("DELETE FROM LicenceCategoryServiceDetail e WHERE e.category=?1")
    void deleteByCategory(LicenceCategory category);
    
    List<LicenceCategoryServiceDetail> findByCategoryAndActive(LicenceCategory category,Boolean active);
}
