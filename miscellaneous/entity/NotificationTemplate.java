/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_notification_templates", schema = "lims")
@NoArgsConstructor
@Setter
@Getter
public class NotificationTemplate implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "applicant_text_template")
    private String textTemplate;
    
    @Column(name = "applicant_email_template")
    private String emailTemplate;
    
    @Column(name = "staff_email_template")
    private String staffEmailTemplate;
    
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
