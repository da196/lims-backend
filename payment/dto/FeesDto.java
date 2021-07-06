package tz.go.tcra.lims.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FeesDto {

	private Long fee_id;
	private Double amount;

}
