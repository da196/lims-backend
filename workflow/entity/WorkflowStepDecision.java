/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Data;

import lombok.NoArgsConstructor;
import tz.go.tcra.lims.miscellaneous.entity.NotificationTemplate;
import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_workflow_step_decisions", schema = "lims")
@Data
@NoArgsConstructor
public class WorkflowStepDecision implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

        @JsonIgnore
	@Column(name = "workflow_step_id")
	private Long step;

	@Column(name = "decision")
	@Enumerated(EnumType.STRING)
	private WorkflowDecisionEnum decision;

        @OneToOne
        @JoinColumn(name = "licence_status_id")
	private Status licenceStatus;
        
        @OneToOne
        @JoinColumn(name = "notification_template_id")
	private NotificationTemplate notificationTemplate;
        
	@Column(name = "date_created")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt = LocalDateTime.now();

	@JsonIgnore
	@Column(name = "active")
	private boolean active = true;

	@JsonIgnore
	@Column(name = "unique_id")
	private UUID uniqueID = UUID.randomUUID();
}
