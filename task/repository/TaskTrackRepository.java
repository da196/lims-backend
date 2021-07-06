/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.entity.Trackable;
import tz.go.tcra.lims.task.dto.TaskMinDto;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface TaskTrackRepository extends JpaRepository<TaskTrack,Long>{
    
    
    @Query("SELECT new tz.go.tcra.lims.task.dto.TaskMinDto("
            + "e.id,"
            + "e.trackName,"
            + "e.trackableId,"
            + "e.trackableType,"
            + "e.createdAt,"
            + "e.dueDate) FROM TaskTrack e WHERE e.isActed=false")
    Page<TaskMinDto> findAllTasks(Pageable pageable);
    
    @Query("SELECT new tz.go.tcra.lims.task.dto.TaskMinDto("
            + "e.id,"
            + "e.trackName,"
            + "e.trackableId,"
            + "e.trackableType,"
            + "e.createdAt,"
            + "e.dueDate) FROM TaskTrack e WHERE e.actorId=?1 AND e.isActed=false")
    Page<TaskMinDto> findMyTasks(Long actorId,Pageable pageable);
    
    Optional<TaskTrack> findFirstByWorkflowStepAndTrackableIdAndTrackableTypeAndIsActed(WorkflowStep step,Long trackableId,TrackableTypeEnum trackableType,Boolean isActed);
    
    @Query("SELECT e FROM TaskTrack e WHERE e.trackable=?1 ORDER BY createdAt DESC")
    List<TaskTrack> findByTrackableOrderByCreatedAtDesc(Trackable trackable);
    
    Optional<TaskTrack> findFirstByTrackableIdAndTrackableTypeAndIsActedAndActorIdOrderByCreatedAtDesc(Long trackableId,TrackableTypeEnum trackable,Boolean isActed,Long actorId);
    
    Optional<TaskTrack> findByIdAndIsActedAndActorIdOrderByCreatedAtDesc(Long id,Boolean isActed,Long actorId);
    
    Optional<TaskTrack> findByTrackableIdAndTrackableTypeAndActorIdAndIsActed(Long trackableId,TrackableTypeEnum trackableType,Long actorId,Boolean isActed);
    
    Optional<TaskTrack> findFirstByTrackableIdAndTrackableTypeAndIsActedOrderByCreatedAtDesc(Long trackableId,TrackableTypeEnum trackableType,Boolean isActed); //get last acted track on licence
    
    Optional<TaskTrack> findFirstByTrackableIdAndTrackableTypeAndIdLessThanAndIsActedOrderByIdDesc(Long trackableId,TrackableTypeEnum trackableType,Long id,Boolean isActed); //get last acted track on licence less than track id
    
    //find the previous assignees of the actor
    List<TaskTrack> findByAssigningActorIdAndIdAndTrackableTypeAndWorkflowIdOrderByCreatedAtDesc(Long assigningActorId,Long trackableId,TrackableTypeEnum trackableType,Long workflowId);
    
    List<TaskTrack> findByWorkflowIdAndWorkflowStepAndTrackableIdAndTrackableTypeAndIsActed(Long workflowId,WorkflowStep step,Long trackableId,TrackableTypeEnum trackableType,Boolean isActed);
    
    boolean existsByTrackableIdAndTrackableTypeAndAssociationIdAndIsActed(Long trackableId,TrackableTypeEnum trackableType,Integer associationId,Boolean isActed);
    
    List<TaskTrack> findByActorIdAndIsActed(Long actorId,Boolean isActed);
}
