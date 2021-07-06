/*
 * To change this license header, choose Licence Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.entity;

import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.task.entity.LicencePresentation;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.MetaValue;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.licencee.entity.LicenceeEntityShareholder;
import tz.go.tcra.lims.licencee.entity.ShareholderChangeApplicationDetail;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_attachments", schema = "lims")
@Data
@NoArgsConstructor
public class Attachment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

        @JsonIgnore
	@AnyMetaDef(
            name = "AttachmentMetaDef",
            idType = "long",
            metaType = "string",
            metaValues = {
                @MetaValue(targetEntity = Licence.class, value = "LICENSE"),
                @MetaValue(targetEntity = LicenceeEntity.class, value = "ENTITY"),
                @MetaValue(targetEntity = TaskActivity.class, value = "LICENSEACTIVITY"),
                @MetaValue(targetEntity = LimsUser.class, value = "USER"),
                @MetaValue(targetEntity = LicencePresentation.class, value = "PRESENTATION"),
                @MetaValue(targetEntity = LicenceeEntityShareholder.class, value = "SHAREHOLDER"),
                @MetaValue(targetEntity = ShareholderChangeApplicationDetail.class, value = "SHAREHOLDER_CHANGE"),
            }
	)
        @Any(
            metaDef = "AttachmentMetaDef",
            metaColumn = @Column(name = "attachable_type")
	)
	@JoinColumn(name = "attachable_id")
	private Attachable attachable;

        @Column(name = "attachable_id",insertable = false,updatable = false)
        private Long attachableId;

        @Column(name = "attachable_type",insertable = false,updatable = false)
        @Enumerated(EnumType.STRING)
        private AttachableTypeEnum attachableType;
    
	@Column(name = "attachment_name")
	private String attachmentName;

	@Column(name = "uri")
	private String uri;

	@ManyToOne
	@JoinColumn(name = "attachment_type_id")
	private AttachmentType attachmentType;

	@JsonIgnore
	@Column(name = "approved")
	private int approved=1;

	@Column(name = "status")
	private boolean status=true;

	@Column(name = "date_created")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt=LocalDateTime.now();

	@Column(name = "date_updated")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	@JsonIgnore
	@Column(name = "unique_id")
	private UUID uniqueID=UUID.randomUUID();
        
}
