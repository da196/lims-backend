package tz.go.tcra.lims.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.portal.application.dto.ApplicantEntityDto;

@Setter
@Getter
@NoArgsConstructor
public class BillingReceiptDto {

	private ApplicantEntityDto entity;
	private String receiptNumber;
	private String payDate;
	private String controlNumber;
	private String currency;
	private String amountInWords;
	private Double amount;
	private String remark;

}
