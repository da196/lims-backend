package tz.go.tcra.lims.reports.service;

import tz.go.tcra.lims.entity.views.PaymentReportView;
import tz.go.tcra.lims.utils.Response;

import java.util.List;

/**
 * @author DonaldSj
 */

public interface PaymentReportService {

    Response<List<PaymentReportView>> getAllPayments();

    Response<List<PaymentReportView>> getTodayPayments();

    Response<List<PaymentReportView>> getThisWeekPayments();

    Response<List<PaymentReportView>> getThisMonthPayments();

    Response<List<PaymentReportView>> getPaymentReportBetweenPayDate(String startDate, String endDate);

}
