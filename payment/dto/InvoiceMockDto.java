package tz.go.tcra.lims.payment.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceMockDto {

	private Long id;

	private String invoiceNumber;

	private Double amount;

	private String amountInWords;

	private Double totalAmount;

	private String totalAmountInWords;

	private String customerCode;

	private String custumerName;

	private String address;

	private String description;

	private LocalDateTime cdate;

	private Boolean active = true;

}
