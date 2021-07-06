/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;
import tz.go.tcra.lims.task.entity.TaskWorkflow;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface TaskWorkflowRepository extends JpaRepository<TaskWorkflow,Long>{
        
    boolean existsByTrackableIdAndTrackableTypeAndWorkflowIdAndActive(Long trackableId,TrackableTypeEnum trackableType,Long workflowId,boolean active);

    List<TaskWorkflow> findByTrackableIdAndTrackableTypeAndActive(Long trackableId,TrackableTypeEnum trackableType,boolean active);
}
