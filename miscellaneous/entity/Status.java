/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_status", schema = "lims")
@Data
@NoArgsConstructor
public class Status implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "phase")
    private String phase;
    
    @Column(name = "name")
    private String name;

    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "group_name")
    private String group;
    
    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active = true;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="created_by")
    private LimsUser createdBy;
    
    @JsonIgnore
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="updated_by")
    private LimsUser updatedBy;
    
    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @Column(name = "unique_id")
    private UUID uniqueID=UUID.randomUUID();
}
