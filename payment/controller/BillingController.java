package tz.go.tcra.lims.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.miscellaneous.enums.BillingStatusEnum;
import tz.go.tcra.lims.payment.dto.BillingControlNumberRequestDto;
import tz.go.tcra.lims.payment.dto.BillingRevenueDto;
import tz.go.tcra.lims.payment.dto.InitiateBillByInternalOfficerDto;
import tz.go.tcra.lims.payment.entity.Billing;
import tz.go.tcra.lims.payment.service.BillingService;
import tz.go.tcra.lims.utils.Response;

@RestController()
@RequestMapping("v1/billing")
public class BillingController {
	@Autowired
	private BillingService billingService;

	@GetMapping(value = "/getAllRevenue")
	@PreAuthorize("hasRole('ROLE_VIEW_BILLING_REPORT')")
	public Response<List<BillingRevenueDto>> getBillingRevenue() {
		return billingService.getBillingRevenue();

	}

	@PostMapping(value = "/request-control-number")
	public Response<Billing> requestControlNumber(
			@RequestBody BillingControlNumberRequestDto billingControlNumberRequestDto) {

		return billingService.requestControlNumber(billingControlNumberRequestDto);

	}

	@PostMapping(value = "/update-bill-andrequest-control-number")
	@PreAuthorize("hasRole('ROLE_BILLING_REQUEST_CONTROL_NUMBER')")
	public Response<Billing> updateBillAndRequestControlNumber(
			@RequestBody BillingControlNumberRequestDto billingControlNumberRequestDto) {

		return billingService.updateBillAndRequestControlNumber(billingControlNumberRequestDto);
	}

	@GetMapping(value = "/getAllRevenue-pageable")
	@PreAuthorize("hasRole('ROLE_VIEW_BILLING_REPORT')")
	public Response<CollectionModel<EntityModel<BillingRevenueDto>>> getBillingRevenuePageable(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(name = "status", defaultValue = "PAID") BillingStatusEnum status) {
		return billingService.getBillingRevenuePageable(keyword, pageable, status);

	}

	@PostMapping(value = "/initiate-bill")
	@PreAuthorize("hasRole('ROLE_BILLING_INITIATION_INTERNAL')")
	public Response<EntityModel<Billing>> initiateBillFromTCRAOfficer(
			@RequestBody InitiateBillByInternalOfficerDto initiateBillByInternalOfficerDto) {

		return billingService.initiateBillFromTCRAOfficer(initiateBillByInternalOfficerDto);

	}

	@PutMapping(value = "/cancel-bill")
	public Response<EntityModel<Billing>> CancelBill(@RequestParam(name = "BillId") Long BillId) {

		return billingService.CancelBill(BillId);
	}

	@PostMapping(value = "/initiate-bill_internally")
	public Response<Billing> initiateBill(@RequestBody BillingControlNumberRequestDto billingControlNumberRequestDto) {
		return billingService.initiateBill(billingControlNumberRequestDto);
	}

}
