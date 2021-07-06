/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.core.lang.Nullable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.miscellaneous.entity.Form;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowStepTypeEnum;
import tz.go.tcra.lims.uaa.entity.Role;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_workflow_steps", schema = "lims")
@Data
@NoArgsConstructor
public class WorkflowStep implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name="code")
    private String code;
    
    @Column(name="name")
    private String name;
    
    @Column(name="workflow_id")
    private Long workflow;
    
    @ManyToOne
    @JoinColumn(name="previous_role_id")
    @Nullable
    private Role previousRole;
    
    @ManyToOne
    @JoinColumn(name="current_role_id")
    private Role currentRole;
    
    @ManyToOne
    @JoinColumn(name="next_role_id")
    @Nullable
    private Role nextRole;
    
    @Column(name="step_number")
    private Integer stepNumber;
    
    @Column(name="due_days")
    private Integer dueDays;
    
    @Column(name="applicant_notify")
    private boolean applicantNotify=false;
    
    @Column(name="step_type")
    @Enumerated(EnumType.STRING)
    private WorkflowStepTypeEnum stepType;
    
    @OneToOne
    @JoinColumn(name="attachment_type")
    private AttachmentType attachmentType;
    
    @Column(name="applicable_state")
    @Enumerated(EnumType.STRING)
    private ApplicableStateEnum applicableState;
    
    @OneToMany(mappedBy="step",cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<WorkflowStepNotifyRole> notifyRoles;
    
    @OneToMany(mappedBy="step",cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<WorkflowStepDecision> decisions;
    
    @OneToOne
    @JoinColumn(name="form_id")
    private Form form;
    
    @JsonIgnore
    @Column(name="active")
    private boolean active=true;
    
    @JsonIgnore
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
}
