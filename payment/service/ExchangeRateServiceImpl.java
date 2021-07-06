package tz.go.tcra.lims.payment.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.payment.dto.ExchangeRateDto;
import tz.go.tcra.lims.payment.dto.ExchangeRequestDto;
import tz.go.tcra.lims.payment.dto.ExchangeResponseDto;
import tz.go.tcra.lims.payment.entity.ExchangeRate;
import tz.go.tcra.lims.payment.repository.ExchangeRateRepository;
import tz.go.tcra.lims.reports.service.DashboardReportServiceImpl;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.GeneralException;

@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

	@Autowired
	private ExchangeRateRepository exchangeRateRepository;

	@Autowired
	private ListOfValueRepository listOfValueRepository;

	@Autowired
	private PagedResourcesAssembler<ExchangeRate> pagedResourcesAssembler;

	@Autowired
	private DashboardReportServiceImpl dashboardReportServiceImpl;

	@Override
	public Response<ExchangeRate> saveExchangeRate(ExchangeRateDto exchangeRateDto) {

		Response<ExchangeRate> response = new Response<>(ResponseCode.SUCCESS, true, "EXCHANGE RATE SAVED SUCCESSFULLY",
				null);
		try {
			List<Long> currencyids = new ArrayList<>();
			currencyids.add(exchangeRateDto.getCurrecyFromId());
			currencyids.add(exchangeRateDto.getCurrecyToId());

			if (exchangeRateRepository.existsByStartDateAndActiveAndCurrecyFromAndCurrecyTo(
					exchangeRateDto.getStartDate(), true,
					listOfValueRepository.findByIdAndActive(exchangeRateDto.getCurrecyFromId(), true),
					listOfValueRepository.findByIdAndActive(exchangeRateDto.getCurrecyToId(), true))) {

				response.setCode(ResponseCode.DUPLICATE);
				response.setMessage("DUPLICATE EXCHANGE RATE");

			} else {

				if (listOfValueRepository.existsByIdInAndActive(currencyids, true)) {
					ExchangeRate exchangeRate = new ExchangeRate();

					exchangeRate.setAmount(exchangeRateDto.getAmount());

					exchangeRate.setCurrecyFrom(
							listOfValueRepository.findByIdAndActive(exchangeRateDto.getCurrecyFromId(), true));
					exchangeRate.setCurrecyTo(
							listOfValueRepository.findByIdAndActive(exchangeRateDto.getCurrecyToId(), true));
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

					/*
					 * if (exchangeRateDto.getExpireDate() != null && exchangeRateDto.getStartDate()
					 * != null) { exchangeRate.setExpireDate( new
					 * java.sql.Timestamp(exchangeRateDto.getExpireDate().getTime()).toLocalDateTime
					 * ()); exchangeRate.setStartDate( new
					 * java.sql.Timestamp(exchangeRateDto.getStartDate().getTime()).toLocalDateTime(
					 * )); }
					 */

					if (exchangeRateDto.getExpireDate() != null && exchangeRateDto.getStartDate() != null) {
						exchangeRate.setExpireDate(exchangeRateDto.getExpireDate());
						exchangeRate.setStartDate(exchangeRateDto.getStartDate());
					}

					exchangeRate.setCreatedAt(LocalDateTime.now());

					ExchangeRate exchangeRatesaved = exchangeRateRepository.saveAndFlush(exchangeRate);

					response.setData(exchangeRatesaved);
				} else {

					response.setCode(ResponseCode.CONSTRAINT_VIOLATION);
					response.setStatus(false);
					response.setMessage("EXCHANGE RATE SAVE VIOLATED CONSTRAINTS FOR CURRECIES");

				}

			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			log.error(e.toString());
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<ExchangeRate> getExchangeRateById(Long id) {
		Response<ExchangeRate> response = new Response<>(ResponseCode.SUCCESS, true,
				"EXCHANGE RATE RETRIEVED SUCCESSFULLY", null);
		try {
			if (exchangeRateRepository.existsByIdAndActive(id, true)) {

				response.setData(exchangeRateRepository.findByIdAndActive(id, true));

			} else {

				response.setStatus(false);
				response.setCode(ResponseCode.NO_RECORD_FOUND);
				response.setMessage("EXCHANGE RATE WAS NOT FOUND");

			}
		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");

		}
		return response;
	}

	@Override
	public Response<ExchangeRate> updateExchangeRate(ExchangeRateDto exchangeRateDto, Long id) {

		Response<ExchangeRate> response = new Response<>(ResponseCode.SUCCESS, true,
				"EXCHANGE RATE UPDATED SUCCESSFULLY", null);

		try {
			if (exchangeRateRepository.existsByIdAndActive(id, true)) {

				List<Long> currencyids = new ArrayList<>();
				currencyids.add(exchangeRateDto.getCurrecyFromId());
				currencyids.add(exchangeRateDto.getCurrecyToId());

				if (listOfValueRepository.existsByIdInAndActive(currencyids, true)) {

					ExchangeRate exchangeRatetoUpdate = exchangeRateRepository.findByIdAndActive(id, true);
					exchangeRatetoUpdate.setAmount(exchangeRateDto.getAmount());
					exchangeRatetoUpdate.setCurrecyFrom(
							listOfValueRepository.findByIdAndActive(exchangeRateDto.getCurrecyFromId(), true));
					exchangeRatetoUpdate.setCurrecyTo(
							listOfValueRepository.findByIdAndActive(exchangeRateDto.getCurrecyToId(), true));
					if (exchangeRateDto.getExpireDate() != null && exchangeRateDto.getStartDate() != null) {
						exchangeRatetoUpdate.setExpireDate(exchangeRateDto.getExpireDate());
						exchangeRatetoUpdate.setStartDate(exchangeRateDto.getStartDate());
					}

					exchangeRatetoUpdate.setUpdatedAt(LocalDateTime.now());

					response.setData(exchangeRateRepository.saveAndFlush(exchangeRatetoUpdate));

				} else {

					response.setCode(ResponseCode.CONSTRAINT_VIOLATION);
					response.setStatus(false);
					response.setMessage("EXCHANGE RATE SAVE VIOLATED CONSTRAINTS FOR CURRECIES");

				}

			} else {
				response.setStatus(false);
				response.setCode(ResponseCode.NO_RECORD_FOUND);
				response.setMessage("EXCHANGE RATE WAS NOT FOUND");

			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");

		}
		return response;
	}

	@Override
	public Response<ExchangeRate> deleteExchangeRate(Long id) {
		Response<ExchangeRate> response = new Response<>(ResponseCode.SUCCESS, true,
				"EXCHANGE RATE DELETED SUCCESSFULLY", null);
		try {
			if (exchangeRateRepository.existsByIdAndActive(id, true)) {

				ExchangeRate exchangeRate = exchangeRateRepository.findByIdAndActive(id, true);
				exchangeRate.setActive(false);
				exchangeRate.setDeleteddAt(LocalDateTime.now());

				response.setData(exchangeRateRepository.saveAndFlush(exchangeRate));

			} else {
				response.setStatus(false);
				response.setCode(ResponseCode.NO_RECORD_FOUND);
				response.setMessage("EXCHANGE RATE WAS NOT DELETED");

			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<List<ExchangeRate>> getAllExchangeRates() {

		Response<List<ExchangeRate>> response = new Response<>(ResponseCode.SUCCESS, true,
				"EXCHANGE RATES RETRIEVED SUCCESSFULLY", null);
		try {
			List<ExchangeRate> exchangeRates = exchangeRateRepository.findByActive(true);

			response.setData(exchangeRates);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");

		}

		return response;
	}

	@Override
	public Response<ExchangeResponseDto> exchangeUSDToTZS(ExchangeRequestDto exchangeRequestDto) {

		if (exchangeRequestDto.getExchangeDate() == null) {

			exchangeRequestDto.setExchangeDate(LocalDateTime.now());
		}

		Response<ExchangeResponseDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"EXCHANGE RATES RETRIEVED SUCCESSFULLY", null);
		try {

			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

			Date exchangeDate = null;
			if (exchangeRequestDto.getExchangeDate() != null) {
				log.info(exchangeRequestDto.toString());

				// exchangeDate =
				// sdformat.parse(exchangeRequestDto.getExchangeDate().toString());
				String DateinString = sdformat.format(
						dashboardReportServiceImpl.convertToDateViaInstant(exchangeRequestDto.getExchangeDate()));

				exchangeDate = new SimpleDateFormat("yyyy-MM-dd").parse(DateinString);

				// exchangeRequestDto.setExchangeDate(sdformat.parse(exchangeRequestDto.getExchangeDate().toString()));

			}

			ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
			if (exchangeRequestDto.getCurrecyFrom() == exchangeRequestDto.getCurrecyTo()) {

				exchangeResponseDto.setAmountFrom(exchangeRequestDto.getAmount());
				exchangeResponseDto.setAmountTo(exchangeRequestDto.getAmount());
				if (listOfValueRepository.existsByIdAndActive(exchangeRequestDto.getCurrecyFrom(), true)) {
					exchangeResponseDto.setCurrencyFrom(listOfValueRepository
							.findByIdAndActive(exchangeRequestDto.getCurrecyFrom(), true).getCode());
					exchangeResponseDto.setCurrencyTo(listOfValueRepository
							.findByIdAndActive(exchangeRequestDto.getCurrecyFrom(), true).getCode());
				}
				exchangeResponseDto.setExpireDate(exchangeRequestDto.getExchangeDate().toString());

				exchangeResponseDto.setMonth(exchangeRequestDto.getExchangeDate().getMonth().toString());
				exchangeResponseDto.setRate(1.0);
				exchangeResponseDto.setStartDate(exchangeRequestDto.getExchangeDate().toString());
				exchangeResponseDto.setYear(String.valueOf(exchangeRequestDto.getExchangeDate().getYear()));

			} else {

				List<ExchangeRate> exchangeRates = exchangeRateRepository.findByActive(true);

				for (ExchangeRate exchangeRate : exchangeRates) {

					if ((exchangeDate != null && exchangeRate.getStartDate() != null && exchangeRate.getAmount() != null
							&& exchangeRequestDto.getAmount() != null)
							&& ((exchangeDate.compareTo(sdformat.parse(exchangeRate.getStartDate().toString())) == 0
									|| exchangeDate
											.compareTo(sdformat.parse(exchangeRate.getExpireDate().toString())) == 0)
									|| (exchangeDate
											.compareTo(sdformat.parse(exchangeRate.getStartDate().toString())) > 0 &&

											sdformat.parse(exchangeRate.getExpireDate().toString())
													.compareTo(exchangeDate) > 0))
							&& (exchangeRequestDto.getCurrecyFrom() == exchangeRate.getCurrecyFrom().getId()
									&& exchangeRequestDto.getCurrecyTo() == exchangeRate.getCurrecyTo().getId())) {

						Double amountTo = exchangeRate.getAmount() * exchangeRequestDto.getAmount();

						exchangeResponseDto.setAmountTo(amountTo);
						exchangeResponseDto.setAmountFrom(exchangeRequestDto.getAmount());
						exchangeResponseDto.setCurrencyFrom(exchangeRate.getCurrecyFrom().getCode());
						exchangeResponseDto.setCurrencyTo(exchangeRate.getCurrecyTo().getCode());

						exchangeResponseDto.setYear(String
								.valueOf(new java.sql.Timestamp(exchangeDate.getTime()).toLocalDateTime().getYear()));
						exchangeResponseDto.setMonth(
								new java.sql.Timestamp(exchangeDate.getTime()).toLocalDateTime().getMonth().toString());
						exchangeResponseDto.setExpireDate(
								sdformat.format(sdformat.parse(exchangeRate.getExpireDate().toString())));

						log.info("EXPIRE DATE= " + exchangeRate.getExpireDate());
						exchangeResponseDto
								.setStartDate(sdformat.format(sdformat.parse(exchangeRate.getStartDate().toString())));
						exchangeResponseDto.setRate(exchangeRate.getAmount());
						break;

					}

				}
			}

			response.setData(exchangeResponseDto);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");

		}

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<ExchangeRate>>> getAllExchangeRatesPageable(String keyword,
			Pageable pageable) {
		Response<CollectionModel<EntityModel<ExchangeRate>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"EXCHANGE RATES RETRIEVED SUCCESSFULLY", null);
		try {
			Page<ExchangeRate> pages = null;

			if (keyword.equalsIgnoreCase("All")) {
				pages = exchangeRateRepository.findByActive(true, pageable);
			} else {

				pages = exchangeRateRepository.findByKeywordAndActive(keyword, true, pageable);
			}

			if (pages != null) {
				response.setData(pagedResourcesAssembler.toModel(pages));
			}
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			response.setStatus(false);
			response.setCode(ResponseCode.FAILURE);
			response.setMessage("FAILURE OCCURED");
		}

		return response;
	}

}
