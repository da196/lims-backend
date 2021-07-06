package tz.go.tcra.lims.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.payment.entity.ErmsBankAccounts;

@Repository
public interface ErmsBankAccountsRepository extends JpaRepository<ErmsBankAccounts, Long> {

	boolean existsByBankAccountAndActive(String bankAccount, boolean b);

	ErmsBankAccounts findFirstByBankAccountAndActive(String bankAccount, boolean b);

}
