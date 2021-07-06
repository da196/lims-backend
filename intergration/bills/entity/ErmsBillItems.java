/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.intergration.bills.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_erms_bill_items", schema = "lims")
@Data
@NoArgsConstructor
public class ErmsBillItems implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "unitPrice")
	private BigDecimal unitPrice;

	@Column(name = "accountCode")
	private String accountCode;

	@Column(name = "quantity")
	private Long quantity;

	@Column(name = "gfsCode")
	private String gfsCode;

	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "item")
	@JsonIgnore
	private List<ErmsBillItemTaxCharges> taxCharges = new ArrayList<>();

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "bill_id")
	// @JsonBackReference
	@JsonIgnore
	private ErmsBills bill;

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
