package tz.go.tcra.lims.intergration.bills.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hcmis-development team
 * @date Dec 22, 2020
 * @version 1.0.0
 */

@Getter
@Setter
@NoArgsConstructor
public class Item {

	private BigDecimal unitPrice;

	private String accountCode;

	private Long quantity;

	private String gfsCode;

	private String description;

	private List<TaxCharges> taxCharges;

	public Item(BigDecimal unitPrice, String accountCode,
				Long quantity, String gfsCode,
				String description,
				List<TaxCharges> taxCharges) {
		this.unitPrice = unitPrice;
		this.accountCode = accountCode;
		this.quantity = quantity;
		this.gfsCode = gfsCode;
		this.description = description;
		this.taxCharges = taxCharges;
	}
}
