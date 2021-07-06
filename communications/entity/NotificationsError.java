/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.communications.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
import tz.go.tcra.lims.communications.dto.CommunicationChannel;

@Entity
@Table(name = "lims_notifications_error", schema = "lims")
@Data
@NoArgsConstructor
public class NotificationsError implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "subject")
	private String subject;

	@Column(name = "message")
	private String message;

	@Column(name = "contact")
	private String contact;

	@Column(name = "error")
	private String error;

	@Column(name = "channel")
	private CommunicationChannel channel;
//	@Column(name = "channel")
//	private int channel;

	@Column(name = "error_times")
	private int errorTimes;

	@Column(name = "approved")
	private Boolean approved;

	@Column(name = "active")
	private boolean active = true;

	@Column(name = "date_created")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "date_updated")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

}
