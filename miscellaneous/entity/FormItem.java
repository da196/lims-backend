/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import tz.go.tcra.lims.miscellaneous.enums.FormFeedbackTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.FormFlagEnum;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_form_items", schema = "lims")
@NoArgsConstructor
@Setter
@Getter
public class FormItem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "form_id")
    private Long formId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "active")
    private Boolean active = true;
    
    @Column(name = "parent")
    private Long parent;
    
    @Column(name = "flag")
    private FormFlagEnum flag;
    
    @Column(name = "feedback_type")
    @Enumerated(EnumType.STRING)
    private FormFeedbackTypeEnum feedbackType=FormFeedbackTypeEnum.NONE;
        
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by")
    private LimsUser createdBy;
    
    @JsonIgnore
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();

    @JsonIgnore
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private LimsUser updatedBy;
}
