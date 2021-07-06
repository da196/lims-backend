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
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_workflows", schema = "lims")
@Data
@NoArgsConstructor
public class Workflow implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name="code",unique=true)
    private String code;
    
    @Column(name="name",unique=true)
    private String name;
    
    @Column(name="description")
    private String description;
    
    @ManyToOne
    @JoinColumn(name="workflow_type_id")
    private WorkflowType workflowType;
    
    @OneToMany(mappedBy="workflow",fetch = FetchType.LAZY)
    @OrderBy("stepNumber ASC")
    private List<WorkflowStep> steps;
    
    @Column(name="active")
    private boolean active=true;
    
    @JsonIgnore
    @Column(name = "created_by")
    private Long createdBy;
    
    @JsonIgnore
    @Column(name = "date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();

    @JsonIgnore
    @Column(name = "updated_by")
    private Long updatedBy;
    
    @JsonIgnore
    @Column(name = "date_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Nullable
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @Column(name = "unique_id")
    private UUID uniqueID=UUID.randomUUID();
}
