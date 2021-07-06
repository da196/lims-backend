package tz.go.tcra.lims.payment.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.miscellaneous.enums.BillingStatusEnum;
import tz.go.tcra.lims.payment.entity.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

	List<Billing> findByGepgStatusAndControlNumberAndActive(int i, String controllNumber, boolean b);

	boolean existsByBillingNumber(String billId);

	Billing findByBillingNumber(String billId);

	boolean existsByBillingNumberAndControlNumber(String billId, String controlNumber);

	Billing findByBillingNumberAndControlNumber(String billId, String controlNumber);

	List<Billing> findByActive(boolean b);

	List<Billing> findByLicenceIdIn(List<Long> licenceids);

	boolean existsByLicenceId(Long licenseId);

	List<Billing> findByLicenceId(Long licenseId);

	List<Billing> findByLicenceIdInAndStatusIn(List<Long> licenceids, List<BillingStatusEnum> status);

	boolean existsByStatusAndActive(BillingStatusEnum paid, boolean b);

	List<Billing> findByStatusAndActive(BillingStatusEnum paid, boolean b);

	@Query("SELECT b FROM Billing b WHERE  "
			+ "CONCAT(b.billingNumber,b.controlNumber,b.gsfCode,b.currency.code,b.active)" + " LIKE %?1%")
	Page<Billing> findAll(String keyword, Pageable pageable);

	List<Billing> findByStatusAndActiveAndExpireDateBefore(BillingStatusEnum pending, boolean b, LocalDateTime now);

	boolean existsByStatusAndActiveAndLicenceId(BillingStatusEnum paid, boolean b, Long id);

	List<Billing> findByLicenceIdAndAttachedTo(Long licenseId, BillingAttachedToEnum licence);

	boolean existsByIdAndStatus(Long billId, BillingStatusEnum pending);

	Billing findByIdAndStatus(Long billId, BillingStatusEnum pending);

	List<Billing> findByLicenceIdInOrderByIdDesc(List<Long> licenceids);

	List<Billing> findByActiveAndBillRate(boolean b, Double rate);

}
