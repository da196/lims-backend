/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.licencee.dto.NameChangeDto;
import tz.go.tcra.lims.licencee.entity.NameChangeApplicationDetail;


/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface NameChangeApplicationDetailRepository extends JpaRepository<NameChangeApplicationDetail, Long> {
    
    @Modifying
    void deleteByApplicationId(Long applicationId);
    
    @Query("SELECT new tz.go.tcra.lims.licencee.dto.NameChangeDto(e.name) "
            + "FROM NameChangeApplicationDetail e "
            + "WHERE e.applicationId=?1")
    NameChangeDto findNameChangeDtoByApplicationId(Long applicationId);
}
