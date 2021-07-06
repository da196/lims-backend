package tz.go.tcra.lims.payment.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.feestructure.repository.FeeStructureRepository;
import tz.go.tcra.lims.intergration.bills.dto.ClientDto;
import tz.go.tcra.lims.intergration.bills.dto.ClientType;
import tz.go.tcra.lims.intergration.bills.dto.EntryDto;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceCancelDto;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceSubmissionDto;
import tz.go.tcra.lims.intergration.bills.dto.ReceiptSubmissionDto;
import tz.go.tcra.lims.intergration.bills.service.InvoiceService;
import tz.go.tcra.lims.intergration.dto.BookSide;
import tz.go.tcra.lims.intergration.dto.OperationType;
import tz.go.tcra.lims.licence.dto.LicenceBillingChargesDto;
import tz.go.tcra.lims.licence.dto.LicenceBillingDto;
import tz.go.tcra.lims.licence.repository.LicenceApplicationEntityRepository;
import tz.go.tcra.lims.licence.repository.LicenceRepository;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.licencee.repository.EntityApplicationRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityRepository;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueDto;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.miscellaneous.enums.BillingStatusEnum;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.payment.dto.ApplicantDto;
import tz.go.tcra.lims.payment.dto.BillingControlNumberRequestDto;
import tz.go.tcra.lims.payment.dto.BillingRevenueDto;
import tz.go.tcra.lims.payment.dto.ExchangeRequestDto;
import tz.go.tcra.lims.payment.dto.ExchangeResponseDto;
import tz.go.tcra.lims.payment.dto.FeesDto;
import tz.go.tcra.lims.payment.dto.InitiateBillByInternalOfficerDto;
import tz.go.tcra.lims.payment.dto.LicenceDto;
import tz.go.tcra.lims.payment.dto.LicenceProductBDto;
import tz.go.tcra.lims.payment.dto.RequestForControlNumberGepgDto;
import tz.go.tcra.lims.payment.entity.Billing;
import tz.go.tcra.lims.payment.entity.BillingCharges;
import tz.go.tcra.lims.payment.entity.BillingReading;
import tz.go.tcra.lims.payment.entity.BillingTransaction;
import tz.go.tcra.lims.payment.entity.ErmsBankAccounts;
import tz.go.tcra.lims.payment.repository.BillingChargesRepository;
import tz.go.tcra.lims.payment.repository.BillingReadingRepository;
import tz.go.tcra.lims.payment.repository.BillingRepository;
import tz.go.tcra.lims.payment.repository.BillingTransactionRepository;
import tz.go.tcra.lims.payment.repository.ErmsBankAccountsRepository;
import tz.go.tcra.lims.portal.listofvalues.service.ListOfValuerService;
import tz.go.tcra.lims.task.service.TaskService;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.GeneralException;

@Slf4j
@Service
public class BillingServiceImpl implements BillingService {

	@Autowired
	private BillingRepository billingRepository;

	@Autowired
	private ErmsBankAccountsRepository ermsBankAccountsRepository;

	@Autowired
	private BillingReadingRepository billingReadingRepository;

	@Autowired
	private TaskService licenseTrackService;

	@Autowired
	private FeeStructureRepository licenseFeeStructureRepository;

	@Autowired
	private LicenceRepository licenseRepository;

	@Autowired
	private BillingChargesRepository billingChargesRepository;

	@Autowired
	private BillingTransactionRepository billingTransactionRepository;

	@Autowired
	private ExchangeRateService exchangeRateService;

	@Autowired

	private EntityApplicationRepository entityApplicationRepository;

	@Autowired
	private BillingServiceModelAssembler billingServiceModelAssembler;

	@Autowired
	private ListOfValueRepository listOfValueRepository;

	@Autowired
	private ListOfValuerService listOfValuerService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private LicenceApplicationEntityRepository licenceApplicationEntityRepository;

	@Autowired
	private ProcessGePGControlNumberService processGePGControlNumberService;

	@Autowired
	private PagedResourcesAssembler<BillingRevenueDto> pagedResourcesAssembler;

	@Autowired
	private LicenceeEntityRepository licenceeEntityRepository;

	@Value("${lims.payment.billapprovedby}")
	private String approvedby;

	@Value("${lims.payment.billpayOption}")
	private int PaymentMode;

	@Value("${lims.payment.billgenby}")
	private String genby;

	@Value("${lims.payment.billpayOption}")
	private String option;

	@Value("${lims.payment.billgsCode}")
	private String gscode;

	@Value("${lims.payment.name}")
	private String name;

	@Value("${lims.payment.period}")
	private String period;

	@Value("${lims.payment.description}")
	private String description;

	@Value("${erms.department.code}")
	private String departmentCode;

	@Value("${erms.branch.code}")
	private String branchCode;

	@Value("${erms.trade.receivable.account-code}")
	private String receivableAccountCode;

	@Value("${erms.trade.receivable.gsf.code}")
	private String receivableGsfCode;

	@Override
	public Billing saveBilling(Billing billing) {

		try {

			Billing billingresponse = billingRepository.saveAndFlush(billing);

			return billingresponse;

			// BillingCharges
		} catch (Exception e) {

			return null;
		}
	}

	@Override
	public Billing updateBilling(Billing billing, Long id) {
		try {
			if (billingRepository.existsById(id) && billing.getId() == id) {

				Billing billingtoUpdate = billingRepository.findById(id).get();

				billingtoUpdate.setUpdatedAt(LocalDateTime.now());
				billingtoUpdate.setAmount(billing.getAmount());
				billingtoUpdate.setAmountPending(billing.getAmountPending());
				billingtoUpdate.setControlNumber(billing.getControlNumber());
				billingtoUpdate.setStatus(billing.getStatus());

				return billingRepository.saveAndFlush(billingtoUpdate);
			} else {

				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Billing updateBillingControlNumber(String billId, String controlNumber) {

		try {
			if (billingRepository.existsByBillingNumber(billId)) {

				Billing billingtoUpdate = billingRepository.findByBillingNumber(billId);

				billingtoUpdate.setUpdatedAt(LocalDateTime.now());
				billingtoUpdate.setIssuedDate(LocalDateTime.now());
				billingtoUpdate.setGepgStatus(1);
				billingtoUpdate.setStatus(BillingStatusEnum.PENDING);

				billingtoUpdate.setControlNumber(controlNumber);

				if (billingChargesRepository.existsByBillingId(billingtoUpdate.getId())) {

					List<BillingCharges> billingChargeslist = billingChargesRepository
							.findAllByBillingId(billingtoUpdate.getId());
					List<BillingCharges> bllingChargesupdates = new ArrayList<>();

					for (BillingCharges billingCharges : billingChargeslist) {

						billingCharges.setStatus(BillingStatusEnum.PENDING.toString());
						billingCharges.setUpdatedAt(LocalDateTime.now());

						bllingChargesupdates.add(billingCharges);

					}
					if (bllingChargesupdates != null) {
						billingChargesRepository.saveAll(bllingChargesupdates);

					}
				}

				Billing billingtoUpdated = billingRepository.saveAndFlush(billingtoUpdate);

				// verify if Bill is for application fee or Initial fee , don't send to erms
				// till payment are made
				if (licenseFeeStructureRepository.existsById(billingtoUpdated.getFeeId())
						&& (licenseFeeStructureRepository.findById(billingtoUpdated.getFeeId()).get()
								.getApplicableState().equals(ApplicableStateEnum.APPLICATION)
								|| licenseFeeStructureRepository.findById(billingtoUpdated.getFeeId()).get()
										.getApplicableState().equals(ApplicableStateEnum.INITIAL_PAYMENT))) {

				} else {

					// prepare invoice to send to ERMS

					InvoiceSubmissionDto invoiceDto = prepareInvoiceToErms(billingtoUpdated);
					if (invoiceDto != null) {
						// invoiceService.generateAndSendInvoice(invoiceDto); // This should be opened
						// on production
					}
				}
				return billingtoUpdated;

				//

			} else {

				return null;
			}
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			return null;
		}
	}

	@Override
	public Billing updateBillingPayment(String billId, String controlNumber, String paidAmount, String billAmount,
			String channel, String bankAccount, String gepgReceipt) {
		try {
			if (billingRepository.existsByBillingNumber(billId)) {

				Billing billingtoUpdate = billingRepository.findByBillingNumber(billId);

				billingtoUpdate.setUpdatedAt(LocalDateTime.now());
				billingtoUpdate.setPayDate(LocalDateTime.now());
				billingtoUpdate.setBankAccount(bankAccount);

				// update Receipt Number

				Random rnd = new Random();
				int receiptNumber = rnd.nextInt(99999999);

				billingtoUpdate.setReceiptNumber("REC-" + String.format("%08d", receiptNumber));
				if (Double.parseDouble(paidAmount) == billingtoUpdate.getAmountPending()) {
					billingtoUpdate.setAmountPending(0.0);

					billingtoUpdate.setStatus(BillingStatusEnum.PAID);

				} else {
					billingtoUpdate.setAmountPending(billingtoUpdate.getAmount() - Double.parseDouble(paidAmount));
					billingtoUpdate.setStatus(BillingStatusEnum.PENDING);
				}

				// billingtoUpdate.setControlNumber(controlNumber);

				// update Billing charges
				if (billingChargesRepository.existsByBillingId(billingtoUpdate.getId())) {

					List<BillingCharges> billingChargeslist = billingChargesRepository
							.findAllByBillingId(billingtoUpdate.getId());
					List<BillingCharges> bllingChargesupdates = new ArrayList<>();

					for (BillingCharges billingCharges : billingChargeslist) {

						billingCharges.setStatus(BillingStatusEnum.PAID.toString());
						billingCharges.setUpdatedAt(LocalDateTime.now());

						bllingChargesupdates.add(billingCharges);

					}
					if (bllingChargesupdates != null) {
						billingChargesRepository.saveAll(bllingChargesupdates);

					}
				}

				// update Billing Transactions

				BillingTransaction billingTransaction = new BillingTransaction();
				billingTransaction.setAmount(billingtoUpdate.getAmount());
				billingTransaction.setAmountPaid(Double.parseDouble(paidAmount));
				billingTransaction.setAmountPending(billingtoUpdate.getAmount() - Double.parseDouble(paidAmount));
				billingTransaction.setBillingId(billingtoUpdate.getId());
				billingTransaction.setChannel(channel);
				billingTransaction.setControlNumber(controlNumber);
				billingTransaction.setCreatedAt(LocalDateTime.now());
				billingTransaction.setCurrency(billingtoUpdate.getCurrency().getName());
				billingTransaction.setIssuedDate(LocalDateTime.now());

				saveBillingTransactions(billingTransaction);

				Billing billingUpdated = billingRepository.saveAndFlush(billingtoUpdate);
				if (billingUpdated != null) {
					// send alert to Internal process to start
					licenseTrackService.receivePaymentNotification(billingUpdated.getLicenceId(),
							billingUpdated.getAttachedTo().toString());

					// check if payment are associated with Application fee or initiatil fee , so
					// you can send invoice first

					if (licenseFeeStructureRepository.existsById(billingUpdated.getFeeId())
							&& (licenseFeeStructureRepository.findById(billingUpdated.getFeeId()).get()
									.getApplicableState().equals(ApplicableStateEnum.APPLICATION)
									|| licenseFeeStructureRepository.findById(billingUpdated.getFeeId()).get()
											.getApplicableState().equals(ApplicableStateEnum.INITIAL_PAYMENT))) {

						// send invoice

						// invoiceService.generateAndSendInvoice(prepareInvoiceToErms(billingUpdated));
						// stoped on test

						// send payment receipt now

						invoiceService.generateAndSendReceipt(prepareReceiptToErms(billingUpdated, gepgReceipt));

					} else {
						// send alert to ERMS to send receipt

						// call service to send now
						// invoiceService.generateAndSendReceipt(prepareReceiptToErms(billingUpdated,
						// gepgReceipt));// stoped on test
					}

				}

				return billingUpdated;
			} else {

				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public Boolean saveBillingTransactions(BillingTransaction billingTransactions) {
		try {
			billingTransactionRepository.saveAndFlush(billingTransactions);
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	@Override
	public Response<List<BillingRevenueDto>> getBillingRevenue() {
		Response<List<BillingRevenueDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"BILLING REVENUE RETRIEVED SUCCESSFULLY", null);
		try {

			List<BillingRevenueDto> BillingRevenueDtolist = new ArrayList<>();

			List<Billing> billinglist = billingRepository.findByActive(true);

			for (Billing billing : billinglist) {

				BillingRevenueDto billingRevenueDto = new BillingRevenueDto();

				billingRevenueDto.setBilling(billing);

				if (licenseRepository.existsByIdAndActive(billing.getLicenceId(), true)) {

					Licence license = licenseRepository.findByIdAndActive(billing.getLicenceId(), true);
					LicenceDto licenceDto = new LicenceDto();

					licenceDto.setId(license.getId());
//					licenceDto.setAppliedServices(license.getAppliedServices());
					if (license.getApplicantEntity() != null) {
						ApplicantDto applicantDto = new ApplicantDto();
						applicantDto.setId(license.getApplicantEntity().getId());
						applicantDto.setName(license.getApplicantEntity().getName());

						licenceDto.setApplicant(applicantDto);
					}

					if (license.getLicenseProduct() != null) {
						LicenceProductBDto licenceProductBDto = new LicenceProductBDto();
						licenceProductBDto.setCode(license.getLicenseProduct().getCode());
						licenceProductBDto.setId(license.getLicenseProduct().getId());
						licenceProductBDto.setName(license.getLicenseProduct().getName());

						licenceDto.setLicenceProduct(licenceProductBDto);
					}

					billingRevenueDto.setLicence(licenceDto);
				}
				if (billingChargesRepository.existsByBillingIdAndActive(billing.getId(), true)) {
					List<BillingCharges> billingChargeslist = billingChargesRepository.findByBilling(billing);

					billingRevenueDto.setFees(billingChargeslist);
				}

				BillingRevenueDtolist.add(billingRevenueDto);

			}

			if (BillingRevenueDtolist.isEmpty()) {

				response.setMessage("BILLING REVENUE NOT FOUND");
				return response;
			}

			response.setData(BillingRevenueDtolist);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");

		}
		return response;

	}

	@Override
	public Response<Billing> requestControlNumber(BillingControlNumberRequestDto billingControlNumberRequestDto) {
		Response<Billing> response = new Response<>(ResponseCode.SUCCESS, true,
				"BILLING REVENUE RETRIEVED SUCCESSFULLY", null);

		log.info("payload data received" + billingControlNumberRequestDto);
		// try {

		Billing billing = new Billing();

		billing.setAmount(billingControlNumberRequestDto.getAmount());
		billing.setAmountPending(billingControlNumberRequestDto.getAmount());

		if (licenseFeeStructureRepository.existsByIdInAndActive(billingControlNumberRequestDto.getFeeIds(), true)) {

			Double amountUSD = 0.0;

			log.info("Fee ids" + billingControlNumberRequestDto.getFeeIds());
			List<FeeStructure> licenceFeeStructures = licenseFeeStructureRepository
					.findByIdInAndActive(billingControlNumberRequestDto.getFeeIds(), true);

			log.info("Fee Structure" + licenceFeeStructures);

			for (FeeStructure fee : licenceFeeStructures) {

				amountUSD = amountUSD + fee.getFeeAmount();

			}

			billing.setAmountUSD(amountUSD);
		}

		billing.setRate(billingControlNumberRequestDto.getRate());

		Random rnd = new Random();
		int invoice = rnd.nextInt(999999);

		billing.setBillingNumber("TCRAHLMS" + String.format("%06d", invoice));
		billing.setCreatedAt(LocalDateTime.now());
		if (listOfValueRepository.existsByIdAndActive(billingControlNumberRequestDto.getCurrency(), true)) {
			billing.setCurrency(
					listOfValueRepository.findByIdAndActive(billingControlNumberRequestDto.getCurrency(), true));
		}
		LocalDateTime today = LocalDateTime.now(); // Today
		LocalDateTime expiredate = today.plusDays(30); // Plus 1 day
		billing.setExpireDate(expiredate);// add 30 days
		if (billingControlNumberRequestDto.getFeeIds() != null) {
			billing.setFeeId(billingControlNumberRequestDto.getFeeIds().get(0));
		}
		billing.setGepgStatus(0);
		// billing.setIssuedDate(issuedDate);// update this once control number has been
		// given
		billing.setLicenceId(billingControlNumberRequestDto.getLicenceId());

		billing.setStatus(BillingStatusEnum.BILLED);
		billing.setAttachedTo(billingControlNumberRequestDto.getBillable());

		Billing billingresponse = billingRepository.saveAndFlush(billing);

		// now save charges

		if (billingresponse != null && billingControlNumberRequestDto.getFeeIds() != null) {

			List<BillingCharges> billingChargeslist = new ArrayList<>();
			for (Long feeId : billingControlNumberRequestDto.getFeeIds()) {

				if (billingresponse != null && licenseFeeStructureRepository.existsById(feeId)) {
					FeeStructure licenseFeeStructure = licenseFeeStructureRepository.findById(feeId).get();
					// save billing charges now
					BillingCharges billingCharges = new BillingCharges();

					billingCharges.setStatus(billingresponse.getStatus().toString());
					billingCharges.setAmount(licenseFeeStructure.getFeeAmount());
					billingCharges.setBilling(billingRepository.getOne(billingresponse.getId()));

					billingCharges.setFeeType(licenseFeeStructure.getFeeType().getName());
					billingCharges.setFeeId(licenseFeeStructure.getId());
					billingCharges.setCreatedAt(LocalDateTime.now());

					billingChargeslist.add(billingCharges);

				}

			}
			if (billingChargeslist != null) {
				billingChargesRepository.saveAll(billingChargeslist);

			}

			// now call GEPG gate way but first construct request to GEPG
			if (licenseRepository.existsById(billingresponse.getLicenceId())) {

				Licence license = licenseRepository.findById(billingresponse.getLicenceId()).get();
				RequestForControlNumberGepgDto requestForControlNumberGepgDto = new RequestForControlNumberGepgDto();

				requestForControlNumberGepgDto.setAmount(billingresponse.getAmount());

				requestForControlNumberGepgDto.setBillapprby(approvedby);

				requestForControlNumberGepgDto.setBilleqamount(billingresponse.getAmount());
				requestForControlNumberGepgDto.setBillgenby(genby);
				requestForControlNumberGepgDto.setBillpayoption(option);
				// requestForControlNumberGepgDto.setGfscode(gscode);
				if (billingresponse.getGsfCode() != null) {
					requestForControlNumberGepgDto.setGfscode(billingresponse.getGsfCode());
				}
				requestForControlNumberGepgDto.setMiscamount("0.0");

				requestForControlNumberGepgDto.setName(name);
				requestForControlNumberGepgDto.setBillID(billingresponse.getBillingNumber());

				if (license.getApplicantEntity() != null) {

					requestForControlNumberGepgDto.setPayeremail(license.getApplicantEntity().getEmail());
					requestForControlNumberGepgDto.setPayercellnumber(license.getApplicantEntity().getPhone());
					requestForControlNumberGepgDto.setCompany(license.getApplicantEntity().getName());
					if (license.getApplicantEntity().getPhone().startsWith("0")) {
						String phoneNumber = license.getApplicantEntity().getPhone().replaceFirst("[0]", "255");
						requestForControlNumberGepgDto.setPayercellnumber(phoneNumber);

					}
					requestForControlNumberGepgDto.setPayerID(license.getApplicantEntity().getTinNo());
					requestForControlNumberGepgDto.setPayername(license.getApplicantEntity().getName());

				}
				requestForControlNumberGepgDto.setCurrency(billingresponse.getCurrency().getCode());
				if (listOfValueRepository.existsByIdAndActive(billingresponse.getCurrency().getId(), true)) {
					requestForControlNumberGepgDto.setCurrency(billingresponse.getCurrency().getCode());
				}

				requestForControlNumberGepgDto.setPeriod(period);
				requestForControlNumberGepgDto.setBilldescr(description);

				if (license.getLicenseProduct() != null && license.getLicenseProduct().getLicenseCategory() != null) {
					requestForControlNumberGepgDto
							.setBilldescr(license.getLicenseProduct().getLicenseCategory().getName());
					requestForControlNumberGepgDto
							.setService(license.getLicenseProduct().getLicenseCategory().getName());
				}
				processGePGControlNumberService.requestControlNumber(requestForControlNumberGepgDto);
			}

		}

		response.setData(billingresponse);

		// } catch (Exception e) {

		// log.info(e.getLocalizedMessage());
		// return null;
		// }

		return response;
	}

	@Override
	public Response<Billing> initiateBill(BillingControlNumberRequestDto billingControlNumberRequestDto) {
		Response<Billing> response = new Response<>(ResponseCode.SUCCESS, true,
				"BILLING REVENUE RETRIEVED SUCCESSFULLY", null);

		log.info("payload data received" + billingControlNumberRequestDto);
		// try {

		Billing billing = new Billing();

		// get current rate exchange
		ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();

		exchangeRequestDto.setAmount(billingControlNumberRequestDto.getAmount());
		Long currecyTo = 2L;
		Long currecyFrom = 1L;

		List<ListOfValueDto> values = listOfValuerService.getListOfListOfValueByType(ListOfValueTypeEnum.CURRENCY)
				.getData();

		for (ListOfValueDto value : values) {

			if (value.getCode().equalsIgnoreCase("TZS")) {

				currecyTo = value.getId();
			}

			if (value.getCode().equalsIgnoreCase("USD")) {

				currecyFrom = value.getId();

			}

		}

		Long usd = listOfValueRepository.findFirstByCodeAndActive("USD", true).getId();

		Long tzs = listOfValueRepository.findFirstByCodeAndActive("TZS", true).getId();
		exchangeRequestDto.setCurrecyFrom(usd);// USD
		exchangeRequestDto.setCurrecyTo(tzs);// TZS

		log.info("CURRENCY FROM =" + currecyFrom);

		log.info("CURRENCY TO =" + currecyTo);

		ExchangeResponseDto rate = exchangeRateService.exchangeUSDToTZS(exchangeRequestDto).getData();
		log.info("EXCHANGE BODY  =" + rate);
		if (rate != null && rate.getRate() != null) {
			billing.setBillRate(rate.getRate());

		}

		if (licenseFeeStructureRepository.existsByIdInAndActive(billingControlNumberRequestDto.getFeeIds(), true)) {

			Double amountUSD = 0.0;

			log.info("Fee ids" + billingControlNumberRequestDto.getFeeIds());
			List<FeeStructure> licenceFeeStructures = licenseFeeStructureRepository
					.findByIdInAndActive(billingControlNumberRequestDto.getFeeIds(), true);

			log.info("Fee Structure" + licenceFeeStructures);
			ListOfValue listOfValue = new ListOfValue();
			Long feeid = 0L;
			for (FeeStructure fee : licenceFeeStructures) {

				amountUSD = amountUSD + fee.getFeeAmount();
				if (fee.getFeeCurrency() != null) {
					listOfValue = fee.getFeeCurrency();
					feeid = fee.getId();

					billing.setGsfCode(fee.getCode());
					billing.setAccountCode(fee.getAccountCode());
				}

			}

			billing.setAmountUSD(amountUSD);
			billing.setAmount(amountUSD);
			billing.setAmountPending(amountUSD);
			billing.setCurrency(listOfValue);
			billing.setFeeId(feeid);

		}

		billing.setRate(1.0);

		Random rnd = new Random();
		int invoice = rnd.nextInt(999999);

		billing.setBillingNumber("TCRAHLMS" + String.format("%06d", invoice));
		billing.setCreatedAt(LocalDateTime.now());

		LocalDateTime today = LocalDateTime.now(); // Today
		LocalDateTime expiredate = today.plusDays(30); // Plus 1 day
		billing.setPayment_mode(PaymentMode);
		// billing.setExpireDate(expiredate);// add 30 days
		if (billingControlNumberRequestDto.getFeeIds() != null)

			billing.setGepgStatus(0);
		// billing.setIssuedDate(issuedDate);// update this once control number has been
		// given
		billing.setLicenceId(billingControlNumberRequestDto.getLicenceId());

		billing.setStatus(BillingStatusEnum.BILLED);
		billing.setAttachedTo(billingControlNumberRequestDto.getBillable());

		Billing billingresponse = billingRepository.saveAndFlush(billing);

		// now save charges

		if (billingresponse != null && billingControlNumberRequestDto.getFeeIds() != null) {

			List<BillingCharges> billingChargeslist = new ArrayList<>();
			for (Long feeId : billingControlNumberRequestDto.getFeeIds()) {

				if (billingresponse != null && licenseFeeStructureRepository.existsById(feeId)) {
					FeeStructure licenseFeeStructure = licenseFeeStructureRepository.findById(feeId).get();
					// save billing charges now
					BillingCharges billingCharges = new BillingCharges();

					billingCharges.setStatus(billingresponse.getStatus().toString());
					billingCharges.setAmount(licenseFeeStructure.getFeeAmount());
					billingCharges.setBilling(billingRepository.getOne(billingresponse.getId()));

					billingCharges.setFeeType(licenseFeeStructure.getFeeType().getName());
					billingCharges.setFeeId(licenseFeeStructure.getId());
					billingCharges.setCreatedAt(LocalDateTime.now());

					billingChargeslist.add(billingCharges);

				}

			}
			if (billingChargeslist != null) {
				billingChargesRepository.saveAll(billingChargeslist);

			}

		}

		response.setData(billingresponse);

		// } catch (Exception e) {

		// log.info(e.getLocalizedMessage());
		// return null;
		// }

		return response;
	}

	@Override
	public Response<Billing> updateBillAndRequestControlNumber(
			BillingControlNumberRequestDto billingControlNumberRequestDto) {
		Response<Billing> response = new Response<>(ResponseCode.SUCCESS, true,
				"BILLING REVENUE RETRIEVED SUCCESSFULLY", null);

		log.info("payload data received" + billingControlNumberRequestDto);
		// try {

		Billing billing = billingRepository.findByBillingNumber(billingControlNumberRequestDto.getInvoiceNumber());

		billing.setAmount(billingControlNumberRequestDto.getAmount());
		billing.setAmountPending(billingControlNumberRequestDto.getAmount());

		billing.setRate(billingControlNumberRequestDto.getRate());
		billing.setGepgStatus(2);// requested to generate control Number if it gets control number set to 1 , if
									// it fails retry
		// 0 is bill initiated

		if (listOfValueRepository.existsByIdAndActive(billingControlNumberRequestDto.getCurrency(), true)) {
			billing.setCurrency(
					listOfValueRepository.findByIdAndActive(billingControlNumberRequestDto.getCurrency(), true));
		}
		LocalDateTime today = LocalDateTime.now(); // Today
		LocalDateTime expiredate = today.plusDays(30); // Plus 30 day
		if (billing.getExpireDate() == null) {
			billing.setExpireDate(expiredate);// add 30 days
		}
		if (billingControlNumberRequestDto.getFeeIds() != null && billing.getFeeId() == 0)
			billing.setFeeId(billingControlNumberRequestDto.getFeeIds().get(0));

		if (billing.getLicenceId() == 0) {
			billing.setLicenceId(billingControlNumberRequestDto.getLicenceId());
		}

		Billing billingresponse = billingRepository.saveAndFlush(billing);

		// now save charges

		if (billingresponse != null && billingControlNumberRequestDto.getFeeIds() != null) {

			// now call GEPG gate way but first construct request to GEPG
			if (licenseRepository.existsById(billingresponse.getLicenceId())
					|| licenceeEntityRepository.existsById(billingresponse.getLicenceId())) {

				RequestForControlNumberGepgDto requestForControlNumberGepgDto = new RequestForControlNumberGepgDto();
				if (licenseRepository.existsByIdAndActive(billing.getLicenceId(), true)
						&& billing.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {
					Licence license = licenseRepository.findById(billingresponse.getLicenceId()).get();

					if (license.getApplicantEntity() != null) {

						requestForControlNumberGepgDto.setPayeremail(license.getApplicantEntity().getEmail());
						requestForControlNumberGepgDto.setPayercellnumber(license.getApplicantEntity().getPhone());
						requestForControlNumberGepgDto.setCompany(license.getApplicantEntity().getName());
						if (license.getApplicantEntity().getPhone().startsWith("0")) {
							String phoneNumber = license.getApplicantEntity().getPhone().replaceFirst("[0]", "255");
							requestForControlNumberGepgDto.setPayercellnumber(phoneNumber);

						}
						requestForControlNumberGepgDto.setPayerID(license.getApplicantEntity().getTinNo());
						requestForControlNumberGepgDto.setPayername(license.getApplicantEntity().getName());

						if (license.getLicenseProduct() != null
								&& license.getLicenseProduct().getLicenseCategory() != null) {

							requestForControlNumberGepgDto
									.setBilldescr(license.getLicenseProduct().getLicenseCategory().getName());
							requestForControlNumberGepgDto
									.setService(license.getLicenseProduct().getLicenseCategory().getName());
						}

					}

				}

				//

				if (licenceeEntityRepository.existsById(billing.getLicenceId())
						&& billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY)) {
					LicenceeEntity licenceeEntity = licenceeEntityRepository.findById(billing.getLicenceId()).get();

					if (billingChargesRepository.existsByBillingAndActive(billing, true)) {
						List<BillingCharges> billingChargeslist = billingChargesRepository.findByBilling(billing);

						requestForControlNumberGepgDto.setService(billingChargeslist.get(0).getFeeType());
						requestForControlNumberGepgDto.setBilldescr(billingChargeslist.get(0).getFeeType());

					}

					requestForControlNumberGepgDto.setCompany(licenceeEntity.getName());

					requestForControlNumberGepgDto.setPayercellnumber(licenceeEntity.getPhone());
					requestForControlNumberGepgDto.setPayeremail(licenceeEntity.getEmail());
					requestForControlNumberGepgDto.setPayerID(licenceeEntity.getBusinessLicenceNo());

					requestForControlNumberGepgDto.setPayername(licenceeEntity.getName());

				}
				if (entityApplicationRepository.existsById(billing.getLicenceId())
						&& billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY_APPLICATION)) {
					EntityApplication entityApplication = entityApplicationRepository.findById(billing.getLicenceId())
							.get();

					if (billingChargesRepository.existsByBillingAndActive(billing, true)) {
						List<BillingCharges> billingChargeslist = billingChargesRepository.findByBilling(billing);

						requestForControlNumberGepgDto.setService(billingChargeslist.get(0).getFeeType());
						requestForControlNumberGepgDto.setBilldescr(billingChargeslist.get(0).getFeeType());

					}
					if (entityApplication.getApplicantEntity() != null) {
						requestForControlNumberGepgDto.setCompany(entityApplication.getApplicantEntity().getName());

						requestForControlNumberGepgDto
								.setPayercellnumber(entityApplication.getApplicantEntity().getPhone());
						requestForControlNumberGepgDto.setPayeremail(entityApplication.getApplicantEntity().getEmail());
						requestForControlNumberGepgDto
								.setPayerID(entityApplication.getApplicantEntity().getBusinessLicenceNo());

						requestForControlNumberGepgDto.setPayername(entityApplication.getApplicantEntity().getName());
					}

				}

				requestForControlNumberGepgDto.setAmount(billingresponse.getAmount());

				requestForControlNumberGepgDto.setBillapprby(approvedby);
				requestForControlNumberGepgDto.setBilleqamount(billingresponse.getAmount());
				// get exchange rate and send equivalent amount
				if (billingresponse.getCurrency().getCode().equalsIgnoreCase("USD")) {
					// call exchange rate
					ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();

					ListOfValue listOfValue = listOfValueRepository.findFirstByCodeAndActive("TZS", true);

					exchangeRequestDto.setAmount(billingresponse.getAmount());// default amount
					exchangeRequestDto.setCurrecyFrom(billingresponse.getCurrency().getId());
					if (listOfValue != null) {
						exchangeRequestDto.setCurrecyTo(listOfValue.getId());
					}

					Response<ExchangeResponseDto> exchangeRate = exchangeRateService
							.exchangeUSDToTZS(exchangeRequestDto);

					if (exchangeRate.getData() != null) {

						requestForControlNumberGepgDto.setBilleqamount(exchangeRate.getData().getAmountTo());

					}

				}

				requestForControlNumberGepgDto.setBillgenby(genby);
				requestForControlNumberGepgDto.setBillpayoption(option);
				if (billingresponse.getPayment_mode() != 0) {
					requestForControlNumberGepgDto
							.setBillpayoption(Integer.toString(billingresponse.getPayment_mode()));
				}
				// requestForControlNumberGepgDto.setGfscode(gscode);
				if (billingresponse.getGsfCode() != null) {
					requestForControlNumberGepgDto.setGfscode(billingresponse.getGsfCode());
				}
				requestForControlNumberGepgDto.setMiscamount("0.0");
				requestForControlNumberGepgDto.setName(name);
				requestForControlNumberGepgDto.setBillID(billingresponse.getBillingNumber());

				requestForControlNumberGepgDto.setCurrency(billingresponse.getCurrency().getCode());
				if (listOfValueRepository.existsByIdAndActive(billingresponse.getCurrency().getId(), true)) {
					requestForControlNumberGepgDto.setCurrency(billingresponse.getCurrency().getCode());
				}

				requestForControlNumberGepgDto.setPeriod(period);
				requestForControlNumberGepgDto.setBilldescr(description);

				if (licenceeEntityRepository.existsById(billingresponse.getLicenceId())
						&& billingresponse.getAttachedTo().equals(BillingAttachedToEnum.ENTITY)
						&& billingChargesRepository.existsByBillingId(billingresponse.getId())) {

					List<BillingCharges> charges = billingChargesRepository.findByBilling(billingresponse);

					requestForControlNumberGepgDto.setBilldescr(charges.get(0).getFeeType());
					requestForControlNumberGepgDto.setService(charges.get(0).getFeeType());

				}
				processGePGControlNumberService.requestControlNumber(requestForControlNumberGepgDto);
			}

		}

		response.setData(billingresponse);

		// } catch (Exception e) {

		// log.info(e.getLocalizedMessage());
		// return null;
		// }

		return response;
	}

	@Override
	public List<LicenceBillingDto> getLicenseBillingByLicenseid(Long LicenseId, BillingAttachedToEnum billable) {
		List<LicenceBillingDto> data = new ArrayList<>();
		try {

			if (!billingRepository.existsByLicenceId(LicenseId)) {
				return data;
			}

			// retrieve all bills with these license ids
			List<Billing> billings = billingRepository.findByLicenceIdAndAttachedTo(LicenseId, billable);

			for (Billing billing : billings) {
				// Licence licence = licenseRepository.findById(billing.getLicenceId()).get();

				LicenceBillingDto dt = new LicenceBillingDto();
				dt.setAmount(billing.getAmount());
				dt.setBillingNumber(billing.getBillingNumber());
				dt.setControlNumber(billing.getControlNumber());
				dt.setCreatedAt(billing.getCreatedAt());
				dt.setCurrency(billing.getCurrency().getCode());
				dt.setCurrencyId(billing.getCurrency().getId());
				dt.setPaidAt(billing.getPayDate());
				dt.setStatus(billing.getStatus().toString());
				dt.setGepgFlag(billing.getGepgStatus());

				List<LicenceBillingChargesDto> charges = new ArrayList<>();

				billingChargesRepository.findByBilling(billing).forEach(charge -> {
					LicenceBillingChargesDto licenceBillingChargesDto = new LicenceBillingChargesDto();

					if (billing.getRate() != null || billing.getRate() != 0.0) {
						Double amount = billing.getRate() * charge.getAmount();
						licenceBillingChargesDto.setAmount(amount);
					}

					licenceBillingChargesDto.setCurrency(billing.getCurrency().getCode());
					licenceBillingChargesDto.setFeeType(charge.getFeeType());
					licenceBillingChargesDto.setStatus(charge.getStatus());

					charges.add(licenceBillingChargesDto);

				});
				dt.setCharges(charges);

				data.add(dt);
			}

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return data;

	}

	@Override
	public String getMoneyInWords(Double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<CollectionModel<EntityModel<BillingRevenueDto>>> getBillingRevenuePageable(String keyword,
			Pageable pageable, BillingStatusEnum status) {

		Response<CollectionModel<EntityModel<BillingRevenueDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"BILLING REVENUE RETRIEVED SUCCESSFULLY", null);
		try {

			// Page<Billing> pages = billingRepository.findAll(keyword, pageable);

			Page<BillingReading> pagesr = null;
			if (keyword.equalsIgnoreCase("All")) {
				if (status.equals(BillingStatusEnum.PAID)) {

					pagesr = billingReadingRepository.findAll(pageable);
				} else {
					pagesr = billingReadingRepository.findAllByStatus(status, pageable);
				}
			} else {
				if (status.equals(BillingStatusEnum.PAID)) {
					pagesr = billingReadingRepository.findAll(keyword, pageable);
				} else {
					pagesr = billingReadingRepository.findAll(keyword, status, pageable);
				}
			}

			// Page<BillingRevenueDto> dtos = pages.map(this::convertToBillingRevenueDto);
			Page<BillingRevenueDto> dtos = pagesr.map(this::convertToBillingRevenueDto);

			response.setData(pagedResourcesAssembler.toModel(dtos));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");

		}

		return response;
	}

	private BillingRevenueDto convertToBillingRevenueDto(BillingReading billingr) {
		Licence license = null;
		if (licenseRepository.existsById(billingr.getLicenceid())
				&& billingr.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {
			license = licenseRepository.findById(billingr.getLicenceid()).get();
		}

		BillingRevenueDto billingRevenueDto = new BillingRevenueDto();
		Billing biling = new Billing();
		biling.setActive(billingr.getActive());
		biling.setAmount(billingr.getAmount());
		biling.setAmountPending(billingr.getAmountPending());
		biling.setAmountUSD(billingr.getAmountUSD());
		biling.setBillingNumber(billingr.getBillingNumber());
		biling.setControlNumber(billingr.getControlNumber());
		biling.setCreatedAt(billingr.getCreatedAt());
		biling.setCurrency(billingr.getCurrency());
		biling.setExpireDate(billingr.getExpireDate());
		biling.setFeeId(billingr.getFeeId());
		biling.setGepgStatus(billingr.getGepgStatus());
		biling.setGsfCode(billingr.getGsfCode());
		biling.setId(billingr.getId());
		biling.setAttachedTo(billingr.getAttachedTo());

		biling.setIssuedDate(billingr.getIssuedDate());
		if (license != null) {
			biling.setLicenceId(license.getId());
		}
		biling.setPayDate(billingr.getPayDate());
		biling.setRate(billingr.getRate());
		biling.setStatus(billingr.getStatus());
		biling.setUpdatedAt(billingr.getUpdatedAt());
		biling.setUuid(billingr.getUuid());

		billingRevenueDto.setBilling(biling);
		billingRevenueDto.setBillable(billingr.getAttachedTo().toString());

		if (license != null && billingr.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {

			LicenceDto licenceDto = new LicenceDto();

			licenceDto.setId(license.getId());
//			licenceDto.setAppliedServices(license.getAppliedServices());
			if (license.getApplicantEntity() != null) {
				ApplicantDto applicantDto = new ApplicantDto();
				applicantDto.setId(license.getApplicantEntity().getId());
				applicantDto.setName(license.getApplicantEntity().getName());

				licenceDto.setApplicant(applicantDto);
			}

			if (license.getLicenseProduct() != null) {
				LicenceProductBDto licenceProductBDto = new LicenceProductBDto();
				licenceProductBDto.setCode(license.getLicenseProduct().getCode());
				licenceProductBDto.setId(license.getLicenseProduct().getId());
				licenceProductBDto.setName(license.getLicenseProduct().getName());

				licenceDto.setLicenceProduct(licenceProductBDto);
			}

			billingRevenueDto.setLicence(licenceDto);
		}
		if (billingChargesRepository.existsByBillingIdAndActive(billingr.getId(), true)) {
			List<BillingCharges> billingChargeslist = billingChargesRepository.findByIdAndActive(billingr.getId(),
					true);

			billingRevenueDto.setFees(billingChargeslist);
		}

		return billingRevenueDto;
	}

	public void expiredControlNumber() {

	}

	@Override
	public Response<EntityModel<Billing>> initiateBillFromTCRAOfficer(
			InitiateBillByInternalOfficerDto initiateBillByInternalOfficerDto) {
		Response<EntityModel<Billing>> response = new Response<>(ResponseCode.SUCCESS, true,
				"BILLING REVENUE RETRIEVED SUCCESSFULLY", null);

		log.info("payload data received from TCRA Bill Initiator Officer " + initiateBillByInternalOfficerDto);
		// try {

		Billing billing = new Billing();
		// collect all fee ids received
		List<Long> feeids = new ArrayList<>();
		Double amountUSD = 0.0;

		for (FeesDto fee : initiateBillByInternalOfficerDto.getFees()) {
			amountUSD = amountUSD + fee.getAmount();

			feeids.add(fee.getFee_id());

		}

		// get current rate exchange
		ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();

		exchangeRequestDto.setAmount(10.0);// This value has not meaning just added here to have a complete USD
		Long currecyTo = 2L;
		Long currecyFrom = 1L;

		List<ListOfValueDto> values = listOfValuerService.getListOfListOfValueByType(ListOfValueTypeEnum.CURRENCY)
				.getData();

		for (ListOfValueDto value : values) {

			if (value.getCode().equalsIgnoreCase("TZS")) {

				currecyTo = value.getId();
			}

			if (value.getCode().equalsIgnoreCase("USD")) {

				currecyFrom = value.getId();

			}

		}

		Long usd = listOfValueRepository.findFirstByCodeAndActive("USD", true).getId();

		Long tzs = listOfValueRepository.findFirstByCodeAndActive("TZS", true).getId();
		exchangeRequestDto.setCurrecyFrom(usd);// USD
		exchangeRequestDto.setCurrecyTo(tzs);// TZS

		log.info("CURRENCY FROM =" + currecyFrom);

		log.info("CURRENCY TO =" + currecyTo);

		Response<ExchangeResponseDto> rates = exchangeRateService.exchangeUSDToTZS(exchangeRequestDto);

		log.info("EXCHANGE RATE =" + rates);

		if (rates != null && rates.getData() != null) {
			billing.setBillRate(rates.getData().getRate());

		}

		if (licenseFeeStructureRepository.existsByIdInAndActive(feeids, true)) {

			log.info("Fee ids" + feeids);

			Long feeid = 0L;

			if (!initiateBillByInternalOfficerDto.getFees().isEmpty()
					&& initiateBillByInternalOfficerDto.getFees().get(0) != null)

				feeid = initiateBillByInternalOfficerDto.getFees().get(0).getFee_id();

			if (licenseFeeStructureRepository.existsByIdAndActive(feeid, true)) {
				FeeStructure licenseFeeStructure = licenseFeeStructureRepository.findByIdAndActive(feeid, true);
				billing.setGsfCode(licenseFeeStructure.getCode());
				billing.setAccountCode(licenseFeeStructure.getAccountCode());
			}

			billing.setAmountUSD(amountUSD);
			billing.setAmount(amountUSD);
			billing.setAmountPending(amountUSD);
			if (listOfValueRepository.existsByIdAndActive(initiateBillByInternalOfficerDto.getCurrency_id(), true)) {
				billing.setCurrency(listOfValueRepository
						.findByIdAndActive(initiateBillByInternalOfficerDto.getCurrency_id(), true));
			}
			billing.setFeeId(feeid);

		}

		billing.setRate(1.0);
		billing.setPayment_mode(initiateBillByInternalOfficerDto.getPayment_mode());

		Random rnd = new Random();
		int invoice = rnd.nextInt(999999);

		billing.setBillingNumber("TCRAHLMS" + String.format("%06d", invoice));
		billing.setCreatedAt(LocalDateTime.now());

		LocalDateTime today = LocalDateTime.now(); // Today
		LocalDateTime expiredate = today.plusDays(initiateBillByInternalOfficerDto.getExpire_days());

		billing.setExpireDate(expiredate);
		// billing.setExpireDate(expiredate);// add days specified from front end by
		// TCRA officer

		billing.setGepgStatus(0);
		// billing.setIssuedDate(issuedDate);// update this once control number has been
		// given

		billing.setLicenceId(initiateBillByInternalOfficerDto.getLicence_id());

		billing.setStatus(BillingStatusEnum.BILLED);
		billing.setAttachedTo(initiateBillByInternalOfficerDto.getBillable());

		Billing billingresponse = billingRepository.saveAndFlush(billing);

		// now save charges

		if (billingresponse != null && !initiateBillByInternalOfficerDto.getFees().isEmpty()) {

			List<BillingCharges> billingChargeslist = new ArrayList<>();
			for (FeesDto fee : initiateBillByInternalOfficerDto.getFees()) {

				if (billingresponse != null && licenseFeeStructureRepository.existsById(fee.getFee_id())) {
					FeeStructure licenseFeeStructure = licenseFeeStructureRepository.findById(fee.getFee_id()).get();
					// save billing charges now
					BillingCharges billingCharges = new BillingCharges();

					billingCharges.setStatus(billingresponse.getStatus().toString());
					billingCharges.setAmount(fee.getAmount());
					billingCharges.setBilling(billingRepository.getOne(billingresponse.getId()));

					billingCharges.setFeeType(licenseFeeStructure.getFeeType().getName());
					billingCharges.setFeeId(fee.getFee_id());
					billingCharges.setCreatedAt(LocalDateTime.now());

					billingChargeslist.add(billingCharges);

				}

			}
			if (billingChargeslist != null) {
				billingChargesRepository.saveAll(billingChargeslist);

			}

		}

		response.setData(billingServiceModelAssembler.toModel(billingresponse));

		// } catch (Exception e) {

		// log.info(e.getLocalizedMessage());
		// return null;
		// }

		return response;
	}

	public InvoiceSubmissionDto prepareInvoiceToErms(Billing billing) {

		InvoiceSubmissionDto invoiceSubmissionDto = new InvoiceSubmissionDto();

		invoiceSubmissionDto.setAmount(billing.getAmount());
		invoiceSubmissionDto.setAttachment("");
		invoiceSubmissionDto.setBillNumber(billing.getBillingNumber());
		invoiceSubmissionDto.setBranchCode(branchCode);
		ClientDto client = new ClientDto();
		if (licenseRepository.existsById(billing.getLicenceId())
				&& billing.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {
			Licence license = licenseRepository.findById(billing.getLicenceId()).get();
			if (license.getApplicantEntity() != null) {
				client.setAddress(license.getApplicantEntity().getPostalAddress());
				client.setClientType(ClientType.INDIVIDUAL);
				client.setCode(Long.toString(license.getApplicantEntity().getId()));
				client.setEmail(license.getApplicantEntity().getEmail());
				client.setName(license.getApplicantEntity().getName());
				client.setPhone(license.getApplicantEntity().getPhone());
			}

		}

		if (licenceeEntityRepository.existsById(billing.getLicenceId())
				&& billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY)) {
			LicenceeEntity licenceeEntity = licenceeEntityRepository.findById(billing.getLicenceId()).get();

			client.setAddress(licenceeEntity.getPostalAddress());
			client.setClientType(ClientType.INDIVIDUAL);
			client.setCode(Long.toString(licenceeEntity.getId()));
			client.setEmail(licenceeEntity.getEmail());
			client.setName(licenceeEntity.getName());
			client.setPhone(licenceeEntity.getPhone());

		}
		if (entityApplicationRepository.existsById(billing.getLicenceId())
				&& billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY_APPLICATION)) {
			EntityApplication entityApplication = entityApplicationRepository.findById(billing.getLicenceId()).get();

			if (entityApplication != null && entityApplication.getApplicantEntity() != null) {

				client.setAddress(entityApplication.getApplicantEntity().getPostalAddress());
				client.setClientType(ClientType.INDIVIDUAL);
				client.setCode(Long.toString(entityApplication.getApplicantEntity().getId()));
				client.setEmail(entityApplication.getApplicantEntity().getEmail());
				client.setName(entityApplication.getApplicantEntity().getName());
				client.setPhone(entityApplication.getApplicantEntity().getPhone());

			}
		}

		invoiceSubmissionDto.setClient(client);
		invoiceSubmissionDto.setControlNumber(billing.getControlNumber());
		invoiceSubmissionDto.setCurrencyCode(billing.getCurrency().getCode());
		invoiceSubmissionDto.setDepartmentCode(departmentCode);
		invoiceSubmissionDto.setDescription("Malipo tuyatarajiayo");

		List<EntryDto> entries = new ArrayList<>();
		EntryDto entrydebit = new EntryDto();

		entrydebit.setAccountCode(receivableAccountCode);
		entrydebit.setAmount(billing.getAmount());
		entrydebit.setBookSide(BookSide.DEBIT);
		entrydebit.setDescription("Debit Entry");

		entrydebit.setGfsCode(receivableGsfCode);

		entrydebit.setServiceEntry(false);
		entrydebit.setTaxEntry(false);

		entries.add(entrydebit);

		EntryDto entrycredit = new EntryDto();

		entrycredit.setAccountCode(billing.getAccountCode());
		entrycredit.setAmount(billing.getAmount());
		entrycredit.setBookSide(BookSide.CREDIT);
		entrycredit.setDescription("Credit Entry");
		if (billing.getGsfCode() != null && billing.getGsfCode().length() >= 8) {
			entrycredit.setGfsCode(billing.getGsfCode().substring(0, 8));
		}
		entrycredit.setServiceEntry(true);
		entrycredit.setTaxEntry(false);

		entries.add(entrycredit);

		invoiceSubmissionDto.setEntries(entries);

		invoiceSubmissionDto.setExchangeRate(billing.getBillRate());

		invoiceSubmissionDto.setGeneratedBy(genby);
		invoiceSubmissionDto.setOperationType(OperationType.SUBMISSION);
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		if (billing.getIssuedDate() != null) {

			invoiceSubmissionDto.setReceivableDate(simpleDateFormat
					.format(java.util.Date.from(billing.getIssuedDate().atZone(ZoneId.systemDefault()).toInstant())));
		}

		return invoiceSubmissionDto;
	}

	public ReceiptSubmissionDto prepareReceiptToErms(Billing billing, String gepgReceipt) {

		ReceiptSubmissionDto receiptDto = new ReceiptSubmissionDto();

		receiptDto.setAmount(billing.getAmount());
		receiptDto.setAttachment("");
		receiptDto.setBankAccountNo(billing.getBankAccount());
		receiptDto.setBranchCode(branchCode);
		receiptDto.setExchequerNo(gepgReceipt);

		ClientDto client = new ClientDto();
		if (licenseRepository.existsById(billing.getLicenceId())
				&& billing.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {
			Licence license = licenseRepository.findById(billing.getLicenceId()).get();
			if (license.getApplicantEntity() != null) {
				client.setAddress(license.getApplicantEntity().getPostalAddress());
				client.setClientType(ClientType.INDIVIDUAL);
				client.setCode(Long.toString(license.getApplicantEntity().getId()));
				client.setEmail(license.getApplicantEntity().getEmail());
				client.setName(license.getApplicantEntity().getName());
				client.setPhone(license.getApplicantEntity().getPhone());
			}

		}

		if (licenceeEntityRepository.existsById(billing.getLicenceId())
				&& billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY)) {
			LicenceeEntity licenceeEntity = licenceeEntityRepository.findById(billing.getLicenceId()).get();

			client.setAddress(licenceeEntity.getPostalAddress());
			client.setClientType(ClientType.INDIVIDUAL);
			client.setCode(Long.toString(licenceeEntity.getId()));
			client.setEmail(licenceeEntity.getEmail());
			client.setName(licenceeEntity.getName());
			client.setPhone(licenceeEntity.getPhone());

		}
		if (entityApplicationRepository.existsById(billing.getLicenceId())
				&& billing.getAttachedTo().equals(BillingAttachedToEnum.ENTITY_APPLICATION)) {
			EntityApplication entityApplication = entityApplicationRepository.findById(billing.getLicenceId()).get();

			if (entityApplication != null && entityApplication.getApplicantEntity() != null) {

				client.setAddress(entityApplication.getApplicantEntity().getPostalAddress());
				client.setClientType(ClientType.INDIVIDUAL);
				client.setCode(Long.toString(entityApplication.getApplicantEntity().getId()));
				client.setEmail(entityApplication.getApplicantEntity().getEmail());
				client.setName(entityApplication.getApplicantEntity().getName());
				client.setPhone(entityApplication.getApplicantEntity().getPhone());

			}
		}
		receiptDto.setClient(client);
		receiptDto.setControlNumber(billing.getControlNumber());
		receiptDto.setCurrencyCode(billing.getCurrency().getCode());
		receiptDto.setDepartmentCode(departmentCode);
		receiptDto.setDescription("Malipo Yamefanyika");
		receiptDto.setBillNumber(billing.getBillingNumber());
		receiptDto.setReferenceNumber(billing.getControlNumber());

		List<EntryDto> entries = new ArrayList<>();
		EntryDto entrydebit = new EntryDto();
		// use Bank No to get account code and Gsf code
		if (billing.getBankAccount() != null
				&& ermsBankAccountsRepository.existsByBankAccountAndActive(billing.getBankAccount(), true)) {
			ErmsBankAccounts ermsBankAccounts = ermsBankAccountsRepository
					.findFirstByBankAccountAndActive(billing.getBankAccount(), true);

			entrydebit.setAccountCode(ermsBankAccounts.getAccountCode());
			entrydebit.setGfsCode(ermsBankAccounts.getGsfCode());
		}

		if (billing.getBankAccount() == null
				|| !ermsBankAccountsRepository.existsByBankAccountAndActive(billing.getBankAccount(), true)) {
			if (billing.getCurrency().getCode().equalsIgnoreCase("TZS")) {
				receiptDto.setBankAccountNo("22510031438");
				entrydebit.setAccountCode("NMB01");// user NMB ACOOUNT FOT TESTING SIMULATION
				entrydebit.setGfsCode("62122103");
			} else {

				receiptDto.setBankAccountNo("22510031439");
				entrydebit.setAccountCode("NMB02");// user NMB ACOOUNT FOT TESTING SIMULATION
				entrydebit.setGfsCode("62122103");
			}
		}
		entrydebit.setAmount(billing.getAmount());
		entrydebit.setBookSide(BookSide.DEBIT);
		entrydebit.setDescription("Debit Entry");

		entrydebit.setServiceEntry(false);
		entrydebit.setTaxEntry(false);

		entries.add(entrydebit);

		EntryDto entrycredit = new EntryDto();

		entrycredit.setAccountCode(receivableAccountCode);
		entrycredit.setAmount(billing.getAmount());
		entrycredit.setBookSide(BookSide.CREDIT);
		entrycredit.setDescription("Credit Entry");

		entrycredit.setGfsCode(receivableGsfCode);

		entrycredit.setServiceEntry(true);
		entrycredit.setTaxEntry(false);

		entries.add(entrycredit);

		receiptDto.setEntries(entries);

		receiptDto.setExchangeRate(billing.getBillRate());

		receiptDto.setGeneratedBy(genby);

		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		if (billing.getPayDate() != null) {

			receiptDto.setReceiptDate(simpleDateFormat
					.format(java.util.Date.from(billing.getPayDate().atZone(ZoneId.systemDefault()).toInstant())));
		}

		receiptDto.setTitle("Receipt");

		return receiptDto;

	}

	public InvoiceCancelDto prepareInvoiceCancel(Billing billing) {

		InvoiceCancelDto receiptDto = new InvoiceCancelDto();

		receiptDto.setBranchCode(branchCode);

		receiptDto.setDepartmentCode(departmentCode);

		receiptDto.setBillNumber(billing.getBillingNumber());
		receiptDto.setReferenceNumber(billing.getControlNumber());

		List<EntryDto> invoiceEntries = new ArrayList<>();
		EntryDto entrydebit = new EntryDto();
		// use Bank No to get account code and Gsf code
		if (billing.getBankAccount() != null
				&& ermsBankAccountsRepository.existsByBankAccountAndActive(billing.getBankAccount(), true)) {
			ErmsBankAccounts ermsBankAccounts = ermsBankAccountsRepository
					.findFirstByBankAccountAndActive(billing.getBankAccount(), true);

			entrydebit.setAccountCode(ermsBankAccounts.getAccountCode());
			entrydebit.setGfsCode(ermsBankAccounts.getGsfCode());
		}
		entrydebit.setAmount(billing.getAmount());
		entrydebit.setBookSide(BookSide.DEBIT);
		entrydebit.setDescription("Debit Entry");

		entrydebit.setServiceEntry(false);
		entrydebit.setTaxEntry(false);

		invoiceEntries.add(entrydebit);

		EntryDto entrycredit = new EntryDto();

		entrycredit.setAccountCode(receivableAccountCode);
		entrycredit.setAmount(billing.getAmount());
		entrycredit.setBookSide(BookSide.CREDIT);
		entrycredit.setDescription("Credit Entry");

		entrycredit.setGfsCode(receivableGsfCode);

		entrycredit.setServiceEntry(true);
		entrycredit.setTaxEntry(false);

		invoiceEntries.add(entrycredit);

		receiptDto.setInvoiceEntries(invoiceEntries);

		// receipt entries
		List<EntryDto> receiptEntries = new ArrayList<>();

		receiptDto.setReceiptEntries(receiptEntries);

		return receiptDto;

	}

	@Override
	public Response<EntityModel<Billing>> CancelBill(Long BillId) {

		Response<EntityModel<Billing>> response = new Response<>(ResponseCode.SUCCESS, true,
				"BILLING REVENUE CANCELLED SUCCESSFULLY", null);

		if (billingRepository.existsByIdAndStatus(BillId, BillingStatusEnum.PENDING)) {

			Billing billing = billingRepository.findByIdAndStatus(BillId, BillingStatusEnum.PENDING);

			billing.setUpdatedAt(LocalDateTime.now());
			billing.setStatus(BillingStatusEnum.BILLCANCELLED);

			Billing billingUpdated = billingRepository.saveAndFlush(billing);

			// inform ERMS now

			try {
				invoiceService.generateAndSendInvoiceCancel(prepareInvoiceCancel(billingUpdated));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			response.setData(billingServiceModelAssembler.toModel(billingUpdated));

		} else {

			response.setCode(ResponseCode.NO_DATA_CHANGED);
			response.setStatus(false);
			response.setMessage("BILLING WAS NOT UPDATED");

		}

		return response;
	}

}
