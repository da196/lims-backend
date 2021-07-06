package tz.go.tcra.lims.reports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.views.LicenceApplicationView;

import java.util.List;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceApplicationViewRepository extends JpaRepository<LicenceApplicationView, Long> {

    @Query("FROM LicenceApplicationView lav WHERE  " + "CONCAT(" +
            "lav.applicationNumber," +
            "lav.licenseProduct," +
            "lav.applicantEntityName," +
            "lav.applicantEntityName," +
            "lav.applicationType," +
            "lav.licenceState" +
            ")" + " LIKE %?1%")
    Page<LicenceApplicationView> findByKeyword(String keyword, Pageable pageable);

    @Query("FROM LicenceApplicationView lav WHERE lav.applicationStage LIKE UPPER(?1)")
    List<LicenceApplicationView> findByApplicationStage(@Param(value = "applicationStage") String applicationStage);

    @Query("FROM LicenceApplicationView lav WHERE lav.applicationStage LIKE UPPER(?1)")
    Page<LicenceApplicationView> findByApplicationStagePageable(String applicationStage, Pageable pageable);
}
