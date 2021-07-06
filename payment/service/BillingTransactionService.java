package tz.go.tcra.lims.payment.service;

import org.springframework.stereotype.Service;

import tz.go.tcra.lims.payment.entity.BillingTransaction;

@Service
public interface BillingTransactionService {

	BillingTransaction saveBillingTransaction(BillingTransaction billingTransaction);

}
