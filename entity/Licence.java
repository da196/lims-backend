package tz.go.tcra.lims.entity;

import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.task.entity.Trackable;
import tz.go.tcra.lims.feestructure.entity.Feeable;
import tz.go.tcra.lims.product.entity.LicenceProduct;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.LicenceApplicationStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.LicenceStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_licences", schema = "lims")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Licence implements Serializable, Feeable,Attachable,Trackable {

    private static final long serialVersionUID = -2029799457121426097L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="application_number")
    private String applicationNumber;
    
    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();

    @OneToOne
    @JoinColumn(name = "applicant_entity_id", nullable = false)
    private LicenceApplicationEntity applicantEntity;

    @Column(name = "root_licence_category_id")
    private Long rootLicenceCategoryId;
    
    @ManyToOne
    @JoinColumn(name = "license_product_id")
    private LicenceProduct licenseProduct;

    @Column(name = "is_draft")
    private Boolean isDraft=true;
    
    @Column(name="licence_number")
    private String licenceNumber;
    
    @Column(name = "issued_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issuedDate;

    @Column(name = "expire_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "submitted_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;
    
    @Column(name = "cancelled_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelledAt;
    
    @OneToMany
    @JoinColumn(name = "attachable_id", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "attachable_type = 'LICENSE'")
    @JsonIgnore
    private List<Attachment> attachments;

    @Column(name = "application_state")
    @Enumerated(EnumType.STRING)
    private LicenceApplicationStateEnum applicationState=LicenceApplicationStateEnum.NEW;
    
    @Column(name = "operational_status")//determines whether a licence is on process to change refer to OperationalEnum
    private String operationalStatus;
    
    @Column(name = "license_state")
    @Enumerated(EnumType.STRING)
    private LicenceStateEnum licenseState=LicenceStateEnum.APPLICATION;
    
    @Column(name = "current_decision")
    private String decision;//uses workflow decision enum
    
    @Column(name = "comments")
    private String comments;//comments on a licence currently used to show applicant during resubmission

    @Column(name = "active")
    private Boolean active = true;
    
    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;
    
    @Column(name = "creator_id")
    private Long applicant;

    @Column(name = "declaration")
    private Boolean declaration=false;

    @Column(name="reference_licence_id")
    private Long referenceLicenceId=0L;
    
    @Override
    public FeeableTypeEnum feeAbleType() {
        
        return FeeableTypeEnum.LICENSE;
    }

    @Override
    public AttachableTypeEnum getAttachableType() {
        
        return AttachableTypeEnum.LICENSE;
    }

    @Override
    public TrackableTypeEnum getTrackableType() {
        return TrackableTypeEnum.LICENCE;
    }
}
