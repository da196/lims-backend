/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import tz.go.tcra.lims.uaa.entity.Role;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_workflow_step_notify_roles", schema = "lims")
@Data
@NoArgsConstructor
public class WorkflowStepNotifyRole implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @JsonIgnore
    @Column(name="workflow_step_id")
    private Long step;
    
    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;
    
    @JsonIgnore
    @Column(name = "date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @JsonIgnore
    @Column(name = "unique_id")
    private UUID uniqueID=UUID.randomUUID();
}
