package dev.abidino.export.export.export.csv;

import dev.abidino.export.FileUtil;
import dev.abidino.export.export.api.Filter;
import dev.abidino.export.export.api.TableHeaderSubType;
import dev.abidino.export.export.entities.Request;
import dev.abidino.export.export.entities.TableHeader;
import dev.abidino.export.export.service.ColumnHeaderService;
import dev.abidino.export.export.service.QueryExecuteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import static dev.abidino.export.export.ExportConstant.BATCH_SIZE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvExportService {
    private final ColumnHeaderService columnHeaderService;
    private final QueryExecuteService queryExecuteService;

    public String createCsv(String query, TableHeaderSubType exportType, Long dataCount, Long currentOffset, Request request, List<Filter> filters) {
        TableHeader tableHeader = request.getTableHeader();
        List<String> headerList = columnHeaderService.getHeaderListByTableHeaderId(tableHeader.getId());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        CSVPrinter csvPrinter;

        try {
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long lastValue = dataCount + currentOffset;

        try {
            csvPrinter.printRecord(headerList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (currentOffset < lastValue) {
            String paginatedQuery = query + " LIMIT " + BATCH_SIZE + " OFFSET " + currentOffset;
            List<List<Object>> lists = queryExecuteService.executeQuery(paginatedQuery, filters);
            addValues(lists, csvPrinter);
            currentOffset += BATCH_SIZE;
        }

        try {
            csvPrinter.close();
            writer.close();
            outputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        FileUtil.convertByteArrayOutputStreamToFile(outputStream, FileUtil.createFileName(exportType.name(), ".csv"));
        return FileUtil.convertToBase64(outputStream);
    }

    private void addValues(List<List<Object>> records, CSVPrinter csvPrinter) {
        if (records == null || records.isEmpty()) {
            return;
        }
        try {
            records.forEach(data -> {
                try {
                    csvPrinter.printRecord(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            csvPrinter.flush();
        } catch (Exception e) {
            throw new RuntimeException("CSV dosyası oluşturulurken hata: " + e.getMessage());
        }
    }
}
