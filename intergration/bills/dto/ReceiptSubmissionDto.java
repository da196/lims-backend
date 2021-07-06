package tz.go.tcra.lims.intergration.bills.dto;

import java.util.List;
import lombok.Data;


@Data
public class ReceiptSubmissionDto {

	private String description;

	// private String receivableDate;

	private String receiptDate;

	private String bankAccountNo;

	private String exchequerNo;

	private String billNumber;

	private String referenceNumber;

	private String branchCode;

	private String attachment;

	// private OperationType operationType;

	private String departmentCode;

	private Double amount;

	private String currencyCode;
	private List<EntryDto> entries;
	private Double exchangeRate;
	private ClientDto client;

	private String title;
	private String generatedBy;
	private String controlNumber;

}
