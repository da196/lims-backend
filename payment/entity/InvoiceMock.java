package tz.go.tcra.lims.payment.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_invoice_mock", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceMock implements Serializable {

	private static final long serialVersionUID = -2029799457121426097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "invoice_number")
	private String invoiceNumber;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "customer_code")
	private String customerCode;

	@Column(name = "custumer_name")
	private String custumerName;

	@Column(name = "address")
	private String address;

	@Column(name = "description")
	private String description;

	@Column(name = "cdate")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cdate;

	@Column(name = "active")
	private Boolean active = true;

}
