/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import tz.go.tcra.lims.geolocation.dto.GeoLocationType;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_geo_locations", schema = "lims")
@Data
@NoArgsConstructor
public class GeoLocation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;
	/*
	 * @Column(name = "parent_id") private Long parentId;
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_id")
	// @JsonBackReference
	@JsonIgnore
	private GeoLocation parent;

	@OneToMany(mappedBy = "parent")
	@JsonIgnore
	private List<GeoLocation> geoLocations = new ArrayList<>();

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private GeoLocationType type;

	@Column(name = "approved")
	private Boolean approved;

	@Column(name = "active")
	private boolean active;

	@Column(name = "date_created")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "date_updated")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	@Column(name = "unique_id")
	private UUID uniqueID;
}
