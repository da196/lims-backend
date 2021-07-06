package tz.go.tcra.lims.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.views.LicenceGeneralStatsView;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceGeneralStatsViewRepository extends JpaRepository<LicenceGeneralStatsView, Long> {

    LicenceGeneralStatsView findFirstByOrderByTotalLicencesAsc();
}
