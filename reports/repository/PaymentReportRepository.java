package tz.go.tcra.lims.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.views.PaymentReportView;

/**
 * @author DonaldSj
 */

@Repository
public interface PaymentReportRepository extends JpaRepository<PaymentReportView, Long> {
}
