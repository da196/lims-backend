/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.core.lang.Nullable;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.LicenceCategoryFlagEnum;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_licence_categories", schema = "lims")
@NoArgsConstructor
@Setter
@Getter
public class LicenceCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "code")
    private String code;

    @Column(name = "name",unique = true)
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "parent_id")
    private Long parent;

    @JsonIgnore
    @Column(name = "approved")
    private Boolean approved = true;

    @Column(name = "active")
    private Boolean active = true;

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
    
    @JsonIgnore
    @Basic(optional = true)
    @Where(clause = "deleted_at is null")
    @Column(name = "deleted_at")
    @Nullable
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy="category")
    private List<LicenceCategoryServiceDetail> services;

    @Column(name = "has_service")
    private Boolean hasService = false;

    @Column(name = "flag")
    @Enumerated(EnumType.STRING)
    private LicenceCategoryFlagEnum flag;

}
