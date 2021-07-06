package tz.go.tcra.lims.reports.service;

import tz.go.tcra.lims.entity.views.LicenceApplicationView;
import tz.go.tcra.lims.entity.views.LicenceView;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author DonaldSj
 */

public interface DataExportService {

//    void writeHeaderLine();
//
//    void createCell(Row row, int columnCount, Object value, CellStyle style);
//
//    void writeDataLines(List<LicenceView> dataList);
//
//    void export(HttpServletResponse response) throws IOException;

    ByteArrayInputStream exportLicenceDataToExcel(List<LicenceView> licences);

    ByteArrayInputStream exportLicenceApplicationDataToExcel(List<LicenceApplicationView> licenceApplications);
}
