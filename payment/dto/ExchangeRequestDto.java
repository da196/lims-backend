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
public class ExchangeRequestDto {
	private Double amount;

	private LocalDateTime exchangeDate;

	private Long currecyTo;

	private Long currecyFrom;

}
