package dev.abidino.export.report.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GenericExcelExportService {
    public <T extends ExcelExportable> void createExcelForReport(List<T> records, Sheet sheet) {
        try {
            if (sheet.getLastRowNum() == -1) {
                String[] headers = records.get(0).excelHeaders();
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }
            }

            int rowNum = sheet.getLastRowNum() + 1;
            for (T myRecord : records) {
                Row row = sheet.createRow(rowNum++);
                Object[] data = myRecord.excelData();
                for (int i = 0; i < data.length; i++) {
                    Cell cell = row.createCell(i);
                    if (data[i] == null) {
                        cell.setCellValue((""));
                    } else if (data[i] instanceof Number value) {
                        cell.setCellValue(value.doubleValue());
                    } else if (data[i] instanceof Boolean value) {
                        cell.setCellValue(value);
                    } else {
                        cell.setCellValue(String.valueOf(data[i]));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel dosyası oluşturulurken hata: " + e.getMessage());
        }
    }

}