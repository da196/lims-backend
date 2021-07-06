package tz.go.tcra.lims.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.entity.views.LicenceApplicationStatsView;
import tz.go.tcra.lims.entity.views.LicenceApplicationView;
import tz.go.tcra.lims.entity.views.LicenceGeneralStatsView;
import tz.go.tcra.lims.entity.views.LicenceView;
import tz.go.tcra.lims.licence.repository.LicenceRepository;
import tz.go.tcra.lims.reports.assemblers.LicenceApplicationReportAssembler;
import tz.go.tcra.lims.reports.assemblers.LicenceApplicationsStatsAssembler;
import tz.go.tcra.lims.reports.assemblers.LicenceGeneralReportAssembler;
import tz.go.tcra.lims.reports.assemblers.LicenceGeneralStatsAssembler;
import tz.go.tcra.lims.reports.dto.LicenceApplicationMainDto;
import tz.go.tcra.lims.reports.dto.LicenceApplicationStatsDto;
import tz.go.tcra.lims.reports.dto.LicenceGeneralStatsDto;
import tz.go.tcra.lims.reports.dto.LicenceMainDto;
import tz.go.tcra.lims.reports.repository.LicenceApplicationStatsViewRepository;
import tz.go.tcra.lims.reports.repository.LicenceApplicationViewRepository;
import tz.go.tcra.lims.reports.repository.LicenceGeneralStatsViewRepository;
import tz.go.tcra.lims.reports.repository.LicenceReportViewRepository;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

import java.util.List;

/**
 * @author DonaldSj
 */

@Service
@Slf4j
public class LicenceReportServiceImpl implements LicenceReportService {

    private final LicenceReportViewRepository licenceReportViewRepository;

    private final LicenceApplicationViewRepository applicationViewRepository;

    private final LicenceGeneralStatsViewRepository licenceGeneralStatsViewRepository;

    private final LicenceApplicationStatsViewRepository applicationStatsViewRepository;

    private final LicenceRepository licenseRepository;

    private final LicenceGeneralReportAssembler licenceGeneralReportAssembler;

    private final LicenceApplicationReportAssembler licenceApplicationReportAssembler;

    private final LicenceGeneralStatsAssembler licenceGeneralStatsAssembler;

    private final LicenceApplicationsStatsAssembler applicationsStatsAssembler;

    private final PagedResourcesAssembler<LicenceView> pagedLicenceViewAssembler;

    private final PagedResourcesAssembler<LicenceApplicationView> pagedLicenceApplicationAssembler;

    private final PagedResourcesAssembler<Licence> pagedLicenceAssembler;

    private final AppUtility utility;

    public LicenceReportServiceImpl(LicenceApplicationViewRepository applicationViewRepository, LicenceReportViewRepository licenceReportViewRepository, LicenceGeneralStatsViewRepository licenceGeneralStatsViewRepository, LicenceApplicationStatsViewRepository applicationStatsViewRepository, LicenceRepository licenseRepository, PagedResourcesAssembler<Licence> pagedLicenceAssembler, AppUtility utility, LicenceGeneralReportAssembler licenceGeneralReportAssembler, LicenceApplicationReportAssembler licenceApplicationReportAssembler, LicenceGeneralStatsAssembler licenceGeneralStatsAssembler, LicenceApplicationsStatsAssembler applicationsStatsAssembler, PagedResourcesAssembler<LicenceView> pagedLicenceViewAssembler, PagedResourcesAssembler<LicenceApplicationView> pagedLicenceApplicationAssembler) {
        this.applicationViewRepository = applicationViewRepository;
        this.licenceReportViewRepository = licenceReportViewRepository;
        this.licenceGeneralStatsViewRepository = licenceGeneralStatsViewRepository;
        this.applicationStatsViewRepository = applicationStatsViewRepository;
        this.licenseRepository = licenseRepository;
        this.pagedLicenceAssembler = pagedLicenceAssembler;
        this.utility = utility;
        this.licenceGeneralReportAssembler = licenceGeneralReportAssembler;
        this.licenceApplicationReportAssembler = licenceApplicationReportAssembler;
        this.licenceGeneralStatsAssembler = licenceGeneralStatsAssembler;
        this.applicationsStatsAssembler = applicationsStatsAssembler;
        this.pagedLicenceViewAssembler = pagedLicenceViewAssembler;
        this.pagedLicenceApplicationAssembler = pagedLicenceApplicationAssembler;
    }

    @Override
    public Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceGeneralReport(String keyword,
                                                                                          Pageable pageable) {
        Page<LicenceView> licenceReport;
        if (keyword.trim().isEmpty()) {
            licenceReport = licenceReportViewRepository.findAll(pageable);
            log.info(utility.getUser().getEmail() + " - Accessed licence general report");
        } else {
            licenceReport = licenceReportViewRepository.findByKeyword(keyword, pageable);
        }
        return new Response<>(ResponseCode.SUCCESS, true, "Licence report",
                pagedLicenceViewAssembler.toModel(licenceReport));
    }

    @Override
    public Response<CollectionModel<EntityModel<LicenceApplicationView>>> getAllLicenceApplicationsReport(
            String keyword, Pageable pageable) {
        Page<LicenceApplicationView> licenceApplicationReport;
        if (keyword.trim().isEmpty()) {
            licenceApplicationReport = applicationViewRepository.findAll(pageable);
        } else {
            licenceApplicationReport = applicationViewRepository.findByKeyword(keyword, pageable);
        }
        return new Response<>(ResponseCode.SUCCESS, true, "",
                pagedLicenceApplicationAssembler.toModel(licenceApplicationReport));
    }

    @Override
    public Response<EntityModel<LicenceGeneralStatsDto>> getLicenceReportGeneralStats() {
        LicenceGeneralStatsDto licenceGeneralStats = new LicenceGeneralStatsDto();
        try {
            LicenceGeneralStatsView licenceGeneralStat = licenceGeneralStatsViewRepository
                    .findFirstByOrderByTotalLicencesAsc();
            // CANCELLED, SUSPENDED, EXPIRED,ACTIVE,APPLICATION
            licenceGeneralStats.setTotalLicences(licenceGeneralStat.getTotalLicences());
            licenceGeneralStats.setActiveLicense(licenceGeneralStat.getActiveLicense());
            licenceGeneralStats.setExpiredLicense(licenceGeneralStat.getExpiredLicense());
            licenceGeneralStats.setCancelledLicense(licenceGeneralStat.getCancelledLicense());
            licenceGeneralStats.setNewLicences(licenceGeneralStat.getNewLicences());
            licenceGeneralStats.setSuspendedLicense(licenceGeneralStat.getSuspendedLicense());

            log.info("Licence stats retrieved successfully..");
            return new Response<>(ResponseCode.SUCCESS, true, "Licence statistics..",
                    licenceGeneralStatsAssembler.toModel(licenceGeneralStats));
        } catch (Exception e) {
            log.error("Exception error occurred, could not fetch licence stats because of: " + e.getMessage());
            e.printStackTrace();
            return new Response<>(ResponseCode.FAILURE, false, "Internal error occurred, could not get licence stats",
                    null);
        }
    }

    @Override
    public Response<EntityModel<LicenceApplicationStatsDto>> getLicenceApplicationsReportGeneralStats() {
        LicenceApplicationStatsDto applicationGeneralStats = new LicenceApplicationStatsDto();
        try {
            LicenceApplicationStatsView applicationStat = applicationStatsViewRepository
                    .findFirstByOrderByTotalApplicationsAsc();
            applicationGeneralStats.setTotalApplications(applicationStat.getTotalApplications());
            applicationGeneralStats.setNewApplications(applicationStat.getNewApplications());
            applicationGeneralStats.setInProgressApplications(applicationStat.getInProgressApplications());
            applicationGeneralStats.setUnderMinistryApplications(applicationStat.getUnderMinistryApplications());
            return new Response<>(ResponseCode.SUCCESS, true, "Licence application statistics..",
                    applicationsStatsAssembler.toModel(applicationGeneralStats));
        } catch (Exception e) {
            log.error("Exception error occurred, could not fetch application stats because of: " + e.getMessage());
            e.printStackTrace();
            return new Response<>(ResponseCode.FAILURE, false, "Internal error occurred, could not get stats",
                    null);
        }
    }

    /*
     * This function returns all applications and licence in raw. no any makeup!! It
     * gives ability to search by keyword and page.
     */
    @Override
    public Response<CollectionModel<EntityModel<Licence>>> getAllLicenceAndApplications(String keyword,
                                                                                        Pageable pageable) {
        Page<Licence> allLicenceAndApplications;

        try {
            if (keyword.trim().isEmpty()) {
                allLicenceAndApplications = licenseRepository.findAll(pageable);
            } else {
                allLicenceAndApplications = licenseRepository.findByKeyword(keyword, pageable);
            }
            return new Response<>(ResponseCode.SUCCESS, true, "All licence and applications",
                    pagedLicenceAssembler.toModel(allLicenceAndApplications));
        } catch (Exception e) {
            return new Response<>(ResponseCode.FAILURE, false, "", null);
        }
    }

    /*
     * This function takes the following params and returns respective licence data
     * CANCELLED, SUSPENDED, EXPIRED,ACTIVE,APPLICATION default is "". any case.
     */
    @Override
    public Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceByLicenceState(String licenceState) {
        try {
            if (!licenceState.trim().isEmpty()) {
                List<LicenceView> allLicenceByState = licenceReportViewRepository.findByLicenceState(licenceState);
                return new Response<>(ResponseCode.SUCCESS, true, "",
                        licenceGeneralReportAssembler.toCollectionModel(allLicenceByState));
            } else {
                List<LicenceView> allLicence = licenceReportViewRepository.findAll();
                return new Response<>(ResponseCode.SUCCESS, true, "All licences of all states",
                        licenceGeneralReportAssembler.toCollectionModel(allLicence));
            }
        } catch (Exception e) {
            log.error(utility.getUser().getEmail() + " Could not get licence data, reason: " + e.getMessage());
            e.printStackTrace();
            return new Response<>(ResponseCode.FAILURE, false, "system internal error occurred", null);
        }
    }

    /*
     * This function takes the following params and returns respective application
     * data NEW, INPROGRESS, ATMINISTRY, CANCELLED, GRANTED default is "". any case
     */
    @Override
    public Response<CollectionModel<EntityModel<LicenceApplicationView>>> getAllLicenceApplicationByCurrentStage(
            String applicationStage) {
        try {
            if (applicationStage != null && !applicationStage.isEmpty()) {
                List<LicenceApplicationView> allLicenceApplByStage = applicationViewRepository
                        .findByApplicationStage(applicationStage);
                return new Response<>(ResponseCode.SUCCESS, true, "licence applications by stages",
                        licenceApplicationReportAssembler.toCollectionModel(allLicenceApplByStage));
            } else {
                List<LicenceApplicationView> allLicenceApplications = applicationViewRepository.findAll();
                return new Response<>(ResponseCode.SUCCESS, true, "All licence applications of all stages",
                        licenceApplicationReportAssembler.toCollectionModel(allLicenceApplications));
            }
        } catch (Exception e) {
            log.error(utility.getUser().getEmail() + " Could not get application data, reason: " + e.getMessage());
            e.printStackTrace();
            return new Response<>(ResponseCode.FAILURE, false, "system internal error occurred", null);
        }
    }

    @Override
    public Response<LicenceApplicationMainDto> getLicenceApplicationMainReport(Pageable pageable) {

        try {
            LicenceApplicationStatsDto applicationGeneralStats = new LicenceApplicationStatsDto();

            LicenceApplicationStatsView applicationStat = applicationStatsViewRepository
                    .findFirstByOrderByTotalApplicationsAsc();
            applicationGeneralStats.setTotalApplications(applicationStat.getTotalApplications());
            applicationGeneralStats.setNewApplications(applicationStat.getNewApplications());
            applicationGeneralStats.setInProgressApplications(applicationStat.getInProgressApplications());
            applicationGeneralStats.setUnderMinistryApplications(applicationStat.getUnderMinistryApplications());

            LicenceApplicationMainDto licenceApplications = new LicenceApplicationMainDto();

            licenceApplications.setApplicationStats(applicationGeneralStats);
            licenceApplications.setAllApplications(applicationViewRepository.findAll(pageable));
            licenceApplications.setNewApplications(applicationViewRepository.findByApplicationStagePageable("NEW", pageable));
            licenceApplications.setInProgressApplications(applicationViewRepository.findByApplicationStagePageable("INPROGRESS", pageable));
            licenceApplications.setAtMinistryApplications(applicationViewRepository.findByApplicationStagePageable("ATMINISTRY", pageable));
            licenceApplications.setCancelledApplications(applicationViewRepository.findByApplicationStagePageable("CANCELLED", pageable));
            return new Response<>(ResponseCode.SUCCESS, true, "Success response", licenceApplications);

        } catch (Exception e) {
            log.error("exception error occurred, could not get reports: " + e.getMessage());
            e.printStackTrace();
            return new Response<>(ResponseCode.FAILURE, false, "failure response", null);
        }
    }

    @Override
    public Response<LicenceMainDto> getLicenceMainReport(Pageable pageable) {
        try {
            LicenceGeneralStatsDto licenceGeneralStats = new LicenceGeneralStatsDto();
            LicenceGeneralStatsView licenceGeneralStatsRepo = licenceGeneralStatsViewRepository.findFirstByOrderByTotalLicencesAsc();
            // CANCELLED, SUSPENDED, EXPIRED,ACTIVE,APPLICATION
            licenceGeneralStats.setTotalLicences(licenceGeneralStatsRepo.getTotalLicences());
            licenceGeneralStats.setActiveLicense(licenceGeneralStatsRepo.getActiveLicense());
            licenceGeneralStats.setExpiredLicense(licenceGeneralStatsRepo.getExpiredLicense());
            licenceGeneralStats.setCancelledLicense(licenceGeneralStatsRepo.getCancelledLicense());
            licenceGeneralStats.setNewLicences(licenceGeneralStatsRepo.getNewLicences());
            licenceGeneralStats.setSuspendedLicense(licenceGeneralStatsRepo.getSuspendedLicense());

            LicenceMainDto licenceReport = new LicenceMainDto();
            licenceReport.setLicenceStatistics(licenceGeneralStats);
            licenceReport.setAllLicenses(licenceReportViewRepository.findAll(pageable));
            licenceReport.setActiveLicenses(licenceReportViewRepository.findByLicenceState("ACTIVE", pageable));
            licenceReport.setCancelledLicenses(licenceReportViewRepository.findByLicenceState("CANCELLED", pageable));
            licenceReport.setExpiredLicenses(licenceReportViewRepository.findByLicenceState("EXPIRED", pageable));
            licenceReport.setSuspendedLicenses(licenceReportViewRepository.findByLicenceState("SUSPENDED", pageable));

            log.info("Licence stats retrieved successfully..");
            return new Response<>(ResponseCode.SUCCESS, true, "Success response", licenceReport);

        } catch (Exception e) {
            log.error("Exception error occurred, could not fetch licence report: " + e.getMessage());
            e.printStackTrace();
            return new Response<>(ResponseCode.FAILURE, false, "Exception error occurred", null);
        }
    }

    private List<LicenceView> getStatisticsByState(String licenceState) {
        List<LicenceView> licenceStats;
        if (licenceState.equalsIgnoreCase("ALL")) {
            licenceStats = licenceReportViewRepository.findAll();
        } else {
            licenceStats = licenceReportViewRepository.findByLicenceState(licenceState);
        }
        return licenceStats;
    }

    @Override
    public Response<CollectionModel<EntityModel<LicenceView>>> getAllLicenceByLicenceStateAndKeyWord(
            String licenceState, String keyword, Pageable pageable) {

        Page<LicenceView> allLicenceByState;
        try {
            if (!licenceState.trim().isEmpty()) {
                if (keyword.trim().isEmpty()) {

                    allLicenceByState = licenceReportViewRepository.findByLicenceState(licenceState, pageable);
                    return new Response<>(ResponseCode.SUCCESS, true, "",
                            pagedLicenceViewAssembler.toModel(allLicenceByState));

                } else {

                    allLicenceByState = licenceReportViewRepository.findByLicenceStateAndKeyword(licenceState, keyword,
                            pageable);
                    return new Response<>(ResponseCode.SUCCESS, true, "",
                            pagedLicenceViewAssembler.toModel(allLicenceByState));

                }
            } else {
                if (keyword.trim().isEmpty()) {

                    allLicenceByState = licenceReportViewRepository.findAll(pageable);
                    return new Response<>(ResponseCode.SUCCESS, true, "",
                            pagedLicenceViewAssembler.toModel(allLicenceByState));

                } else {

                    allLicenceByState = licenceReportViewRepository.findByKeyword(keyword, pageable);
                    return new Response<>(ResponseCode.SUCCESS, true, "",
                            pagedLicenceViewAssembler.toModel(allLicenceByState));
                }
            }
        } catch (Exception e) {
            log.error(utility.getUser().getEmail() + " Could not get licence data, reason: " + e.getMessage());
            e.printStackTrace();
            return new Response<>(ResponseCode.FAILURE, false, "system internal error occurred", null);
        }
    }
}
