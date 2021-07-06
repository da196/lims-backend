package tz.go.tcra.lims.reports.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.entity.views.PaymentReportView;
import tz.go.tcra.lims.utils.Response;

import java.util.List;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/payment-reports")
public class PaymentReportServiceController {

    @GetMapping(value = "/all-payments", name = "getAllPayments")
    public Response<List<PaymentReportView>> getAllPayments(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return null;
    }

    @GetMapping(value = "/today", name = "getTodayPayments")
    public Response<List<PaymentReportView>> getTodayPayments() {
        return null;
    }

    @GetMapping(value = "/this-week", name = "getThisWeekPayments")
    public Response<List<PaymentReportView>> getThisWeekPayments() {
        return null;
    }

    @GetMapping(value = "/this-month", name = "getThisMonthPayments")
    public Response<List<PaymentReportView>> getThisMonthPayments() {
        return null;
    }

    @GetMapping(value = "")
    public Response<List<PaymentReportView>> getPaymentReportBetweenPayDate(
            @RequestParam(name = "from") String startDate,
            @RequestParam(name = "to") String endDate) {
        return null;
    }
}
