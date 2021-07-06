package tz.go.tcra.lims.reports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.views.LicenceView;

import java.util.List;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceReportViewRepository extends JpaRepository<LicenceView, Long> {

    @Query("FROM LicenceView lv WHERE  " + "CONCAT(" + "lv.applicationNumber," + "lv.licenseProduct,"
            + "lv.applicantEntityName," + "lv.licenceNumber," + "lv.applicationType," + "lv.licenceState" + ")"
            + " LIKE %?1% ORDER BY id DESC")
    Page<LicenceView> findByKeyword(String keyword, Pageable pageable);

    @Query("FROM LicenceView lv WHERE lv.licenceState LIKE UPPER(?1) ORDER BY lv.id DESC")
    List<LicenceView> findByLicenceState(@Param(value = "licenceState") String licenceState);

    @Query("FROM LicenceView lv WHERE lv.licenceState LIKE UPPER(?1) ORDER BY lv.id DESC")
    Page<LicenceView> findByLicenceState(String licenceState, Pageable pageable);

    @Query("FROM LicenceView lv ORDER BY lv.id DESC")
    Page<LicenceView> findAllLicences(Pageable pageable);

    @Query("FROM LicenceView lv WHERE  " + "CONCAT(" + "lv.applicationNumber," + "lv.licenseProduct,"
            + "lv.applicantEntityName," + "lv.licenceNumber," + "lv.applicationType," + "lv.licenceState" + ")"
            + " LIKE %?2%" + " AND lv.licenceState LIKE %?2% ORDER BY id DESC")
    Page<LicenceView> findByLicenceStateAndKeyword(String licenceState, String keyword, Pageable pageable);
}
