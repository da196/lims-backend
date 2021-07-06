package tz.go.tcra.lims.reports.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import tz.go.tcra.lims.entity.views.LicenceView;

/**
 * @author DonaldSj
 */

@Getter
@Setter
public class LicenceMainDto {
    private LicenceGeneralStatsDto licenceStatistics;
    private Page<LicenceView> allLicenses;
    private Page<LicenceView> activeLicenses;
    private Page<LicenceView> expiredLicenses;
    private Page<LicenceView> cancelledLicenses;
    private Page<LicenceView> suspendedLicenses;
}
