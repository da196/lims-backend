package tz.go.tcra.lims.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.miscellaneous.enums.BillingStatusEnum;
import tz.go.tcra.lims.payment.entity.BillingReading;

@Repository
public interface BillingReadingRepository extends JpaRepository<BillingReading, Long> {

	@Query("SELECT b FROM BillingReading b WHERE  "
			+ "CONCAT(b.billingNumber,b.controlNumber,b.gsfCode,b.currency.code,b.status)" + " LIKE %?1%"
			+ "AND active =true")
	Page<BillingReading> findAll(String keyword, Pageable pageable);

	@Query("SELECT b FROM BillingReading b WHERE  " + "b.status = ?1" + "AND active =true")
	Page<BillingReading> findAllByStatus(String status, Pageable pageable);

	@Query("SELECT b FROM BillingReading b WHERE  "
			+ "CONCAT(b.billingNumber,b.controlNumber,b.gsfCode,b.currency.code,b.status)" + " LIKE %?1%"
			+ "AND active =true" + " AND  b.status =?2")
	Page<BillingReading> findAll(String keyword, String status, Pageable pageable);

	@Query("SELECT b FROM BillingReading b WHERE  " + "b.status =?1" + "AND active =true")
	Page<BillingReading> findAllByStatus(BillingStatusEnum status, Pageable pageable);

	@Query("SELECT b FROM BillingReading b WHERE  "
			+ "CONCAT(b.billingNumber,b.controlNumber,b.gsfCode,b.currency.code,b.status)" + " LIKE %?1%"
			+ "AND active =true" + " AND  b.status =?2")
	Page<BillingReading> findAll(String keyword, BillingStatusEnum status, Pageable pageable);

}
