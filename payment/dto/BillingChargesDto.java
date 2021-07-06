package tz.go.tcra.lims.payment.dto;

import lombok.Data;

@Data
public class BillingChargesDto {

	private Long id;

	private Long billingId;

	private Double amount;

	private String status;

	private String feeType;
	private Long feeTypeId;
	private String currency;

	private Boolean active;

}
