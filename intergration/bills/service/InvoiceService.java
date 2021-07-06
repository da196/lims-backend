package tz.go.tcra.lims.intergration.bills.service;

import org.springframework.stereotype.Service;

import tz.go.tcra.lims.intergration.bills.dto.InvoiceCancelDto;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceExpireDto;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceSubmissionDto;
import tz.go.tcra.lims.intergration.bills.dto.ReceiptCancelDto;
import tz.go.tcra.lims.intergration.bills.dto.ReceiptSubmissionDto;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

@Service
public interface InvoiceService {

	Response<Object> generateAndSendInvoice(InvoiceSubmissionDto invoiceDto) throws Exception;

	Response<Object> generateAndSendReceipt(ReceiptSubmissionDto receiptDto) throws Exception;

	Response<Object> generateAndSendReceiptCancel(ReceiptCancelDto receiptCancelDto) throws Exception;

	Response<Object> generateAndSendInvoiceCancel(InvoiceCancelDto invoiceCancelDto) throws Exception;

	Response<Object> generateAndSendInvoiceExpired(InvoiceExpireDto invoiceExpireDto) throws Exception;

}
