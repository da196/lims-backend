package tz.go.tcra.lims.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.views.LicenceApplicationStatsView;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceApplicationStatsViewRepository extends JpaRepository<LicenceApplicationStatsView, Long> {

    LicenceApplicationStatsView findFirstByOrderByTotalApplicationsAsc();
}
