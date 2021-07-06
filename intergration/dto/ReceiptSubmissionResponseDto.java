package tz.go.tcra.lims.intergration.dto;

import lombok.Data;

@Data
public class ReceiptSubmissionResponseDto {

	private ErmsPayment payment;

	private String message;

}
