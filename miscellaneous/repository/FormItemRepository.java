/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.entity.FormItem;
import tz.go.tcra.lims.miscellaneous.enums.FormFlagEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface FormItemRepository extends JpaRepository<FormItem,Long> {
    
    List<FormItem> findByFormIdAndFlag(Long formId,FormFlagEnum flag);
    
    List<FormItem> findByFormIdAndActive(Long formId,Boolean active);
    
    @Query("SELECT e FROM FormItem e WHERE e.formId=?1 AND e.flag !=?2")
    List<FormItem> findByFormIdAndNonParameter(Long formId,FormFlagEnum flag);
    
    List<FormItem> findByParent(Long parentId);
}
