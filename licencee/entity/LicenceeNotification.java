/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_licencee_notifications", schema = "lims")
@Data
@NoArgsConstructor
public class LicenceeNotification implements Serializable,Attachable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="licencee_entity_id")
    private LicenceeEntity licencee;
    
    @OneToOne
    @JoinColumn(name="notification_category")
    private ListOfValue notificationCategory;
    
    @Column(name="message")
    private String message;
    
    @Column(name="ack")
    private Boolean ack=true;
    
    @Column(name="created_by")
    private Long createdBy;
    
    @Column(name="date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @Column(name="updated_by")
    private Long updatedBy;
    
    @Column(name="date_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @Column(name="unique_id")
    private UUID uniqueID=UUID.randomUUID();
    
    @Override
    public AttachableTypeEnum getAttachableType() {
        
        return AttachableTypeEnum.LICENCEE_NOTIFICATION;
    }
}
