package tz.go.tcra.lims.payment.service;

import org.springframework.stereotype.Service;

import tz.go.tcra.lims.payment.dto.RequestForControlNumberDto;
import tz.go.tcra.lims.payment.dto.RequestForControlNumberGepgDto;

@Service
public interface ProcessGePGControlNumberService {

	RequestForControlNumberGepgDto createRequestForControlNumber(RequestForControlNumberDto requestForControlNumberDto);

	Boolean requestControlNumber(RequestForControlNumberGepgDto requestForControlNumberGepgDto);

}
