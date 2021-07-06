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
import tz.go.tcra.lims.licencee.entity.ShareholderChangeApplicationDetail;


/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface ShareholderChangeApplicationDetailRepository extends JpaRepository<ShareholderChangeApplicationDetail, Long> {
    
    @Modifying
    void deleteByApplicationId(Long applicationId);
    
    @Query("SELECT e FROM ShareholderChangeApplicationDetail e "
            + "WHERE e.applicationId=?1")
    List<ShareholderChangeApplicationDetail> findShareholderDtoByApplicationId(Long applicationId);
}
