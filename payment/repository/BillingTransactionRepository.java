package tz.go.tcra.lims.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.payment.entity.BillingTransaction;

@Repository
public interface BillingTransactionRepository extends JpaRepository<BillingTransaction, Long> {

}
