/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.dto.FormItemOptionMinDto;
import tz.go.tcra.lims.miscellaneous.entity.FormItemOption;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface FormItemOptionRepository extends JpaRepository<FormItemOption,Long> {
    
    @Query("SELECT new tz.go.tcra.lims.miscellaneous.dto.FormItemOptionMinDto(e.id,e.name) "
            + "FROM FormItemOption e WHERE e.formItemId=?1 AND e.active=?2")
    List<FormItemOptionMinDto> findByFormItemId(Long formItemId,Boolean active);
    
    Optional<FormItemOption> findByFormItemIdAndName(Long formItemId,String name);
    
    @Transactional
    @Modifying
    @Query("UPDATE FormItemOption e SET e.active=false WHERE e.formItemId=?1")
    void deactivateFormItemOption(Long formItemId);
}
