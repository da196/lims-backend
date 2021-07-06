package tz.go.tcra.lims.intergration.bills.dto;

import lombok.Getter;
import lombok.Setter;
import tz.go.tcra.lims.intergration.dto.BookSide;

@Setter
@Getter
public class EntryDto {
	private String accountCode;

	private Double amount;

	private String gfsCode;

	private String description;

	private BookSide bookSide;

	private Boolean serviceEntry;

	private Boolean taxEntry;

}
