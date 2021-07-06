package tz.go.tcra.lims.payment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.feestructure.repository.FeeStructureRepository;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceExpireDto;
import tz.go.tcra.lims.intergration.bills.service.InvoiceService;
import tz.go.tcra.lims.licence.repository.LicenceRepository;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.licencee.repository.EntityApplicationRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityRepository;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.miscellaneous.enums.BillingStatusEnum;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.payment.dto.ExchangeRequestDto;
import tz.go.tcra.lims.payment.dto.ExchangeResponseDto;
import tz.go.tcra.lims.payment.dto.RequestForControlNumberDto;
import tz.go.tcra.lims.payment.dto.RequestForControlNumberGepgDto;
import tz.go.tcra.lims.payment.entity.Billing;
import tz.go.tcra.lims.payment.entity.BillingCharges;
import tz.go.tcra.lims.payment.repository.BillingChargesRepository;
import tz.go.tcra.lims.payment.repository.BillingRepository;
import tz.go.tcra.lims.utils.Response;

@Slf4j
@Service
public class ProcessGePGControlNumberServiceImpl implements ProcessGePGControlNumberService {

	@Autowired
	private LicenceRepository licenseRepository;

	@Autowired
	private LicenceeEntityRepository licenceeEntityRepository;

	@Autowired
	private BillingService billingService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private ExchangeRateService exchangeRateService;

	@Autowired

	private EntityApplicationRepository entityApplicationRepository;

	@Autowired
	private BillingRepository billingRepository;

	@Autowired
	private BillingChargesRepository billingChargesRepository;

	@Autowired
	private FeeStructureRepository licenseFeeStructureRepository;

	@Autowired
	private ListOfValueRepository listOfValueRepository;

	@Value("${lims.payment.billapprovedby}")
	private String approvedby;

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

	@Value("${lims.payment.callbackUrl}")
	private String callbackUrl;

	@Value("${lims.payment.igatewayUrl}")
	private String gateWayUrl;

	@Value("${lims.payment.igatewayKey}")
	private String gateWayKey;

	@Override
	public RequestForControlNumberGepgDto createRequestForControlNumber(
			RequestForControlNumberDto requestForControlNumberDto) {

		try {

			if (saveBill(requestForControlNumberDto) == true) {

				LocalDateTime today = LocalDateTime.now(); // Today
				LocalDateTime tomorrow = today.plusDays(30); // Plus 1 day
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // yyyy-MM-dd'T'HH:mm:ss

				String billDate = today.format(formatter);
				String billExpDate = tomorrow.format(formatter);

				if (licenseRepository.existsByIdAndActive(requestForControlNumberDto.getLicenseId(), true)) {

					Licence license = licenseRepository.findByIdAndActive(requestForControlNumberDto.getLicenseId(),
							true);

					if (license.getApplicantEntity() != null) {

						RequestForControlNumberGepgDto requestForControlNumberGepgDto = new RequestForControlNumberGepgDto();

						if (requestForControlNumberDto.getAmount() != null) {
							requestForControlNumberGepgDto.setAmount(requestForControlNumberDto.getAmount());
							requestForControlNumberGepgDto.setBilleqamount(requestForControlNumberDto.getAmount());
						}
						requestForControlNumberGepgDto.setBillapprby(approvedby);
						requestForControlNumberGepgDto.setBilldescr(requestForControlNumberDto.getPaymentPurpose());

						requestForControlNumberGepgDto.setBillexpdate(billExpDate);
						requestForControlNumberGepgDto.setBillgenby(genby);
						requestForControlNumberGepgDto.setBillID(requestForControlNumberDto.getLicenseId().toString());// for
																														// now
																														// use
																														// licenceid
						requestForControlNumberGepgDto.setBillpayoption(option);
						requestForControlNumberGepgDto.setCompany(license.getApplicantEntity().getName());
						if (listOfValueRepository.existsByIdAndActive(requestForControlNumberDto.getCurrency(), true)) {
							requestForControlNumberGepgDto.setCurrency(listOfValueRepository
									.findByIdAndActive(requestForControlNumberDto.getCurrency(), true).getCode());
						}
						// requestForControlNumberGepgDto.setGfscode(gscode);
						if (requestForControlNumberDto != null) {
							// requestForControlNumberGepgDto.setGfscode(requestForControlNumberDto.getg);
						}
						requestForControlNumberGepgDto.setMiscamount("0.0");
						requestForControlNumberGepgDto.setName(name);
						requestForControlNumberGepgDto.setPayercellnumber(license.getApplicantEntity().getPhone());
						requestForControlNumberGepgDto.setPayeremail(license.getApplicantEntity().getEmail());
						requestForControlNumberGepgDto.setPayerID(license.getApplicantEntity().getBusinessLicenceNo());

						requestForControlNumberGepgDto.setPayername(license.getApplicantEntity().getName());
						requestForControlNumberGepgDto.setBillgendate(billDate);

						requestForControlNumberGepgDto.setUrl("URL");
						requestForControlNumberGepgDto.setKey("KEY");

						requestForControlNumberGepgDto.setPeriod(period);
						if (license.getLicenseProduct() != null
								&& license.getLicenseProduct().getLicenseCategory() != null) {
							requestForControlNumberGepgDto
									.setService(license.getLicenseProduct().getLicenseCategory().getName());
						}

						return requestForControlNumberGepgDto;
					} else {
						return null;
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public Boolean saveBill(RequestForControlNumberDto requestForControlNumberDto) {

		Billing billing = new Billing();
		if (licenseRepository.existsByIdAndActive(requestForControlNumberDto.getLicenseId(), true)) {

			Licence license = licenseRepository.findByIdAndActive(requestForControlNumberDto.getLicenseId(), true);

			billing.setAmount(requestForControlNumberDto.getAmount());
			billing.setAmountPending(requestForControlNumberDto.getAmount());

			if (license.getApplicantEntity() != null && license.getApplicantEntity().getBusinessLicenceNo() != null) {

				Random rnd = new Random();
				int invoice = rnd.nextInt(999999);

				billing.setBillingNumber("TCRAH" + String.format("%06d", invoice));// license.getLicenceeEntity().getBusinessLicenceNo());

				billing.setFeeId(license.getId());
			}
			billing.setCreatedAt(LocalDateTime.now());
			if (listOfValueRepository.existsByIdAndActive(requestForControlNumberDto.getCurrency(), true)) {
				billing.setCurrency(
						listOfValueRepository.findByIdAndActive(requestForControlNumberDto.getCurrency(), true));
			}

			billing.setLicenceId(requestForControlNumberDto.getLicenseId());
			billing.setRate(requestForControlNumberDto.getRate());
			billing.setStatus(BillingStatusEnum.BILLED);

			Billing billingresponse = billingRepository.saveAndFlush(billing);

			if (billingresponse != null && requestForControlNumberDto.getFeeIds() != null) {

				List<BillingCharges> billingChargeslist = new ArrayList<>();
				for (Long feeId : requestForControlNumberDto.getFeeIds()) {

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

				return true;
			} else {
				return false;
			}

		} else {

			return false;
		}

	}

	@Override
	public Boolean requestControlNumber(RequestForControlNumberGepgDto requestForControlNumberGepgDto) {

		Random rnd = new Random();
		int invoice = rnd.nextInt(999999);

		// requestForControlNumberGepgDto.setBillID("TCRAHLMS" + String.format("%06d",
		// invoice));

		if (requestForControlNumberGepgDto.getGfscode() == null) {
			// requestForControlNumberGepgDto.setGfscode(gscode);
		}
		if (requestForControlNumberGepgDto.getBillpayoption() == null) {
			requestForControlNumberGepgDto.setBillpayoption(option);
		}

		String reqtogepg = controlNumberRequestMaker(requestForControlNumberGepgDto);

		if (reqtogepg != null) {
			try {

				// String encodedString = new String(Base64.encodeBase64(reqtogepg.getBytes()));

				String ibraString = "<mamlakapg>\r\n" + "    <appinfo>\r\n"
						+ "        <key>Fwhwi@Qa2020PjcteCwell12!aTcr</key>\r\n" + "        <name>OTAS</name>\r\n"
						+ "        <url>http://10.200.222.95:8998/OTAS-Intergration/v1/</url>\r\n"
						+ "    </appinfo>\r\n" + "    <billinfo>\r\n" + "        <service>Network devices</service>\r\n"
						+ "        <period>Annual</period>\r\n" + "        <company>VODACOM TANZANIA PLC</company>\r\n"
						+ "        <billID>TCRAH1772021</billID>\r\n" + "        <amount>9200000</amount>\r\n"
						+ "        <miscamount>0</miscamount>\r\n"
						+ "        <payerID>VODACOM TANZANIA PLC</payerID>\r\n"
						+ "        <payername>VODACOM TANZANIA PLC</payername>\r\n"
						+ "        <billdescr>LicenseFee against Refno: VSCA/2021/0010</billdescr>\r\n"
						+ "        <billgendate>2021-01-28T16:05:12</billgendate>\r\n"
						+ "        <billexpdate>2021-02-27T16:05:12</billexpdate>\r\n"
						+ "        <billgenby>VODACOM TANZANIA PLC</billgenby>\r\n"
						+ "        <billapprby>VODACOM TANZANIA plc</billapprby>\r\n"
						+ "        <payercellnumber>255623606170</payercellnumber>\r\n"
						+ "        <payeremail>supplierdatbase08@gmail.com</payeremail>\r\n"
						+ "        <currency>TZS</currency>\r\n" + "        <billeqamount>9200000</billeqamount>\r\n"
						+ "        <RemFlag>true</RemFlag>\r\n" + "        <billpayoption>1</billpayoption>\r\n"
						+ "        <gfscode>140101</gfscode>\r\n" + "    </billinfo>\r\n" + "</mamlakapg>";

				// String encodedString = Base64.encodeBase64(ibraString.getBytes()).toString();

				Base64.Encoder encoder = Base64.getEncoder();
				// Encoding The Object(Payload)
				String encodedString = encoder.encodeToString(reqtogepg.getBytes());

				// String gateWayUrl = "http://10.200.222.64/receiver.php";

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_XML);

				RestTemplate restTemplate = new RestTemplate();
				HttpEntity<String> request = new HttpEntity<String>(encodedString, headers);
				HttpEntity<String> response = restTemplate.exchange(gateWayUrl, HttpMethod.POST, request, String.class);

				log.info(" Request from me :" + reqtogepg);

				// log.info(" Request from me :" + reqtogepg);

				log.info(" Response from remote :" + response.getBody());

				return true;
			} catch (Exception e) {
				log.info(e.getMessage());
				return false;
			}
		}

		return false;
	}

	public String controlNumberRequestMaker(RequestForControlNumberGepgDto requestRaw) {
		LocalDateTime today = LocalDateTime.now(); // Today
		LocalDateTime tomorrow = today.plusDays(30); // Plus 1 day
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // yyyy-MM-dd'T'HH:mm:ss

		String billDate = today.format(formatter);
		String billExpDate = tomorrow.format(formatter);
		if (requestRaw.getBillgendate() == null) {
			requestRaw.setBillgendate(billDate);
		}
		if (requestRaw.getBillexpdate() == null) {
			requestRaw.setBillexpdate(billExpDate);
		}

		requestRaw.setUrl(callbackUrl);//
		// demo callback URL

		// http://10.200.222.95:8998/OTAS-Intergration/v1/

		// requestRaw.setUrl("http://10.200.222.95:8998/OTAS-Intergration/v1/");
		// requestRaw.setKey("Fwhwi@Qa2020PjcteCwell12!aTcr");
		// requestRaw.setKey("c613d4a344c6fbe4622f676ab3b630a60ef16f54862f1661673bd17cb9174178");
		requestRaw.setKey(gateWayKey);
		requestRaw.setName(name);
		// requestRaw.setBillID("TCRAH15632021");

		String request = "<mamlakapg>\r\n" + "    <appinfo>\r\n" + " <key>" + requestRaw.getKey() + "</key>\r\n"
				+ "        <name>" + requestRaw.getName() + "</name>\r\n" + "        <url>" + requestRaw.getUrl()
				+ "</url>\r\n" + "    </appinfo>\r\n" + "    <billinfo>\r\n" + "        <service>"
				+ requestRaw.getService() + "</service>\r\n" + "        <period>" + requestRaw.getPeriod()
				+ "</period>\r\n" + "        <company>" + requestRaw.getCompany() + "</company>\r\n"
				+ "        <billID>" + requestRaw.getBillID() + "</billID>\r\n" + "        <amount>"
				+ requestRaw.getAmount() + "</amount>\r\n" + "        <miscamount>" + requestRaw.getMiscamount()
				+ "</miscamount>\r\n" + "        <payerID>" + requestRaw.getPayerID() + "</payerID>\r\n"
				+ "        <payername>" + requestRaw.getPayername() + "</payername>\r\n" + "        <billdescr>"
				+ requestRaw.getBilldescr() + "</billdescr>\r\n" + "        <billgendate>" + requestRaw.getBillgendate()
				+ "</billgendate>\r\n" + "        <billexpdate>" + requestRaw.getBillexpdate() + "</billexpdate>\r\n"
				+ "        <billgenby>" + requestRaw.getBillgenby() + "</billgenby>\r\n" + "        <billapprby>"
				+ requestRaw.getBillapprby() + "</billapprby>\r\n" + "        <payercellnumber>"
				+ requestRaw.getPayercellnumber() + "</payercellnumber>\r\n" + "        <payeremail>"
				+ requestRaw.getPayeremail() + "</payeremail>\r\n" + "        <currency>" + requestRaw.getCurrency()
				+ "</currency>\r\n" + "        <billeqamount>" + requestRaw.getBilleqamount() + "</billeqamount>\r\n"
				+ "        <billpayoption>" + requestRaw.getBillpayoption() + "</billpayoption>\r\n"
				+ "        <gfscode>" + requestRaw.getGfscode() + "</gfscode>\r\n" + "    </billinfo>\r\n"
				+ "</mamlakapg>";

		return request;
	}

	@Scheduled(fixedDelay = 600000) // every 10 minute
	public Boolean reGenerateControlNumber() {

		try {
			LocalDateTime today = LocalDateTime.now(); // Today
			LocalDateTime tomorrow = today.plusDays(30); // Plus 1 day
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // yyyy-MM-dd'T'HH:mm:ss

			String billDate = today.format(formatter);
			String billExpDate = tomorrow.format(formatter);
			String controllNumber = null;

			List<Billing> billinglist = billingRepository.findByGepgStatusAndControlNumberAndActive(2, controllNumber,
					true);

			for (Billing billing : billinglist) {

				RequestForControlNumberGepgDto requestForControlNumberGepgDto = new RequestForControlNumberGepgDto();

				if (licenseRepository.existsByIdAndActive(billing.getLicenceId(), true)
						&& billing.getAttachedTo().equals(BillingAttachedToEnum.LICENCE)) {

					Licence license = licenseRepository.findByIdAndActive(billing.getLicenceId(), true);

					if (license.getLicenseProduct() != null
							&& license.getLicenseProduct().getLicenseCategory() != null) {
						requestForControlNumberGepgDto
								.setService(license.getLicenseProduct().getLicenseCategory().getName());
						requestForControlNumberGepgDto
								.setBilldescr(license.getLicenseProduct().getLicenseCategory().getName());
					}

					requestForControlNumberGepgDto.setCompany(license.getApplicantEntity().getName());

					requestForControlNumberGepgDto.setPayercellnumber(license.getApplicantEntity().getPhone());
					requestForControlNumberGepgDto.setPayeremail(license.getApplicantEntity().getEmail());
					requestForControlNumberGepgDto.setPayerID(license.getApplicantEntity().getBusinessLicenceNo());

					requestForControlNumberGepgDto.setPayername(license.getApplicantEntity().getName());

				}
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

				if (billing.getAmount() != null) {
					requestForControlNumberGepgDto.setAmount(billing.getAmount());
					requestForControlNumberGepgDto.setBilleqamount(billing.getAmount());

					// get exchange rate and send equivalent amount
					if (billing.getCurrency().getCode().equalsIgnoreCase("USD")) {
						// call exchange rate
						ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();

						ListOfValue listOfValue = listOfValueRepository.findFirstByCodeAndActive("TZS", true);

						exchangeRequestDto.setAmount(billing.getAmount());// default amount
						exchangeRequestDto.setCurrecyFrom(billing.getCurrency().getId());
						if (listOfValue != null) {
							exchangeRequestDto.setCurrecyTo(listOfValue.getId());
						}

						Response<ExchangeResponseDto> exchangeRate = exchangeRateService
								.exchangeUSDToTZS(exchangeRequestDto);

						if (exchangeRate.getData() != null) {

							requestForControlNumberGepgDto.setBilleqamount(exchangeRate.getData().getAmountTo());

						}

					}
				}
				requestForControlNumberGepgDto.setBillapprby(approvedby);

				requestForControlNumberGepgDto.setBillexpdate(billExpDate);
				if (billing.getExpireDate() != null) {
					requestForControlNumberGepgDto.setBillexpdate(billing.getExpireDate().format(formatter));
				}

				requestForControlNumberGepgDto.setBillgenby(genby);
				requestForControlNumberGepgDto.setBillID(billing.getBillingNumber().toString());// for
				requestForControlNumberGepgDto.setBillpayoption(option); // now
				// use
				if (billing.getPayment_mode() != 0) {
					requestForControlNumberGepgDto.setBillpayoption(Integer.toString(billing.getPayment_mode()));
				}

				requestForControlNumberGepgDto.setCurrency(billing.getCurrency().getCode());
				requestForControlNumberGepgDto.setGfscode(gscode);
				if (billing.getGsfCode() != null) {

					requestForControlNumberGepgDto.setGfscode(billing.getGsfCode());
				}
				requestForControlNumberGepgDto.setMiscamount("0.0");
				requestForControlNumberGepgDto.setName(name);
				if (billing.getGsfCode() != null) {
					requestForControlNumberGepgDto.setGfscode(billing.getGsfCode());
				}

				requestForControlNumberGepgDto.setBillgendate(billDate);

				requestForControlNumberGepgDto.setUrl("URL");
				requestForControlNumberGepgDto.setKey("KEY");

				requestForControlNumberGepgDto.setPeriod(period);

				requestControlNumber(requestForControlNumberGepgDto);

				log.info("Requested control number for " + requestForControlNumberGepgDto);

			}

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Scheduled(fixedDelay = 10800000) // every 3 hrs
	public boolean expireControlNumber() {
		log.info("EXECUTING CONTROL NUMBER EXPIRED IN EVERY 3 HRS CHECK ");
		List<String> bills = new ArrayList<>();
		boolean response = false;
		List<Billing> billings = billingRepository.findByStatusAndActiveAndExpireDateBefore(BillingStatusEnum.PENDING,
				true, LocalDateTime.now());
		List<Billing> billingtoUpdate = new ArrayList<>();
		for (Billing billing : billings) {

			billing.setStatus(BillingStatusEnum.NOTPAID);
			billing.setUpdatedAt(LocalDateTime.now());
			bills.add(billing.getBillingNumber());
			billingtoUpdate.add(billing);

		}

		if (!billingtoUpdate.isEmpty()) {

			billingRepository.saveAll(billingtoUpdate);

			// send notification to ERMS on expired Invoice
			InvoiceExpireDto invoiceExpireDto = new InvoiceExpireDto();
			invoiceExpireDto.setBills(bills);

			try {
				// invoiceService.generateAndSendInvoiceExpired(invoiceExpireDto); // commented
				// on test
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response = true;
		}

		return response;
	}

	// Update Exchange rate to bill rate
	@Scheduled(fixedDelay = 600000) // every 10min
	public boolean updateExchangeRate() {
		log.info("EXECUTING CONTROL NUMBER EXPIRED IN EVERY 3 HRS CHECK ");
		List<String> bills = new ArrayList<>();
		boolean response = false;
		// List<Billing> billings =
		// billingRepository.findByStatusAndActiveAndExpireDateBefore(BillingStatusEnum.PENDING,
		// true, LocalDateTime.now());
		Double rate = null;

		List<Billing> billings = billingRepository.findByActiveAndBillRate(true, rate);
		List<Billing> billingtoUpdate = new ArrayList<>();
		for (Billing billing : billings) {

			// get current rate exchange
			ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();

			exchangeRequestDto.setAmount(billing.getAmount());// This value has not meaning just added here to have a
																// complete USD

			Long usd = listOfValueRepository.findFirstByCodeAndActive("USD", true).getId();

			Long tzs = listOfValueRepository.findFirstByCodeAndActive("TZS", true).getId();
			exchangeRequestDto.setCurrecyFrom(usd);// USD
			exchangeRequestDto.setCurrecyTo(tzs);// TZS

			Response<ExchangeResponseDto> rates = exchangeRateService.exchangeUSDToTZS(exchangeRequestDto);

			log.info("EXCHANGE RATE =" + rates);

			if (rates != null && rates.getData() != null) {
				billing.setBillRate(rates.getData().getRate());

				billingtoUpdate.add(billing);

			}

		}

		if (!billingtoUpdate.isEmpty()) {

			billingRepository.saveAll(billingtoUpdate);

			response = true;
		}

		return response;
	}

}
