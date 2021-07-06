package tz.go.tcra.lims.reports.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationsSummaryDto {
    private int totalApplications;
    private int newApplications;
    private int inprogressApplications;
    private int underMinistryApplications;
}
