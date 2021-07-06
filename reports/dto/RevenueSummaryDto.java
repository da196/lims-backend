package tz.go.tcra.lims.reports.dto;

import lombok.Data;

@Data
public class RevenueSummaryDto {
    private Double totalAmount;

    private Double qoq;
    private String qoqSign;
    private Double mom;
    private String momSign;
    private Double MonthlyAmount;
    private Double WeeklyAmount;
    private Double dailyAmount;

}
