package tz.go.tcra.lims.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.payment.dto.InvoiceMockDto;
import tz.go.tcra.lims.payment.entity.InvoiceMock;
import tz.go.tcra.lims.payment.service.InvoiceMockService;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping("/v1/invoice-mock")
public class InvoiceMockController {
	@Autowired
	private InvoiceMockService invoiceMockService;

	@GetMapping(value = "/{id}")
	Response<InvoiceMockDto> getInvoiceMockById(@PathVariable("id") Long id) {

		return invoiceMockService.getByIdInvoiceMock(id);

	}

	@GetMapping(value = "/getAllInvoice-mocks")
	Response<List<InvoiceMock>> getAllInvoiceMock() {

		return invoiceMockService.getAllInvoiceMock();

	}

}
