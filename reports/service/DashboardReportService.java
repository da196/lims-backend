package tz.go.tcra.lims.reports.service;

import org.springframework.stereotype.Service;
import tz.go.tcra.lims.miscellaneous.enums.LicenceStateEnum;
import tz.go.tcra.lims.portal.application.dto.LicencePortalMinDto;
import tz.go.tcra.lims.reports.dto.DashboardReportDto;
import tz.go.tcra.lims.reports.dto.LicenceeDashBoardDto;
import tz.go.tcra.lims.utils.Response;

import java.util.List;

@Service
public interface DashboardReportService {

    Response<DashboardReportDto> getDashboardReport();

    Response<LicenceeDashBoardDto> getLicenceeDashboardReport();

    Response<List<LicencePortalMinDto>> getLicenseSatisticsByApplicant(LicenceStateEnum licenseState);

}
