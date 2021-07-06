package tz.go.tcra.lims.task.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;

import tz.go.tcra.lims.task.entity.TaskStatusHistory;
import tz.go.tcra.lims.task.dto.TaskStatusHistoryDto;
import tz.go.tcra.lims.task.entity.Trackable;

/**
 * @author DonaldSj
 */

@Repository
public interface TaskStatusHistoryRepository extends JpaRepository<TaskStatusHistory, Long> {

    boolean existsByTrackableIdAndTrackableTypeAndGroupAndPhase(Long trackableId,TrackableTypeEnum trackableType,String group,String phase);
    
    Optional<TaskStatusHistory> findFirstByTrackableIdAndTrackableTypeOrderByCreatedAtDesc(Long trackableId,TrackableTypeEnum trackableType);
    
    @Query("SELECT new tz.go.tcra.lims.task.dto.TaskStatusHistoryDto(e.group,e.phase,to_char(e.createdAt,'DY dd-MM-yyyy HH:MI:ss')) "
            + "FROM TaskStatusHistory e "
            + "WHERE e.trackable=?1 "
            + "ORDER BY e.createdAt DESC")
    List<TaskStatusHistoryDto> findByTrackable(Trackable trackable);
}
