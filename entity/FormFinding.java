/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.entity;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_form_findings", schema = "lims")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormFinding implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "activity_id")
    private Long activityId;

    @Column(name = "form_item_id ")
    private Long formItemId;

    @Column(name="findings")
    private String findings;
    
    @Column(name="remarks")
    private String remarks;
    
    @JsonIgnore
    @Column(name = "date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "date_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(name = "unique_id")
    private UUID uniqueID = UUID.randomUUID();
}
