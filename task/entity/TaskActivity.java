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
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.Where;
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.entity.Attachment;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_task_activities", schema = "lims")
@Data
@NoArgsConstructor
public class TaskActivity implements Serializable,Attachable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "activity_name")
    private String activityName;
    
    @JsonIgnore
    @AnyMetaDef(
        name = "TaskActivityMetaDef",
        idType = "long",
        metaType = "string",
        metaValues = {
            @MetaValue(targetEntity = Licence.class, value = "LICENCE"),
            @MetaValue(targetEntity = LicenceeEntity.class, value = "ENTITY_APPLICATION")
        }
    )
    @Any(
        metaDef = "TaskActivityMetaDef",
        metaColumn = @Column(name = "trackable_type")
    )
    @JoinColumn(name = "trackable_id")
    private Trackable trackable;
    
    @Column(name = "trackable_type",insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private TrackableTypeEnum trackableType;
    
    @Column(name = "trackable_id",insertable = false,updatable = false)
    private Long trackableId;
    
    @OneToOne
    @JoinColumn(name = "track_id")
    private TaskTrack track;

    @Column(name = "comments ")
    private String comments;

    @Column(name="decision")
    @Enumerated(EnumType.STRING)
    private WorkflowDecisionEnum decision;
    
    @OneToMany
    @JoinColumn(name = "attachable_id", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "attachable_type = 'LICENSEACTIVITY'")
    private List<Attachment> attachments;
    
    @JsonIgnore
    @Column(name = "form_id")
    private Long formId=0L;

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
    @Column(name = "unique_id")
    private UUID uniqueID = UUID.randomUUID();

    @JsonIgnore
    @Override
    public AttachableTypeEnum getAttachableType() {        
        return AttachableTypeEnum.LICENSEACTIVITY;
    }
}
