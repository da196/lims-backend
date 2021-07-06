package tz.go.tcra.lims.reports.dto;

import lombok.Data;

@Data
public class LicenceeDashBoardDto {
    private int activeLicense;

    private int expiredLicense;
    private int invoice;
    private int inprogress;

}
