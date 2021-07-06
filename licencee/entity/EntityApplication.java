package tz.go.tcra.lims.licencee.entity;

import tz.go.tcra.lims.product.entity.EntityProduct;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.entity.Attachment;
import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.task.entity.Trackable;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;

/**
 * @author emmanuel.mfikwa
 */

@Entity
@Table(name = "lims_entity_applications", schema = "lims")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EntityApplication implements Serializable,Attachable,Trackable {

    private static final long serialVersionUID = -2029799457121426097L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();

    @OneToOne
    @JoinColumn(name = "licencee_entity_id", nullable = false)
    private LicenceeEntity applicantEntity;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private EntityProduct entityProduct;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Column(name = "approved_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
    
    @OneToMany
    @JoinColumn(name = "attachable_id", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "attachable_type = 'ENTITY_INDIVIDUAL_APPLICATION'")
    @JsonIgnore
    private List<Attachment> attachments;  
    
    @JsonIgnore
    @OneToMany(mappedBy="applicationId")
    private List<ShareholderChangeApplicationDetail> shareholderChangeDetails;  
    
//    @Column(name = "application_type")
//    @Enumerated(EnumType.STRING)
//    private EntityApplicationTypeEnum applicationType;//application type
    
    @Column(name = "current_decision")
    private String decision;//uses workflow decision enum

    @Column(name = "comments")
    private String comments;//comments on an applicantion currently used to show applicant during resubmission
    
    @Column(name = "active")
    private Boolean active = true;
    
    @Column(name = "is_draft")
    private Boolean isDraft;
    
    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;
    
    @Column(name = "creator_id")
    private Long applicant;

    @JsonIgnore
    @Override
    public AttachableTypeEnum getAttachableType() {
        
        return AttachableTypeEnum.ENTITY_INDIVIDUAL_APPLICATION;
    }

    @JsonIgnore
    @Override
    public TrackableTypeEnum getTrackableType() {
        
        return TrackableTypeEnum.ENTITY_APPLICATION;
    }
}
