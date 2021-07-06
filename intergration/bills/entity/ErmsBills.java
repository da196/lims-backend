/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.intergration.bills.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_erms_bills", schema = "lims")
@Data
@NoArgsConstructor
public class ErmsBills implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "bill_type")
	private String billType;

	@Column(name = "description")
	private String description;

	@OneToOne
	@JoinColumn(name = "client", nullable = false)
	private ErmsBillClients client;

	@NotNull
	@Column(name = "bill_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date billDate;

	@OneToOne
	@JoinColumn(name = "currency", nullable = false)
	private ListOfValue currency;

	@Column(name = "bill_expire_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date billExpiryDate;

	@Column(name = "source_ref")
	private String sourceRef;

	@OneToMany(mappedBy = "bill")
	@JsonIgnore
	private List<ErmsBillItems> items = new ArrayList<>();

	@Column(name = "mode_of_settlement")
	private String modeOfSettlement;

	@Column(name = "approved")
	private Boolean approved = true;

	@Column(name = "active")
	private boolean active = true;

	@Column(name = "date_created")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "date_updated")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	@JsonIgnore
	@Column(name = "uuid", unique = true)
	private UUID uuid = UUID.randomUUID();
}
