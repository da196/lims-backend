package tz.go.tcra.lims.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.entity.views.PaymentReportView;
import tz.go.tcra.lims.utils.Response;

import java.util.List;

/**
 * @author DonaldSj
 */

@Service
@Slf4j
public class PaymentReportServiceImpl implements PaymentReportService {
    @Override
    public Response<List<PaymentReportView>> getAllPayments() {
        return null;
    }

    @Override
    public Response<List<PaymentReportView>> getTodayPayments() {
        return null;
    }

    @Override
    public Response<List<PaymentReportView>> getThisWeekPayments() {
        return null;
    }

    @Override
    public Response<List<PaymentReportView>> getThisMonthPayments() {
        return null;
    }

    @Override
    public Response<List<PaymentReportView>> getPaymentReportBetweenPayDate(String startDate, String endDate) {
        return null;
    }
}
