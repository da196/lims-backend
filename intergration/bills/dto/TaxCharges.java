package tz.go.tcra.lims.intergration.bills.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author hcmis-development team
 * @date Dec 22, 2020
 * @version 1.0.0
 */

@Setter
@Getter
@NoArgsConstructor
public class TaxCharges {

	private Float taxRate;

	private String accountCode;

	private String description;

	private BigDecimal taxAmount;

}
