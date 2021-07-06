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
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name="lims_shareholder_change_application_details", schema="lims")
@Data
@NoArgsConstructor
public class ShareholderChangeApplicationDetail implements Serializable,Attachable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="fullname")
    private String fullname;
    
    @Column(name="shares_in_percentage")
    private Long shares;
    
    @Column(name="nationality")
    private String nationality;
    
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

    @Override
    public AttachableTypeEnum getAttachableType() {
        
        return AttachableTypeEnum.SHAREHOLDER_CHANGE;
    }
}
