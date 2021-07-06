/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;
import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.entity.Trackable;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface TaskActivityRepository extends JpaRepository<TaskActivity,Long>{
    
    @Query("SELECT e FROM TaskActivity e WHERE e.trackable=?1")
    List<TaskActivity> findByTrackable(Trackable trackable);
    
    TaskActivity findFirstByTrack(TaskTrack track);
    
    Optional<TaskActivity> findFirstByTrackableIdAndAndTrackableTypeOrderByCreatedAtDesc(Long trackableId,TrackableTypeEnum trackableType);
}
