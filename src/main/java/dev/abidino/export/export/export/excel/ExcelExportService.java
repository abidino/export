package dev.abidino.export.export.export.excel;

import dev.abidino.export.FileUtil;
import dev.abidino.export.export.api.ExportType;
import dev.abidino.export.export.api.Filter;
import dev.abidino.export.export.entities.Request;
import dev.abidino.export.export.entities.TableHeader;
import dev.abidino.export.export.service.ColumnHeaderService;
import dev.abidino.export.export.service.QueryExecuteService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static dev.abidino.export.export.ExportConstant.BATCH_SIZE;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final ColumnHeaderService columnHeaderService;
    private final QueryExecuteService queryExecuteService;

    public String createExcel(String query, ExportType exportType, Long dataCount, Long currentOffset, Request request, List<Filter> filters) {
        TableHeader tableHeader = request.getTableHeader();
        List<String> headerList = columnHeaderService.getHeaderListByTableHeaderId(tableHeader.getId());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(tableHeader.getHeader());
        while (currentOffset < dataCount) {
            String paginatedQuery = query + " LIMIT " + BATCH_SIZE + " OFFSET " + currentOffset;
            List<List<Object>> lists = queryExecuteService.executeQuery(paginatedQuery, filters);
            addValues(lists, headerList, sheet);
            currentOffset += BATCH_SIZE;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FileUtil.convertByteArrayOutputStreamToFile(outputStream, FileUtil.createFileName(exportType.name(), ".xlsx"));
        return FileUtil.convertToBase64(outputStream);
    }

    private void addValues(List<List<Object>> records, List<String> columnHeaders, Sheet sheet) {
        try {
            if (sheet.getLastRowNum() == -1) {
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < columnHeaders.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnHeaders.get(i));
                }
            }

            int rowNum = sheet.getLastRowNum() + 1;
            for (List<Object> myRecord : records) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < myRecord.size(); i++) {
                    Cell cell = row.createCell(i);
                    Object data = myRecord.get(i);
                    if (data == null) {
                        cell.setCellValue((""));
                    } else if (data instanceof Number value) {
                        cell.setCellValue(value.doubleValue());
                    } else if (data instanceof Boolean value) {
                        cell.setCellValue(value);
                    } else {
                        cell.setCellValue(String.valueOf(data));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel dosyası oluşturulurken hata: " + e.getMessage());
        }
    }

}
