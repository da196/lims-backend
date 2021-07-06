package tz.go.tcra.lims.reports.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
public class LicenceApplicationStatsDto {
    private Long totalApplications;
    private Long newApplications;
    private Long inProgressApplications;
    private Long underMinistryApplications;
}
