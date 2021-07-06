/*
 * To change this license header, choose Licence Headers in Project Properties.
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
import java.sql.Time;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_presentations", schema = "lims")
@Data
@NoArgsConstructor
public class LicencePresentation implements Serializable,Attachable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
        
        @OneToOne
	@JoinColumn(name = "licence_id")
	private Licence licence;

        @Column(name = "workflow_id")
        private Long workflowId;
        
        @Column(name = "workflow_step_id")
        private Long workflowStepId;
        
	@Column(name = "presentation_date")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime presentationDate;

        @Column(name = "remark")
	private String remark;
        
	@Column(name = "status")
        @Enumerated(EnumType.STRING)
	private WorkflowDecisionEnum status;
        
	@Column(name = "active")
	private Boolean active=false;
        
	@Column(name = "date_created")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt=LocalDateTime.now();

	@Column(name = "date_updated")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	@JsonIgnore
	@Column(name = "unique_id")
	private UUID uniqueID=UUID.randomUUID();

    @Override
    public AttachableTypeEnum getAttachableType() {
        return AttachableTypeEnum.PRESENTATION;
    }
}
