package tz.go.tcra.lims.intergration.bills.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceCancelDto;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceExpireDto;
import tz.go.tcra.lims.intergration.bills.dto.InvoiceSubmissionDto;
import tz.go.tcra.lims.intergration.bills.dto.ReceiptCancelDto;
import tz.go.tcra.lims.intergration.bills.dto.ReceiptSubmissionDto;
import tz.go.tcra.lims.intergration.bills.entity.CancelInvoice;
import tz.go.tcra.lims.intergration.bills.entity.CancelReceipt;
import tz.go.tcra.lims.intergration.bills.entity.ExpiredInvoice;
import tz.go.tcra.lims.intergration.bills.entity.GeneratedInvoice;
import tz.go.tcra.lims.intergration.bills.entity.GeneratedReceipt;
import tz.go.tcra.lims.intergration.bills.repository.CancelInvoiceRepository;
import tz.go.tcra.lims.intergration.bills.repository.CancelReceiptRepository;
import tz.go.tcra.lims.intergration.bills.repository.ExpiredInvoiceRepository;
import tz.go.tcra.lims.intergration.bills.repository.GeneratedInvoiceRepository;
import tz.go.tcra.lims.intergration.bills.repository.GeneratedReceiptRepository;
import tz.go.tcra.lims.intergration.config.SecurityConfigService;
import tz.go.tcra.lims.intergration.config.SignatureService;
import tz.go.tcra.lims.intergration.config.SysConfig;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.GeneralException;

/**
 * @author DonaldSj
 */

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

	private final SysConfig sysConfig;

	private final SecurityConfigService securityConfigService;

	private final GeneratedInvoiceRepository invoiceRepository;
	@Autowired
	private GeneratedReceiptRepository generatedReceiptRepository;

	@Autowired
	private ExpiredInvoiceRepository expiredInvoiceRepository;

	@Autowired
	private CancelInvoiceRepository cancelInvoiceRepository;

	@Autowired
	private CancelReceiptRepository cancelReceiptRepository;

	private final SignatureService signatureService;

	public InvoiceServiceImpl(SysConfig sysConfig, SecurityConfigService securityConfigService,
			GeneratedInvoiceRepository invoiceRepository, SignatureService signatureService) {
		this.sysConfig = sysConfig;
		this.securityConfigService = securityConfigService;
		this.invoiceRepository = invoiceRepository;
		this.signatureService = signatureService;
	}

	@Override
	public Response<Object> generateAndSendInvoice(InvoiceSubmissionDto invoiceDto) throws Exception {

		log.info("CALLING SERVICE TO SAVE TO DB..");
		GeneratedInvoice invoice = saveInvoice(invoiceDto);

		log.info("DATA PREPARED TO BE SENT TO ERMS..");

//		GeneratedInvoice generatedInvoice = invoiceRepository.save(invoice);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.add("Authorization",
				"Bearer "
						+ securityConfigService
								.accessToken(sysConfig.getErmsClientId(), sysConfig.getErmsClientSecret(),
										sysConfig.getErmsAuthTokenUrl(), sysConfig.getErmsAuthGrantType())
								.getAccessToken());

		HttpEntity<?> request = new HttpEntity<>(signatureService.generateSignedData(invoiceDto), headers);

		System.out.println("SENT OBJECT BODY: " + request);

		ResponseEntity<Object> createdInvoice = restTemplate.exchange(sysConfig.getErmsInvoicePostUrl(),
				HttpMethod.POST, request, Object.class);

//		Object createdInvoice = restTemplate.exchange(sysConfig.getErmsInvoicePostUrl(), HttpMethod.POST, request,
//				Object.class);

		System.out.println("OBJECT YETU...." + createdInvoice);

		if (createdInvoice.getStatusCode().equals(HttpStatus.valueOf(200))) {
			// call other methods to save the Successfull created Invoice

			invoice.setReceivedInErms(true);
			invoice.setUpdatedAt(LocalDateTime.now());
			// invoice.setReceivableNumber(createdInvoice.getBody());
			invoiceRepository.saveAndFlush(invoice);
			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdInvoice.getBody());
		} else {

			// other than 200 status code returned
			System.out.println("OBJECT YETU IMERUDI ...." + createdInvoice);

			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdInvoice.getBody());
		}

	}

	@Override
	public Response<Object> generateAndSendReceipt(ReceiptSubmissionDto receiptDto) throws Exception {

		log.info("DATA PREPARED..");

		saveReceipt(receiptDto);
		log.info("DATA SAVED ..");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.add("Authorization",
				"Bearer "
						+ securityConfigService
								.accessToken(sysConfig.getErmsClientId(), sysConfig.getErmsClientSecret(),
										sysConfig.getErmsAuthTokenUrl(), sysConfig.getErmsAuthGrantType())
								.getAccessToken());

		HttpEntity<?> request = new HttpEntity<>(signatureService.generateSignedData(receiptDto), headers);

		System.out.println("SENT OBJECT BODY: " + request.getBody());

//		ResponseEntity<Object> createdInvoice = restTemplate.exchange(sysConfig.getErmsInvoicePostUrl(),
//				HttpMethod.POST, request, Object.class);

		ResponseEntity<Object> createdReceipt = restTemplate.exchange(sysConfig.getErmsReceiptPostUrl(),
				HttpMethod.POST, request, Object.class);

		System.out.println("OBJECT YETU...." + createdReceipt);
		if (createdReceipt.getStatusCode().equals(HttpStatus.valueOf(200))) {
			// call other methods to save the Successfull created Invoice
//			generatedInvoice.setReceivedInErms(true);
//			invoiceRepository.save(generatedInvoice);
			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdReceipt.getBody());
		} else {

			// other than 200 status code returned
			System.out.println("OBJECT YETU IMERUDI ...." + createdReceipt);

			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdReceipt.getBody());
		}
	}

	public GeneratedReceipt saveReceipt(ReceiptSubmissionDto receiptDto) {
		GeneratedReceipt generatedReceipt = null;

		try {
			GeneratedReceipt receipt = new GeneratedReceipt();
			receipt.setAmount(receiptDto.getAmount());
			receipt.setBankAccountNo(receiptDto.getBankAccountNo());
			receipt.setBillNumber(receiptDto.getBillNumber());
			receipt.setBranchCode(receiptDto.getBranchCode());
			receipt.setExchequerNo(receiptDto.getExchequerNo());
			/*
			 * ErmsBillClients client = new ErmsBillClients(); client.setActive(true);
			 * client.setAddress(receiptDto.getClient().getAddress());
			 * client.setApproved(true);
			 * client.setClientType(receiptDto.getClient().getClientType().toString());
			 * client.setCode(receiptDto.getClient().getCode());
			 * client.setEmail(receiptDto.getClient().getEmail());
			 * client.setName(receiptDto.getClient().getName());
			 * client.setPhone(receiptDto.getClient().getPhone());
			 */

			// receipt.setClient(client);
			receipt.setControlNumber(receiptDto.getControlNumber());
			receipt.setCurrencyCode(receiptDto.getCurrencyCode());
			receipt.setDepartmentCode(receiptDto.getDepartmentCode());
			receipt.setDescription(receiptDto.getDescription());
			receipt.setExchangeRate(receiptDto.getExchangeRate());
			receipt.setGeneratedBy(receiptDto.getGeneratedBy());
			receipt.setReferenceNumber(receiptDto.getReferenceNumber());
			receipt.setReceivableDate(receiptDto.getReceiptDate());
			receipt.setTitle(receiptDto.getTitle());

			log.info(" RECEIPT DATA ABOUT TO BE SAVED.." + receipt);

			generatedReceipt = generatedReceiptRepository.save(receipt);

			log.info(" RECEIPT DATA SAVED..");

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return generatedReceipt;

	}

	public GeneratedInvoice saveInvoice(InvoiceSubmissionDto invoiceDto) {

		GeneratedInvoice invoice = new GeneratedInvoice();
		invoice.setAmount(invoiceDto.getAmount());
		invoice.setBillNumber(invoiceDto.getBillNumber());
		invoice.setBranchCode(invoiceDto.getBranchCode());
		invoice.setControlNumber(invoiceDto.getControlNumber());
		invoice.setCurrencyCode(invoiceDto.getCurrencyCode());
		invoice.setDepartmentCode(invoiceDto.getDepartmentCode());
		invoice.setDescription(invoiceDto.getDescription());
		invoice.setExchangeRate(invoiceDto.getExchangeRate());
		invoice.setGeneratedBy(invoiceDto.getGeneratedBy());
		invoice.setOperationType(invoiceDto.getOperationType());
		invoice.setReceivableDate(invoiceDto.getReceivableDate());
		invoice.setReceivedInErms(false);

		GeneratedInvoice generatedInvoice = invoiceRepository.saveAndFlush(invoice);

		return generatedInvoice;
	}

	public List<ExpiredInvoice> saveInvoiceExpired(InvoiceExpireDto invoiceExpireDto) {

		List<ExpiredInvoice> expiredInvoicelist = new ArrayList<>();
		for (String billNumber : invoiceExpireDto.getBills()) {
			ExpiredInvoice invoice = new ExpiredInvoice();
			invoice.setCreatedAt(LocalDateTime.now());
			invoice.setBillNumber(billNumber);
			invoice.setReceivedInErms(false);
			expiredInvoicelist.add(invoice);
		}

		List<ExpiredInvoice> generatedInvoiceExpired = expiredInvoiceRepository.saveAll(expiredInvoicelist);

		return generatedInvoiceExpired;
	}

	public CancelReceipt saveReceiptCancel(ReceiptCancelDto receiptCancelDto) {

		CancelReceipt invoice = new CancelReceipt();

		invoice.setBillNumber(receiptCancelDto.getBillNumber());
		invoice.setBranchCode(receiptCancelDto.getBranchCode());

		invoice.setDepartmentCode(receiptCancelDto.getDepartmentCode());

		invoice.setReceivedInErms(false);

		CancelReceipt generatedRceiptCancel = cancelReceiptRepository.saveAndFlush(invoice);

		return generatedRceiptCancel;
	}

	public CancelInvoice saveInvoiceCancel(InvoiceCancelDto invoiceCancelDto) {

		CancelInvoice invoice = new CancelInvoice();

		invoice.setBillNumber(invoiceCancelDto.getBillNumber());
		invoice.setBranchCode(invoiceCancelDto.getBranchCode());

		invoice.setDepartmentCode(invoiceCancelDto.getDepartmentCode());

		invoice.setReceivedInErms(false);

		CancelInvoice generatedInvoiceCancel = cancelInvoiceRepository.saveAndFlush(invoice);

		return generatedInvoiceCancel;
	}

	@Override
	public Response<Object> generateAndSendReceiptCancel(ReceiptCancelDto receiptCancelDto) throws Exception {
		log.info("DATA PREPARED..");

		saveReceiptCancel(receiptCancelDto);
		log.info("DATA SAVED ..");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.add("Authorization",
				"Bearer "
						+ securityConfigService
								.accessToken(sysConfig.getErmsClientId(), sysConfig.getErmsClientSecret(),
										sysConfig.getErmsAuthTokenUrl(), sysConfig.getErmsAuthGrantType())
								.getAccessToken());

		HttpEntity<?> request = new HttpEntity<>(signatureService.generateSignedData(receiptCancelDto), headers);

		System.out.println("SENT OBJECT BODY: " + request.getBody());

//		ResponseEntity<Object> createdInvoice = restTemplate.exchange(sysConfig.getErmsInvoicePostUrl(),
//				HttpMethod.POST, request, Object.class);

		ResponseEntity<Object> createdReceiptCancel = restTemplate.exchange(sysConfig.getErmsInvoiceCancelPostUrl(),
				HttpMethod.POST, request, Object.class);

		System.out.println("OBJECT YETU...." + createdReceiptCancel);
		if (createdReceiptCancel.getStatusCode().equals(HttpStatus.valueOf(200))) {
			// call other methods to save the Successfull created Invoice
//			generatedInvoice.setReceivedInErms(true);
//			invoiceRepository.save(generatedInvoice);
			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdReceiptCancel.getBody());
		} else {

			// other than 200 status code returned
			System.out.println("OBJECT YETU IMERUDI ...." + createdReceiptCancel);

			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdReceiptCancel.getBody());
		}
	}

	@Override
	public Response<Object> generateAndSendInvoiceCancel(InvoiceCancelDto invoiceCancelDto) throws Exception {
		log.info("DATA PREPARED..");

		saveInvoiceCancel(invoiceCancelDto);
		log.info("DATA SAVED ..");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.add("Authorization",
				"Bearer "
						+ securityConfigService
								.accessToken(sysConfig.getErmsClientId(), sysConfig.getErmsClientSecret(),
										sysConfig.getErmsAuthTokenUrl(), sysConfig.getErmsAuthGrantType())
								.getAccessToken());

		HttpEntity<?> request = new HttpEntity<>(signatureService.generateSignedData(invoiceCancelDto), headers);

		System.out.println("SENT OBJECT BODY: " + request.getBody());

//		ResponseEntity<Object> createdInvoice = restTemplate.exchange(sysConfig.getErmsInvoicePostUrl(),
//				HttpMethod.POST, request, Object.class);

		ResponseEntity<Object> createdInvoiceCancel = restTemplate.exchange(sysConfig.getErmsInvoiceCancelPostUrl(),
				HttpMethod.POST, request, Object.class);

		System.out.println("OBJECT YETU...." + createdInvoiceCancel);
		if (createdInvoiceCancel.getStatusCode().equals(HttpStatus.valueOf(200))) {
			// call other methods to save the Successfull created Invoice
//			generatedInvoice.setReceivedInErms(true);
//			invoiceRepository.save(generatedInvoice);
			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdInvoiceCancel.getBody());
		} else {

			// other than 200 status code returned
			System.out.println("OBJECT YETU IMERUDI ...." + createdInvoiceCancel);

			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdInvoiceCancel.getBody());
		}
	}

	@Override
	public Response<Object> generateAndSendInvoiceExpired(InvoiceExpireDto invoiceExpireDto) throws Exception {
		log.info("DATA PREPARED..");

		saveInvoiceExpired(invoiceExpireDto);

		List<String> bills = new ArrayList<>();
		bills = invoiceExpireDto.getBills();

		log.info("DATA SAVED ..");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.add("Authorization",
				"Bearer "
						+ securityConfigService
								.accessToken(sysConfig.getErmsClientId(), sysConfig.getErmsClientSecret(),
										sysConfig.getErmsAuthTokenUrl(), sysConfig.getErmsAuthGrantType())
								.getAccessToken());

		HttpEntity<?> request = new HttpEntity<>(signatureService.generateSignedData(bills), headers);

		System.out.println("SENT OBJECT BODY: " + request.getBody());

//		ResponseEntity<Object> createdInvoice = restTemplate.exchange(sysConfig.getErmsInvoicePostUrl(),
//				HttpMethod.POST, request, Object.class);

		ResponseEntity<Object> createdInvoiceExpired = restTemplate.exchange(sysConfig.getErmsInvoiceExpiredPostUrl(),
				HttpMethod.POST, request, Object.class);

		System.out.println("OBJECT YETU...." + createdInvoiceExpired);
		if (createdInvoiceExpired.getStatusCode().equals(HttpStatus.valueOf(200))) {
			// call other methods to save the Successfull created Invoice
//			generatedInvoice.setReceivedInErms(true);
//			invoiceRepository.save(generatedInvoice);
			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdInvoiceExpired.getBody());
		} else {

			// other than 200 status code returned
			System.out.println("OBJECT YETU IMERUDI ...." + createdInvoiceExpired);

			return new Response<>(ResponseCode.SUCCESS, true, "Success..", createdInvoiceExpired.getBody());
		}
	}

}