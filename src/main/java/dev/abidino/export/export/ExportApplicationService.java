package dev.abidino.export.export;

import dev.abidino.export.export.api.ExportType;
import dev.abidino.export.export.entities.Request;
import dev.abidino.export.export.entities.TableHeader;
import dev.abidino.export.export.export.csv.AsyncExportService;
import dev.abidino.export.export.export.csv.CsvExportService;
import dev.abidino.export.export.export.excel.ExcelExportService;
import dev.abidino.export.export.service.QueryExecuteService;
import dev.abidino.export.export.service.RequestService;
import dev.abidino.export.export.service.TableHeaderService;
import dev.abidino.export.kafka.ExportEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportApplicationService {
    private final QueryExecuteService queryExecuteService;
    private final ExcelExportService excelExportService;
    private final CsvExportService csvExportService;
    private final AsyncExportService asyncExportService;
    private final RequestService requestService;
    private final TableHeaderService tableHeaderService;


    public String export(String query, ExportType exportType) {

        Long dataCount = queryExecuteService.executeCountQuery(query);
        log.info("count is {}", dataCount);

        TableHeader tableHeader = tableHeaderService.getTableHeaderByExportType(exportType);
        Request request = createRequest(query, dataCount, tableHeader);
        Request savedRequest = requestService.save(request);

        if (dataCount < 5) {
            String excel = excelExportService.createExcel(query, exportType, dataCount, 0L, savedRequest);
            requestService.updateStatus(savedRequest.getId(), "DONE");
            return excel;
        } else if (dataCount < 7) {
            String csv = csvExportService.createCsv(query, exportType, dataCount, 0L, savedRequest);
            requestService.updateStatus(savedRequest.getId(), "DONE");
            return csv;
        }
        return asyncExportService.startAsyncExport(query, exportType, request.getId());
    }

    public void exportWithAsync(ExportEvent exportEvent) {
        asyncExportService.exportWithKafka(exportEvent);
    }


    private Request createRequest(String query, Long dataCount, TableHeader tableHeader) {
        Request request = new Request();
        request.setTableHeader(tableHeader);
        request.setDataCount(dataCount.intValue());
        request.setFilters(query);
        request.setRequestStatus("INPROGRESS");
        return requestService.save(request);
    }

}
