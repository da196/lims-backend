package tz.go.tcra.lims.reports.dto;

import lombok.Data;

@Data
public class DashboardReportDto {

    private LicenseSummaryDto licensesummaryDto;
    private RevenueSummaryDto revenueSummaryDto;

    private ApplicationsSummaryDto applicationsSummaryDto;

}
