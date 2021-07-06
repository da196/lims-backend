package tz.go.tcra.lims.intergration.bills.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class InvoiceExpireDto {

	private List<String> bills;

}
