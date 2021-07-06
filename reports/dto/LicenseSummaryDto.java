package tz.go.tcra.lims.reports.dto;

import lombok.Data;

@Data
public class LicenseSummaryDto {
    private int activeLicense;
    private int expiredLicense;
    private int suspendedLicense;
    private int cancelledLicense;
    private int totalLicense;
    private Double qoq;
    private String qoqSign;
    private Double mom;
    private String momSign;

}
