package tz.go.tcra.lims.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.payment.entity.Billing;
import tz.go.tcra.lims.payment.entity.BillingCharges;

@Repository
public interface BillingChargesRepository extends JpaRepository<BillingCharges, Long> {

	boolean existsByBillingId(Long id);

	List<BillingCharges> findAllByBillingId(Long id);

	boolean existsByBillingIdAndActive(Long id, boolean b);

	List<BillingCharges> findByIdAndActive(Long id, boolean b);

//	@Query("SELECT new tz.go.tcra.lims.licence.dto.LicenceBillingDto(" + "e.id," + "e.billing.billingNumber,"
//			+ "e.billing.controlNumber," + "e.feeType," + "e.amount," + "e.status,"
//			+ "e.billing.payDate) FROM BillingCharges e WHERE e.billing.licenceId=?1 " + "ORDER BY e.createdAt DESC")
//	List<LicenceBillingDto> findAllChargesByLicence(Long licenceId);

	List<BillingCharges> findByBilling(Billing billing);

	boolean existsByBillingAndActive(Billing billing, boolean b);
}
