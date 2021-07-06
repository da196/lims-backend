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
import java.util.Set;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.feestructure.entity.Feeable;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_licencee_entities", schema = "lims")
@Data
@NoArgsConstructor
public class LicenceeEntity implements Serializable,Attachable,Feeable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="phone")
    private String phone;
    
    @Column(name="email")
    private String email;
    
    @Column(name="fax")
    private String fax;
    
    @Column(name="website")
    private String website;
    
    @Column(name="physical_address")
    private String physicalAddress;
    
    @Column(name="postal_address")
    private String postalAddress;
    
    @Column(name="country_id")
    private long countryID;
    
    @Column(name="region_id")
    private long regionID;
    
    @Column(name="district_id")
    private long districtID;
    
    @Column(name="ward_id")
    private long wardID;
    
    @Column(name="postal_code")
    private String postalCode;
    
    @Column(name="business_license_number")
    private String businessLicenceNo;
    
    @Column(name="registration_certificate_number")
    private String regCertNo;
    
    @Column(name="tin_number")
    private String tinNo;
    
    @Column(name="operational_status")//determines whether an entity is on process to change refer to OperationalEnum
    private String operationalStatus;
    
    @OneToOne
    @JoinColumn(name="category_id")
    private EntityCategory category;
    
    @OneToMany(mappedBy="licenceeEntity")
    private Set<LicenceeEntityShareholder> shareholders;
    
    @OneToOne
    @JoinColumn(name="user_id")
    private LimsUser user;
         
    @Column(name="approved")
    private int approval=1;
    
    @Column(name="active")
    private boolean active=true;
    
    @Column(name="date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name="date_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @Column(name="unique_id")
    private UUID uniqueID=UUID.randomUUID();
    
    @Override
    public AttachableTypeEnum getAttachableType() {
        
        return AttachableTypeEnum.ENTITY;
    }

    @Override
    public FeeableTypeEnum feeAbleType() {
        
        return FeeableTypeEnum.ENTITY;
    }
}
