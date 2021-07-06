package tz.go.tcra.lims.payment.service;

import org.springframework.stereotype.Service;

import tz.go.tcra.lims.payment.entity.MamlakapgPaid;

@Service
public interface MamlakapgPaidService {
	MamlakapgPaid saveMamlakapgPaid(MamlakapgPaid mamlakapgPaid);

}
