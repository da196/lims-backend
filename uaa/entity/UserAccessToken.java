/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_user_access_tokens", schema = "lims")
@Data
@NoArgsConstructor
public class UserAccessToken implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "token")
	private String token;

	@Column(name = "active")
	private boolean status;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "expire_at")
	private LocalDateTime expireAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "date_created")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "date_updated")
	private LocalDateTime updatedAt;

	@Column(name = "unique_id")
	private UUID uniqueID=UUID.randomUUID();
}
