package tz.go.tcra.lims.payment.entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.miscellaneous.enums.BillingStatusEnum;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_billing", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingReading implements Serializable {

	private static final long serialVersionUID = -2029799457121426097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "billing_number")
	private String billingNumber;

	@Column(name = "control_number")
	private String controlNumber;

	@Column(name = "gsf_code")
	private String gsfCode;

	@Column(name = "uuid", unique = true)
	private UUID uuid = UUID.randomUUID();

	@Column(name = "fee_id")
	private Long feeId;

	@Column(name = "licence_id")
	private Long licenceid;

	/*
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "licence_id", nullable = false) private Licence licence;
	 */

	@OneToOne
	@JoinColumn(name = "currency", nullable = false)
	private ListOfValue currency;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "amount_USD")
	private Double amountUSD;

	@Column(name = "amount_pending")
	private Double amountPending;

	@Column(name = "issued_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime issuedDate;

	@Column(name = "pay_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime payDate;

	@Column(name = "expire_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime expireDate;

	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private BillingStatusEnum status = BillingStatusEnum.BILLED;

	@Column(name = "attached_to")
	@Enumerated(EnumType.STRING)
	private BillingAttachedToEnum attachedTo;

	@Column(name = "active")
	private Boolean active = true;

	@Column(name = "gepg_status")
	private int gepgStatus = 0;

	@Column(name = "bill_rate")
	private Double billRate;

	@Column(name = "account_code")
	private String accountCode;

	@Column(name = "bank_account")
	private String bankAccount;

}
