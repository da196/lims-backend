package tz.go.tcra.lims.payment.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.payment.entity.BillingTransaction;
import tz.go.tcra.lims.payment.repository.BillingRepository;
import tz.go.tcra.lims.payment.repository.BillingTransactionRepository;

@Slf4j
@Service
public class BillingTransactionServiceImpl implements BillingTransactionService {
	@Autowired
	private BillingTransactionRepository billingTransactionRepository;

	@Autowired

	BillingRepository billingRepository;

	@Override
	public BillingTransaction saveBillingTransaction(BillingTransaction billingTransaction) {

		try {
			if (billingRepository.existsById(billingTransaction.getBillingId())) {

				billingTransaction.setCreatedAt(LocalDateTime.now());

				return billingTransactionRepository.saveAndFlush(billingTransaction);

			} else {

				return null;
			}
		} catch (Exception e) {

			return null;
		}
	}

}
