package tz.go.tcra.lims.payment.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {

	private Long id;

	private Double amount;

	private Long currecyFromId;

	private Long currecyToId;

	private Date startDate;

	private Date expireDate;

}
