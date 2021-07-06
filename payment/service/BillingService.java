package tz.go.tcra.lims.payment.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import tz.go.tcra.lims.licence.dto.LicenceBillingDto;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.miscellaneous.enums.BillingStatusEnum;
import tz.go.tcra.lims.payment.dto.BillingControlNumberRequestDto;
import tz.go.tcra.lims.payment.dto.BillingRevenueDto;
import tz.go.tcra.lims.payment.dto.InitiateBillByInternalOfficerDto;
import tz.go.tcra.lims.payment.entity.Billing;
import tz.go.tcra.lims.utils.Response;

@Service
public interface BillingService {

	Billing saveBilling(Billing billing);

	Billing updateBilling(Billing billing, Long id);

	Billing updateBillingControlNumber(String billId, String controlNumber);

	Billing updateBillingPayment(String billId, String controlNumber, String paidAmount, String billAmount,
			String channel, String bankAccount, String gepgReceipt);

	Response<List<BillingRevenueDto>> getBillingRevenue();

	Response<Billing> requestControlNumber(BillingControlNumberRequestDto billingControlNumberRequestDto);

	Response<Billing> initiateBill(BillingControlNumberRequestDto billingControlNumberRequestDto);

	Response<EntityModel<Billing>> CancelBill(Long BillId);

	Response<Billing> updateBillAndRequestControlNumber(BillingControlNumberRequestDto billingControlNumberRequestDto);

	List<LicenceBillingDto> getLicenseBillingByLicenseid(Long LicenseId, BillingAttachedToEnum billable);

	String getMoneyInWords(Double amount);

	Response<CollectionModel<EntityModel<BillingRevenueDto>>> getBillingRevenuePageable(String keyword,
			Pageable pageable, BillingStatusEnum status);

	Response<EntityModel<Billing>> initiateBillFromTCRAOfficer(
			InitiateBillByInternalOfficerDto initiateBillByInternalOfficerDto);

}
