package tz.go.tcra.lims.payment.dto;

import lombok.Data;

@Data
public class ExchangeResponseDto {
	private Double rate;
	private Double amountFrom;
	private Double amountTo;
	private String startDate;
	private String expireDate;
	private String year;
	private String month;
	private String currencyFrom;
	private String currencyTo;

}
