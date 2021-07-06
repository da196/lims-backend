package tz.go.tcra.lims.payment.entity;

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
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_billing_charges", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingCharges implements Serializable {

	private static final long serialVersionUID = -2029799457121426097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

        @ManyToOne
	@JoinColumn(name = "billing_id")
	private Billing billing;

	@Column(name = "uuid", unique = true)
	private UUID uuid = UUID.randomUUID();

	@Column(name = "amount")
	private Double amount;

	@Column(name = "status")
	private String status;

	@Column(name = "fee_type")
	private String feeType;

	@Column(name = "fee_id")
	private Long feeId;

	@Column(name = "active")
	private Boolean active = true;

	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

}
