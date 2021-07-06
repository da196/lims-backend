package tz.go.tcra.lims.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tz.go.tcra.lims.payment.dto.InvoiceMockDto;
import tz.go.tcra.lims.payment.entity.InvoiceMock;
import tz.go.tcra.lims.utils.Response;

@Service
public interface InvoiceMockService {

	Response<InvoiceMockDto> getByIdInvoiceMock(Long id);

	Response<List<InvoiceMock>> getAllInvoiceMock();

}
