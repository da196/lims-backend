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

@Entity
@Table(name = "lims_mamlakapg_paid", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MamlakapgPaid implements Serializable {

	private static final long serialVersionUID = -2029799457121426097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "bill_id")
	private String billId;

	@Column(name = "transaction_id")
	private String trxID;

	@Column(name = "uuid", unique = true)
	private UUID uuid = UUID.randomUUID();

	@Column(name = "control_number")
	private String payCntrNum;

	@Column(name = "payReference_id")
	private String payRefId;

	@Column(name = "bill_amt")
	private String billAmt;

	@Column(name = "bill_payOpt")
	private String billPayOpt;

	@Column(name = "cCy")
	private String cCy;

	@Column(name = "trx_dtTm")
	private String trxDtTm;

	@Column(name = "usd_payChnl")
	private String usdPayChnl;

	@Column(name = "pyr_cellNum")
	private String pyrCellNum;

	@Column(name = "Pyr_email")
	private String pyrEmail;

	@Column(name = "pyr_name")
	private String pyrName;

	@Column(name = "psp_receiptNumber")
	private String pspReceiptNumber;

	@Column(name = "psp_name")
	private String pspName;

	@Column(name = "Ctr_accNum")
	private String ctrAccNum;

	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "active")
	private Boolean active = true;

}
