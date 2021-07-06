package tz.go.tcra.lims.intergration.bills.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.intergration.bills.dto.ClientDto;
import tz.go.tcra.lims.intergration.bills.dto.EntryDto;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceExpireDto;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceSubmissionDto;
import tz.go.tcra.lims.intergration.bills.dto.ReceiptSubmissionDto;
import tz.go.tcra.lims.intergration.bills.service.InvoiceService;
import tz.go.tcra.lims.intergration.dto.OperationType;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping("/v1/invoice")
@Slf4j
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;

	@PostMapping(value = "/send")
	public Response<Object> generateAndSendInvoice(@RequestBody InvoiceSubmissionDto invoiceRequest) throws Exception {
		log.info("CONTROLLER REACHED, CALLING SERVICE...");

		InvoiceSubmissionDto invoiceSubmissionDto = new InvoiceSubmissionDto();

		invoiceSubmissionDto.setAmount(100000.0);
		invoiceSubmissionDto.setAttachment("String");
		invoiceSubmissionDto.setBillNumber("TCRAHLMS637391");
		invoiceSubmissionDto.setBranchCode("HQ-TCRA");
		ClientDto client = new ClientDto();

		invoiceSubmissionDto.setClient(invoiceRequest.getClient());
		invoiceSubmissionDto.setControlNumber(invoiceRequest.getControlNumber());
		invoiceSubmissionDto.setCurrencyCode(invoiceRequest.getCurrencyCode());
		invoiceSubmissionDto.setDepartmentCode("1002");
		invoiceSubmissionDto.setDescription(invoiceRequest.getDescription());

		List<EntryDto> entries = new ArrayList<>();

		entries = invoiceRequest.getEntries();

		invoiceSubmissionDto.setEntries(entries);
		invoiceSubmissionDto.setExchangeRate(2300.0);
		invoiceSubmissionDto.setGeneratedBy(invoiceRequest.getGeneratedBy());
		invoiceSubmissionDto.setOperationType(OperationType.SUBMISSION);
		invoiceSubmissionDto.setReceivableDate(invoiceRequest.getReceivableDate());

		return invoiceService.generateAndSendInvoice(invoiceRequest);
	}

	@PostMapping(value = "/send-receipt")
	public Response<Object> generateAndSendReceipt(@RequestBody ReceiptSubmissionDto receiptDto) throws Exception {
		log.info("CONTROLLER REACHED, CALLING SERVICE...");
		return invoiceService.generateAndSendReceipt(receiptDto);
	}

	@PostMapping(value = "/send-expired-invoice")
	public Response<Object> generateAndSendInvoiceExpired(@RequestBody InvoiceExpireDto invoiceExpireDto)
			throws Exception {

		return invoiceService.generateAndSendInvoiceExpired(invoiceExpireDto);

	}

}
