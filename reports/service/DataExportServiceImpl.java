package tz.go.tcra.lims.reports.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.entity.views.LicenceApplicationView;
import tz.go.tcra.lims.entity.views.LicenceView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author DonaldSj
 */

@Service
public class DataExportServiceImpl implements DataExportService {

    public DataExportServiceImpl() {
    }

    @Override
    public ByteArrayInputStream exportLicenceDataToExcel(List<LicenceView> licences) {
        String[] COLUMNS = {"SN", "Licence Owner", "Application Date", "Issue Date", "Licence Number", "Licence Product", "Application Type", "State", "Duration", "Expire Date", "Days to Expiry"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Licences");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNS[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // CellStyle for Age
            CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 1;
            for (LicenceView licence : licences) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(licence.getId());
                row.createCell(1).setCellValue(licence.getApplicantEntityName());
                row.createCell(2).setCellValue(licence.getApplicationDate());
                row.createCell(3).setCellValue(licence.getIssuedDate());
                row.createCell(4).setCellValue(licence.getLicenceNumber());
                row.createCell(5).setCellValue(licence.getLicenseProduct());
                row.createCell(6).setCellValue(licence.getApplicationType());
                row.createCell(7).setCellValue(licence.getLicenceState());
                row.createCell(8).setCellValue(licence.getDuration());
                row.createCell(9).setCellValue(licence.getExpireDate());
                row.createCell(10).setCellValue(licence.getDaysLeftToExpire());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    @Override
    public ByteArrayInputStream exportLicenceApplicationDataToExcel(List<LicenceApplicationView> licenceApplications) {
        String[] COLUMNS = {"SN", "Licence Owner", "Application Date", "Licence Product", "Application Type", "State", "Duration", "Expire Date", "Days to Expiry"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Licence Applications");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNS[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // CellStyle for Age
            CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 1;
            for (LicenceApplicationView licenceApplication : licenceApplications) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(licenceApplication.getId());
                row.createCell(1).setCellValue(licenceApplication.getApplicantEntityName());
                row.createCell(2).setCellValue(licenceApplication.getApplicationDate());
                row.createCell(3).setCellValue(licenceApplication.getLicenseProduct());
                row.createCell(4).setCellValue(licenceApplication.getApplicationType());
                row.createCell(5).setCellValue(licenceApplication.getLicenceState());
                row.createCell(6).setCellValue(licenceApplication.getDuration());
                row.createCell(7).setCellValue(licenceApplication.getExpireDate());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }
}
