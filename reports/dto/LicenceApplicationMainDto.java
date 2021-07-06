package tz.go.tcra.lims.reports.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import tz.go.tcra.lims.entity.views.LicenceApplicationView;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
public class LicenceApplicationMainDto {
    private LicenceApplicationStatsDto applicationStats;
    private Page<LicenceApplicationView> allApplications;
    private Page<LicenceApplicationView> newApplications;
    private Page<LicenceApplicationView> inProgressApplications;
    private Page<LicenceApplicationView> atMinistryApplications;
    private Page<LicenceApplicationView> cancelledApplications;
}
