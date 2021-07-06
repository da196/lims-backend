package tz.go.tcra.lims.intergration.bills.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tz.go.tcra.lims.intergration.dto.OperationType;

@Data
@Setter
@Getter
public class InvoiceSubmissionDto {

	private String description;

	private String receivableDate;

	private OperationType operationType;

	private String billNumber;

	private String controlNumber;

	private String branchCode;

	private String departmentCode;

	private ClientDto client;

	private List<EntryDto> entries;

	private String attachment;

	private String generatedBy;

	private Double amount;

	private String currencyCode;

	private Double exchangeRate;

}
