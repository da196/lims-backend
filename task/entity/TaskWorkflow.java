package tz.go.tcra.lims.task.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_task_workflows", schema = "lims")
@NoArgsConstructor
@Getter
@Setter
public class TaskWorkflow implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "workflow_id")
    private Long workflowId;

    @JsonIgnore
    @AnyMetaDef(
        name = "TaskWorkflowMetaDef",
        idType = "long",
        metaType = "string",
        metaValues = {
            @MetaValue(targetEntity = Licence.class, value = "LICENCE"),
            @MetaValue(targetEntity = LicenceeEntity.class, value = "ENTITY_APPLICATION")
        }
    )
    @Any(
        metaDef = "TaskWorkflowMetaDef",
        metaColumn = @Column(name = "trackable_type")
    )
    @JoinColumn(name = "trackable_id")
    private Trackable trackable;
        
    @Column(name = "trackable_type",insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private TrackableTypeEnum trackableType;
    
    @Column(name = "trackable_id",insertable = false,updatable = false)
    private Long trackableId;
    
    @Column(name = "active")
    private boolean active=true;
    
    @JsonIgnore
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @JsonIgnore
    @Column(name = "date_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();
}
