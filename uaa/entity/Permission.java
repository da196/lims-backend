/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author Donald Sj
 */

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lims_permissions", schema = "lims")
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(unique = true)
	private String name;

	@Column(name = "group_name")
	private String groupName;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "is_fixed")
	private boolean fixed = false;

	@Column(name = "unique_id")
	private UUID uniqueID = UUID.randomUUID();

	public Permission(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}

	public Permission(String name, String displayName, String groupName) {
		this.name = name;
		this.displayName = displayName;
		this.groupName = groupName;
	}

	public Permission(String name, String displayName, boolean fixed) {
		this.name = name;
		this.displayName = displayName;
		this.fixed = fixed;
	}

	public Permission(String name, String displayName, String groupName, boolean fixed) {
		this.name = name;
		this.displayName = displayName;
		this.fixed = fixed;
		this.groupName = groupName;
	}
}
