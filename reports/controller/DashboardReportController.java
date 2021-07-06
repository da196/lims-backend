package tz.go.tcra.lims.reports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.miscellaneous.enums.LicenceStateEnum;
import tz.go.tcra.lims.portal.application.dto.LicencePortalMinDto;
import tz.go.tcra.lims.reports.dto.DashboardReportDto;
import tz.go.tcra.lims.reports.dto.LicenceeDashBoardDto;
import tz.go.tcra.lims.reports.service.DashboardReportService;
import tz.go.tcra.lims.utils.Response;

import java.util.List;

@RestController
@RequestMapping("v1/dashboard-report")
public class DashboardReportController {
    @Autowired
    private DashboardReportService dashboardReportService;

    @GetMapping(value = "/getSummary")
    public Response<DashboardReportDto> getDashboardReport() {
        return dashboardReportService.getDashboardReport();

    }

    @GetMapping(value = "/getLicencee-dashboard")
    public Response<LicenceeDashBoardDto> getLicenceeDashboardReport() {

        return dashboardReportService.getLicenceeDashboardReport();

    }

    @GetMapping(value = "/getLicencee-dashboard/byStatus")
    public Response<List<LicencePortalMinDto>> getLicenseSatisticsByApplicant(
            @RequestParam(name = "licenseState") LicenceStateEnum licenseState) {
        return dashboardReportService.getLicenseSatisticsByApplicant(licenseState);
    }

}
