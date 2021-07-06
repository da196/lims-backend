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
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_workflow_types", schema = "lims")
@Data
@NoArgsConstructor
public class WorkflowType implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name="code")
    private String code;
    
    @Column(name="name")
    private String name;
    
    @Column(name="display_name")
    private String displayName;
    
    @JsonIgnore
    @Column(name="description")
    private String description;
    
    @JsonIgnore
    @Column(name="active")
    private boolean active=true;
    
    @Column(name = "date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column(name = "date_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(name = "unique_id")
    private UUID uniqueID=UUID.randomUUID();

    public WorkflowType(String code, String name, String displayName, String description) {
        this.code = code;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }

    
    
}
