/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.entity;

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
@Table(name="lims_change_of_name_details", schema="lims")
@Data
@NoArgsConstructor
public class NameChangeApplicationDetail implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="application_id")
    private Long applicationId;
    
    @JsonIgnore
    @Column(name="date_created")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @JsonIgnore
    @Column(name="date_updated")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @Column(name="unique_id")
    private UUID uniqueID=UUID.randomUUID();
}
