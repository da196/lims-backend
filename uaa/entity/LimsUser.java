package tz.go.tcra.lims.uaa.entity;

import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import lombok.NoArgsConstructor;
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;

@Entity
@Table(name = "lims_users", schema = "lims")
@Data
@NoArgsConstructor
public class LimsUser implements Serializable,Attachable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;
    
    @Column(name = "postal_address")
    private String postalAddress;
    
    @Column(name = "physical_address")
    private String physicalAddress;
    
    @Column(name = "country_id")
    private Long countryID;
    
    @Column(name = "region_id")
    private Long regionID;
    
    @Column(name = "district_id")
    private Long districtID;
    
    @Column(name = "ad_authentication")
    private boolean adAuthentication=false;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private boolean status=true;

    @Column(name = "is_complete")
    private boolean isComplete;
    
    @Column(name = "date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "date_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(name = "unique_id")
    private UUID uniqueID=UUID.randomUUID();

    @OneToOne(mappedBy="user")
    private LicenceeEntity userEntity;

    public String getFullName() {
        return this.firstName + " " + this.middleName + " " + this.lastName;
    }

    public LimsUser(String email, String password, boolean status, boolean isComplete) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.isComplete = isComplete;
    }

    @Override
    public AttachableTypeEnum getAttachableType() {
        
        return AttachableTypeEnum.USER;
    }
}
