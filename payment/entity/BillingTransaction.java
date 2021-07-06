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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.payment.dto.MamlakapgPaidDto;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_billing_transaction", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingTransaction implements Serializable {

	private static final long serialVersionUID = -2029799457121426097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "billing_id")
	private Long billingId;

	@Column(name = "control_number")
	private String controlNumber;

	@Column(name = "uuid", unique = true)
	private UUID uuid = UUID.randomUUID();

	@Column(name = "currency")
	private String currency;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "amount_paid")
	private Double amountPaid;

	@Column(name = "amount_pending")
	private Double amountPending;

	@Column(name = "channel")
	private String channel;

	@Column(name = "gepg_receipt")
	private String gepgRecept;

	@Column(name = "gepg_payment")
	private MamlakapgPaidDto gepgPayment;

	@Column(name = "issued_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime issuedDate;

	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "active")
	private Boolean active = true;

}
