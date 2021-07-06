package tz.go.tcra.lims.payment.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.payment.dto.ExchangeRateDto;
import tz.go.tcra.lims.payment.dto.ExchangeRequestDto;
import tz.go.tcra.lims.payment.dto.ExchangeResponseDto;
import tz.go.tcra.lims.payment.entity.ExchangeRate;
import tz.go.tcra.lims.payment.service.ExchangeRateService;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping("v1/exchange-rate")
public class ExchangeRateController {

	@Autowired
	private ExchangeRateService exchangeRateService;

	@PostMapping(value = "/save")
	@PreAuthorize("hasRole('ROLE_EXCHANGE_RATE_SAVE')")
	public Response<ExchangeRate> saveExchangeRate(@RequestBody ExchangeRateDto exchangeRateDto) {

		return exchangeRateService.saveExchangeRate(exchangeRateDto);

	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_EXCHANGE_RATE_VIEW')")
	public Response<ExchangeRate> getExchangeRateById(@PathVariable("id") Long id) {

		return exchangeRateService.getExchangeRateById(id);

	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_EXCHANGE_RATE_EDIT')")
	public Response<ExchangeRate> updateExchangeRate(@RequestBody ExchangeRateDto exchangeRateDto,
			@PathVariable("id") Long id) {

		return exchangeRateService.updateExchangeRate(exchangeRateDto, id);

	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_EXCHANGE_RATE_DELETE')")
	public Response<ExchangeRate> deleteExchangeRate(@PathVariable("id") Long id) {
		return exchangeRateService.deleteExchangeRate(id);

	}

	@GetMapping(value = "/get-rates")
	@PreAuthorize("hasRole('ROLE_EXCHANGE_RATE_VIEW_ALL')")
	public Response<List<ExchangeRate>> getAllExchangeRates() {

		return exchangeRateService.getAllExchangeRates();

	}

	@PostMapping(value = "/exchange")
	@PreAuthorize("hasRole('ROLE_EXCHANGE_RATE_MAKE_EXCHANGE')")
	public Response<ExchangeResponseDto> exchangeCurrecy(@RequestBody ExchangeRequestDto exchangeRequestDto) {

		return exchangeRateService.exchangeUSDToTZS(exchangeRequestDto);

	}

	@GetMapping(value = "/exchange/{currencyFrom}/{currencyTo}/{amount}")
	@PreAuthorize("hasRole('ROLE_EXCHANGE_RATE_MAKE_EXCHANGE')")
	public Response<ExchangeResponseDto> exchangeCurrecyForApplicant(@PathVariable("currencyFrom") Long currencyFrom,
			@PathVariable("currencyTo") Long currencyTo, @PathVariable("amount") Double amount) {

		ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();

		exchangeRequestDto.setAmount(amount);
		exchangeRequestDto.setCurrecyFrom(currencyFrom);
		exchangeRequestDto.setCurrecyTo(currencyTo);
		exchangeRequestDto.setExchangeDate(LocalDateTime.now());

		return exchangeRateService.exchangeUSDToTZS(exchangeRequestDto);

	}

	@GetMapping(value = "/get-rates-pageable")
	@PreAuthorize("hasRole('ROLE_EXCHANGE_RATE_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<ExchangeRate>>> getAllExchangeRatesPageable(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return exchangeRateService.getAllExchangeRatesPageable(keyword, pageable);
	}

}
