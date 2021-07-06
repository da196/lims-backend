/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import lombok.NoArgsConstructor;
import tz.go.tcra.lims.miscellaneous.enums.AttachmentTypePurposeEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_attachment_types", schema = "lims")
@Data
@NoArgsConstructor
public class AttachmentType implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name",unique=true)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "is_primary")
	private Boolean isPrimary;

	@Column(name = "attachment_type_purpose")
	@Enumerated(EnumType.STRING)
	private AttachmentTypePurposeEnum attachmentTypePurpose;

	@Column(name = "active", nullable = false)
	private Boolean active = true;

        @JsonIgnore
	@Column(name = "date_created")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

        @JsonIgnore
	@Column(name = "date_updated")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

        @JsonIgnore
	@Column(name = "unique_id")
	private UUID uuid = UUID.randomUUID();
}
