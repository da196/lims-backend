package tz.go.tcra.lims.reports.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.entity.views.LicenceApplicationView;
import tz.go.tcra.lims.entity.views.LicenceView;
import tz.go.tcra.lims.reports.dto.LicenceApplicationMainDto;
import tz.go.tcra.lims.reports.dto.LicenceApplicationStatsDto;
import tz.go.tcra.lims.reports.dto.LicenceGeneralStatsDto;
import tz.go.tcra.lims.reports.dto.LicenceMainDto;
import tz.go.tcra.lims.reports.repository.LicenceApplicationViewRepository;
import tz.go.tcra.lims.reports.repository.LicenceReportViewRepository;
import tz.go.tcra.lims.reports.service.DataExportService;
import tz.go.tcra.lims.reports.service.LicenceReportService;
import tz.go.tcra.lims.utils.Response;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/license-reports")
public class LicenseReportsController {

    private final LicenceReportService licenceReportService;
    private final DataExportService dataExportService;
    private final LicenceReportViewRepository licenceReportViewRepository;
    private final LicenceApplicationViewRepository licenceApplicationViewRepository;

    public LicenseReportsController(LicenceReportService licenceReportService, DataExportService dataExportService, LicenceReportViewRepository licenceReportViewRepository, LicenceApplicationViewRepository licenceApplicationViewRepository) {
        this.licenceReportService = licenceReportService;
        this.dataExportService = dataExportService;
        this.licenceReportViewRepository = licenceReportViewRepository;
        this.licenceApplicationViewRepository = licenceApplicationViewRepository;
    }


    @GetMapping(value = "/licences", name = "allLicenceGeneralReport")
    @PreAuthorize("hasRole('ROLE_VIEW_GENERAL_LICENCE_REPORT')")
    public Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceGeneralReport(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return licenceReportService.getAllLicenceGeneralReport(keyword, pageable);
    }

    @GetMapping(value = "/applications", name = "allLicenceApplicationsReport")
    @PreAuthorize("hasRole('ROLE_VIEW_GENERAL_APPLICATION_REPORT')")
    public Response<CollectionModel<EntityModel<LicenceApplicationView>>> getAllLicenceApplicationsReport(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return licenceReportService.getAllLicenceApplicationsReport(keyword, pageable);
    }

    @GetMapping(value = "/licence-stats", name = "getGeneralLicenceReportStats")
    @PreAuthorize("hasRole('ROLE_VIEW_LICENCE_STATS_REPORT')")
    public Response<EntityModel<LicenceGeneralStatsDto>> getGeneralLicenceReportStats() {
        return licenceReportService.getLicenceReportGeneralStats();
    }

    @GetMapping(value = "/appls-stats", name = "getGeneralLicenceApplicationsReportStats")
    @PreAuthorize("hasRole('ROLE_VIEW_LICENCE_APPL_STATS_REPORT')")
    public Response<EntityModel<LicenceApplicationStatsDto>> getGeneralLicenceApplicationsReportStats() {
        return licenceReportService.getLicenceApplicationsReportGeneralStats();
    }

    @GetMapping(value = "/all-appls-licences", name = "getAllLicenceAndApplications")
    @PreAuthorize("hasRole('ROLE_ALL_APPLICATIONS_LICENCES')")
    public Response<CollectionModel<EntityModel<Licence>>> getAllLicenceAndApplications(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return licenceReportService.getAllLicenceAndApplications(keyword, pageable);
    }

    @GetMapping(value = "/npg/licences", name = "getAllLicenceByLicenceState")
    @PreAuthorize("hasRole('ROLE_VIEW_GENERAL_LICENCE_REPORT')")
    public Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceByLicenceState(
            @RequestParam(name = "state", defaultValue = "") String licenceState) {
        return licenceReportService.getAllLicenceByLicenceState(licenceState);
    }

    @GetMapping(value = "/npg/applications", name = "getAllLicenceApplicationByCurrentStage")
    @PreAuthorize("hasRole('ROLE_VIEW_GENERAL_APPLICATION_REPORT')")
    public Response<CollectionModel<EntityModel<LicenceApplicationView>>> getAllLicenceApplicationByCurrentStage(
            @RequestParam(name = "stage", defaultValue = "") String applicationStage) {
        return licenceReportService.getAllLicenceApplicationByCurrentStage(applicationStage);
    }

    public Response<Licence> findLicenseDetailById() {
        return null;
    }

    @GetMapping(value = "/export/licences-xlsx")
    public ResponseEntity exportLicencesToExcel(String fileName) {
        List<LicenceView> licences = licenceReportViewRepository.findAll();

        ByteArrayInputStream inputStream = dataExportService.exportLicenceDataToExcel(licences);

        return exportReportsToExcel(fileName, inputStream);
    }

    @GetMapping("/export/applications-xlsx")
    public ResponseEntity exportApplicationsToExcel(String fileName) {
        List<LicenceApplicationView> licenceApplications = licenceApplicationViewRepository.findAll();

        ByteArrayInputStream inputStream = dataExportService.exportLicenceApplicationDataToExcel(licenceApplications);

        return exportReportsToExcel(fileName, inputStream);
    }


    //This method saves: exportLicencesToExcel & exportApplicationsToExcel methods to export data to excel
    @NotNull
    private ResponseEntity exportReportsToExcel(String fileName, ByteArrayInputStream inputStream) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateTime = dateFormatter.format(new Date());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + currentDateTime + ".xlsx\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(inputStream));
    }

    @GetMapping(value = "/licences/pageable", name = "getAllLicenceByLicenceStateByKeyword")
    @PreAuthorize("hasRole('ROLE_VIEW_GENERAL_LICENCE_REPORT')")
    public Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceByLicenceStateAndKeyWord(
            @RequestParam(name = "licenceState", defaultValue = "") String licenceState,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return licenceReportService.getAllLicenceByLicenceStateAndKeyWord(licenceState, keyword, pageable);
    }

    @GetMapping(value = "/main/applications", name = "getLicenceApplicationMainReport")
    @PreAuthorize("hasRole('ROLE_VIEW_GENERAL_APPLICATION_REPORT')")
    public Response<LicenceApplicationMainDto> getLicenceApplicationMainReport(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return licenceReportService.getLicenceApplicationMainReport(pageable);
    }

    @GetMapping(value = "/main/licences", name = "getLicenceMainReport")
    @PreAuthorize("hasRole('ROLE_VIEW_GENERAL_LICENCE_REPORT')")
    public Response<LicenceMainDto> getLicenceMainReport(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return licenceReportService.getLicenceMainReport(pageable);
    }

}
