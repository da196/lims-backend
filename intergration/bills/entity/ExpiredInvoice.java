package tz.go.tcra.lims.intergration.bills.entity;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
@Table(name = "lims_erms_invoice_expired", schema = "lims")
public class ExpiredInvoice implements Serializable {

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String billNumber;

	private Boolean receivedInErms = false;

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
