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

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_task_status_history", schema = "lims")
@Data
@NoArgsConstructor
public class TaskStatusHistory implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "phase")
    private String phase;
    
    @Column(name = "group_name")
    private String group;
    
    @JsonIgnore
    @AnyMetaDef(
        name = "TaskStatusHistoryMetaDef",
        idType = "long",
        metaType = "string",
        metaValues = {
            @MetaValue(targetEntity = Licence.class, value = "LICENCE"),
            @MetaValue(targetEntity = EntityApplication.class, value = "ENTITY_APPLICATION")
        }
    )
    @Any(
        metaDef = "TaskStatusHistoryMetaDef",
        metaColumn = @Column(name = "trackable_type")
    )
    @JoinColumn(name = "trackable_id")
    private Trackable trackable;
    
    @Column(name = "trackable_type",insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private TrackableTypeEnum trackableType;
    
    @Column(name = "trackable_id",insertable = false,updatable = false)
    private Long trackableId;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @JsonIgnore
    @Column(name = "unique_id")
    private UUID uniqueID=UUID.randomUUID();
}
