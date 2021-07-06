package tz.go.tcra.lims.reports.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.entity.views.LicenceApplicationView;
import tz.go.tcra.lims.entity.views.LicenceView;
import tz.go.tcra.lims.reports.dto.LicenceApplicationMainDto;
import tz.go.tcra.lims.reports.dto.LicenceApplicationStatsDto;
import tz.go.tcra.lims.reports.dto.LicenceGeneralStatsDto;
import tz.go.tcra.lims.reports.dto.LicenceMainDto;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

public interface LicenceReportService {

    Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceGeneralReport(String keyword, Pageable pageable);

    Response<CollectionModel<EntityModel<LicenceApplicationView>>> getAllLicenceApplicationsReport(String keyword,
                                                                                                   Pageable pageable);

    Response<EntityModel<LicenceGeneralStatsDto>> getLicenceReportGeneralStats();

    Response<EntityModel<LicenceApplicationStatsDto>> getLicenceApplicationsReportGeneralStats();

    Response<CollectionModel<EntityModel<Licence>>> getAllLicenceAndApplications(String keyword, Pageable pageable);

    Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceByLicenceState(String licenceState);

    Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceByLicenceStateAndKeyWord(String licenceState,
                                                                                              String Keyword, Pageable pageable);

    Response<CollectionModel<EntityModel<LicenceApplicationView>>> getAllLicenceApplicationByCurrentStage(
            String applicationStage);

    Response<LicenceApplicationMainDto> getLicenceApplicationMainReport(Pageable pageable);

    Response<LicenceMainDto> getLicenceMainReport(Pageable pageable);


}
