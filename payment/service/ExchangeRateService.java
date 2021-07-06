package tz.go.tcra.lims.payment.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import tz.go.tcra.lims.payment.dto.ExchangeRateDto;
import tz.go.tcra.lims.payment.dto.ExchangeRequestDto;
import tz.go.tcra.lims.payment.dto.ExchangeResponseDto;
import tz.go.tcra.lims.payment.entity.ExchangeRate;
import tz.go.tcra.lims.utils.Response;

@Service
public interface ExchangeRateService {

	Response<ExchangeRate> saveExchangeRate(ExchangeRateDto exchangeRateDto);

	Response<ExchangeRate> getExchangeRateById(Long id);

	Response<ExchangeRate> updateExchangeRate(ExchangeRateDto exchangeRateDto, Long id);

	Response<ExchangeRate> deleteExchangeRate(Long id);

	Response<List<ExchangeRate>> getAllExchangeRates();

	Response<ExchangeResponseDto> exchangeUSDToTZS(ExchangeRequestDto exchangeRequestDto);

	Response<CollectionModel<EntityModel<ExchangeRate>>> getAllExchangeRatesPageable(String keyword, Pageable pageable);

}
