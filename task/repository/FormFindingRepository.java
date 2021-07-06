/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tz.go.tcra.lims.entity.FormFinding;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface FormFindingRepository extends JpaRepository<FormFinding,Long>{
    
    Optional<FormFinding> findFirstByFormItemIdAndActivityId(Long formItemId,Long activityId);
}
