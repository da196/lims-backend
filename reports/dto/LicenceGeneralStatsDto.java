package tz.go.tcra.lims.reports.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Getter
@Setter
public class LicenceGeneralStatsDto {
    //status: ALL, CANCELLED, SUSPENDED, EXPIRED,ACTIVE,APPLICATION
    private Long totalLicences;
    private Long newLicences;
    private Long activeLicense;
    private Long expiredLicense;
    private Long suspendedLicense;
    private Long cancelledLicense;
}
