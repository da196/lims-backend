package tz.go.tcra.lims.intergration.bills.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ReceiptCancelDto {

	private String billNumber;

	private String referenceNumber;

	private String branchCode;

	private String departmentCode;

	private List<EntryDto> invoiceEntries;

	private List<EntryDto> receiptEntries;

}
