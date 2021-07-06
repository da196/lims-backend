package tz.go.tcra.lims.payment.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tz.go.tcra.lims.payment.entity.Mamlakapg;
import tz.go.tcra.lims.payment.repository.MamlakapgRepository;

@Service
public class MamlakapgServiceImpl implements MamlakapgService {
	@Autowired
	private MamlakapgRepository mamlakapgRepository;

	@Override
	public Mamlakapg saveMamlakapg(Mamlakapg mamlakapg) {

		mamlakapg.setCreatedAt(LocalDateTime.now());

		return mamlakapgRepository.saveAndFlush(mamlakapg);
	}

}
