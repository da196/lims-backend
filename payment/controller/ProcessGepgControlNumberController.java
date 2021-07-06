package tz.go.tcra.lims.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.payment.dto.MamlakapgDto;
import tz.go.tcra.lims.payment.dto.MamlakapgPaidDto;
import tz.go.tcra.lims.payment.dto.RequestForControlNumberDto;
import tz.go.tcra.lims.payment.dto.RequestForControlNumberGepgDto;
import tz.go.tcra.lims.payment.entity.Billing;
import tz.go.tcra.lims.payment.entity.Mamlakapg;
import tz.go.tcra.lims.payment.entity.MamlakapgPaid;
import tz.go.tcra.lims.payment.service.BillingService;
import tz.go.tcra.lims.payment.service.MamlakapgPaidService;
import tz.go.tcra.lims.payment.service.MamlakapgService;
import tz.go.tcra.lims.payment.service.ProcessGePGControlNumberService;

@Slf4j
@RestController
@RequestMapping("v1/request-control-number")
public class ProcessGepgControlNumberController {

	@Autowired
	private ProcessGePGControlNumberService processGePGControlNumberService;

	@Autowired
	private BillingService billingService;

	@Autowired
	private MamlakapgPaidService mamlakapgPaidService;

	@Autowired
	private MamlakapgService mamlakapgService;

	@PostMapping(value = "/getControlNumber")
	public Boolean getControllNumber(@RequestBody RequestForControlNumberDto requestForControlNumberDto) {

		RequestForControlNumberGepgDto requestForControlNumberGepgDto = processGePGControlNumberService
				.createRequestForControlNumber(requestForControlNumberDto);
		if (requestForControlNumberGepgDto != null) {
			if (processGePGControlNumberService.requestControlNumber(requestForControlNumberGepgDto) == true) {
				return true;
			} else {

				return false;
			}
		} else {

			return false;
		}

	}

	@PostMapping(value = "/controlnumber", produces = MediaType.TEXT_XML_VALUE, consumes = MediaType.TEXT_XML_VALUE)
	public Billing receiveGepgResponse(@RequestBody MamlakapgDto mamlakapgPaidDTO) {

		log.info(mamlakapgPaidDTO.toString());

		if (mamlakapgPaidDTO != null && mamlakapgPaidDTO.getBillId() != null
				&& mamlakapgPaidDTO.getPayCntrNum() != null) {
			// call service to save logs into db

			Mamlakapg mamlakapg = new Mamlakapg();

			mamlakapg.setBillId(mamlakapgPaidDTO.getBillId());
			mamlakapg.setPayCntrNum(mamlakapgPaidDTO.getPayCntrNum());
			mamlakapg.setTrxSts(mamlakapgPaidDTO.getTrxSts());
			mamlakapg.setTrxStsCode(mamlakapgPaidDTO.getTrxStsCode());

			mamlakapgService.saveMamlakapg(mamlakapg);

			return (billingService.updateBillingControlNumber(mamlakapgPaidDTO.getBillId(),
					mamlakapgPaidDTO.getPayCntrNum()));

		} else {

			return null;
		}
	}

	@PostMapping(value = "/paid", produces = MediaType.TEXT_XML_VALUE, consumes = MediaType.TEXT_XML_VALUE)
	public boolean receiveGepgResponse(@RequestBody MamlakapgPaidDto mamlakapgPaidDto) {

		log.info(mamlakapgPaidDto.toString());

		if (mamlakapgPaidDto != null && mamlakapgPaidDto.getBillId() != null) {

			// call service to save to DB logs from internal gateway

			MamlakapgPaid mamlakapgPaid = new MamlakapgPaid();
			mamlakapgPaid.setBillAmt(mamlakapgPaidDto.getBillAmt());
			mamlakapgPaid.setBillId(mamlakapgPaidDto.getBillId());
			mamlakapgPaid.setBillPayOpt(mamlakapgPaidDto.getBillPayOpt());
			mamlakapgPaid.setCCy(mamlakapgPaidDto.getCCy());
			mamlakapgPaid.setPayCntrNum(mamlakapgPaidDto.getPayCntrNum());
			mamlakapgPaid.setPayRefId(mamlakapgPaidDto.getPayRefId());
			mamlakapgPaid.setPspName(mamlakapgPaidDto.getPspName());
			mamlakapgPaid.setPspReceiptNumber(mamlakapgPaidDto.getPspReceiptNumber());
			mamlakapgPaid.setPyrCellNum(mamlakapgPaidDto.getPyrCellNum());
			mamlakapgPaid.setPyrEmail(mamlakapgPaidDto.getPyrEmail());
			mamlakapgPaid.setPyrName(mamlakapgPaidDto.getPyrName());
			mamlakapgPaid.setTrxDtTm(mamlakapgPaidDto.getTrxDtTm());
			mamlakapgPaid.setTrxID(mamlakapgPaidDto.getTrxID());
			mamlakapgPaid.setUsdPayChnl(mamlakapgPaidDto.getUsdPayChnl());
			mamlakapgPaid.setCtrAccNum(mamlakapgPaidDto.getCtrAccNum());

			mamlakapgPaidService.saveMamlakapgPaid(mamlakapgPaid);

			billingService.updateBillingPayment(mamlakapgPaidDto.getBillId(), mamlakapgPaidDto.getPayCntrNum(),
					mamlakapgPaidDto.getPaidAmt(), mamlakapgPaidDto.getBillAmt(), mamlakapgPaidDto.getUsdPayChnl(),
					mamlakapgPaid.getCtrAccNum(), mamlakapgPaidDto.getPspReceiptNumber());

			return true;
		} else {

			return true;
		}
	}

	@PostMapping(value = "/requestControlNumber")
	public Boolean requestControlNumber(@RequestBody RequestForControlNumberGepgDto requestForControlNumberGepgDto) {
		return processGePGControlNumberService.requestControlNumber(requestForControlNumberGepgDto);
	}

}
