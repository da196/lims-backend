package tz.go.tcra.lims.payment.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.allegro.finance.tradukisto.MoneyConverters;
import tz.go.tcra.lims.payment.dto.InvoiceMockDto;
import tz.go.tcra.lims.payment.entity.InvoiceMock;
import tz.go.tcra.lims.payment.repository.InvoiceMockRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.GeneralException;

@Slf4j
@Service
public class InvoiceMockServiceImpl implements InvoiceMockService {

	@Autowired
	private InvoiceMockRepository invoiceMockRepository;
	@Autowired
	private MoneyInWords moneyInWords;

	@Override
	public Response<InvoiceMockDto> getByIdInvoiceMock(Long id) {

		Response<InvoiceMockDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"INVOICE MOCK RETRIEVED SUCCESSFULLY", null);
		try {

			if (invoiceMockRepository.existsByIdAndActive(id, true)) {
				InvoiceMockDto invoiceMockDto = new InvoiceMockDto();

				InvoiceMock invoiceMock = invoiceMockRepository.findByIdAndActive(id, true);

				invoiceMockDto.setActive(invoiceMock.getActive());
				invoiceMockDto.setAddress(invoiceMock.getAddress());
				invoiceMockDto.setAmount(invoiceMock.getAmount());
				// String amountInWords =
				// getMoneyIntoWords(Double.toString(invoiceMock.getAmount()));
				invoiceMockDto.setAmountInWords(moneyInWords.getMoneyIntoWords(invoiceMock.getAmount()).toUpperCase());
				invoiceMockDto.setCdate(invoiceMock.getCdate());
				invoiceMockDto.setCustomerCode(invoiceMock.getCustomerCode());
				invoiceMockDto.setCustumerName(invoiceMock.getCustumerName());
				invoiceMockDto.setDescription(invoiceMock.getDescription());
				invoiceMockDto.setId(invoiceMock.getId());
				invoiceMockDto.setInvoiceNumber(invoiceMock.getInvoiceNumber());
				invoiceMockDto.setTotalAmount(invoiceMock.getTotalAmount());

				// String totalAmountInWords =
				// getMoneyIntoWords(Double.toString(invoiceMock.getTotalAmount()));

				invoiceMockDto.setTotalAmountInWords(
						moneyInWords.getMoneyIntoWords(invoiceMock.getTotalAmount()).toUpperCase());

				response.setData(invoiceMockDto);

			} else {

				response.setStatus(false);
				response.setCode(ResponseCode.NO_RECORD_FOUND);
				response.setMessage("INVOICE MOCK WAS NOT FOUND");

			}
		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");

		}
		return response;
	}

	@Override
	public Response<List<InvoiceMock>> getAllInvoiceMock() {
		Response<List<InvoiceMock>> response = new Response<>(ResponseCode.SUCCESS, true,
				"INVOICE MOCKS RETRIEVED SUCCESSFULLY", null);
		try {
			List<InvoiceMock> invoiceMocks = invoiceMockRepository.findByActive(true);

			response.setData(invoiceMocks);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");

		}

		return response;
	}

	public String getMoneyIntoWords(String input) {
		MoneyConverters converter = MoneyConverters.ENGLISH_BANKING_MONEY_VALUE;

		return converter.asWords(new BigDecimal(input));
	}

}
