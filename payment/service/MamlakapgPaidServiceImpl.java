package tz.go.tcra.lims.payment.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tz.go.tcra.lims.payment.entity.MamlakapgPaid;
import tz.go.tcra.lims.payment.repository.MamlakapgPaidRepository;

@Service
public class MamlakapgPaidServiceImpl implements MamlakapgPaidService {
	@Autowired
	private MamlakapgPaidRepository mamlakapgPaidRepository;

	@Override
	public MamlakapgPaid saveMamlakapgPaid(MamlakapgPaid mamlakapgPaid) {
		mamlakapgPaid.setCreatedAt(LocalDateTime.now());

		return mamlakapgPaidRepository.saveAndFlush(mamlakapgPaid);
	}

}
