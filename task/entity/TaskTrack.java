/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.entity;

import java.io.Serializable;
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
import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_task_tracks", schema = "lims")
@NoArgsConstructor
@Setter
@Getter
public class TaskTrack implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "workflow_id")
    private Long workflowId;

    @Column(name = "name")
    private String trackName;
    
    @ManyToOne
    @JoinColumn(name = "workflow_step_id")
    private WorkflowStep workflowStep;

    @Column(name = "assigning_actor_role_id ")
    private Long assigningActorRoleId=0L;
    
    @Column(name = "assigning_actor_id ")
    private Long assigningActorId=0L;
    
    @Column(name = "actor_role_id ")
    private Long actorRoleId=0L;

    @Column(name = "actor_id ")
    private Long actorId=0L;

    @JsonIgnore
    @AnyMetaDef(
        name = "TaskTrackMetaDef",
        idType = "long",
        metaType = "string",
        metaValues = {
            @MetaValue(targetEntity = Licence.class, value = "LICENCE"),
            @MetaValue(targetEntity = EntityApplication.class, value = "ENTITY_APPLICATION")
        }
    )
    @Any(
        metaDef = "TaskTrackMetaDef",
        metaColumn = @Column(name = "trackable_type")
    )
    @JoinColumn(name = "trackable_id")
    private Trackable trackable;

    @Column(name = "trackable_type",insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private TrackableTypeEnum trackableType;
    
    @Column(name = "trackable_id",insertable = false,updatable = false)
    private Long trackableId;
    
    @Column(name = "is_acted")
    private Boolean isActed = false;

    @Column(name = "is_associated")
    private Boolean isAssociated = false;

    @Column(name = "association_id")
    private int associationId=0;
    
    @Column(name = "due_date")
    private Date dueDate;
    
    @JsonIgnore
    @Column(name = "date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "date_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(name = "date_completed")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedAt;
    
    @JsonIgnore
    @Column(name = "unique_id")
    private UUID uniqueID = UUID.randomUUID();
}
