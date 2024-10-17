package dev.abidino.export.export;

import dev.abidino.export.export.api.ExportRequest;
import dev.abidino.export.export.api.Filter;
import dev.abidino.export.export.api.TableHeaderSubType;
import dev.abidino.export.export.api.TableHeaderType;
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

import java.util.List;

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


    public String export(ExportRequest exportRequest) {
        List<Filter> filters = exportRequest.filters();
        String query = exportRequest.query();
        TableHeaderSubType tableHeaderSubType = exportRequest.tableHeaderSubType();
        TableHeaderType tableHeaderType = exportRequest.tableHeaderType();

        Long dataCount = queryExecuteService.executeCountQuery(query, filters);

        log.info("count is {}", dataCount);
        if (dataCount == 0) {
            return "data not found";
        }

        TableHeader tableHeader = tableHeaderService.getTableHeaderByExportType(tableHeaderType, tableHeaderSubType);
        Request request = createRequest(query, dataCount, tableHeader);
        Request savedRequest = requestService.save(request);

        if (dataCount < 5) {
            String excel = excelExportService.createExcel(query, tableHeaderSubType, dataCount, 0L, savedRequest, filters);
            requestService.updateStatus(savedRequest.getId(), "DONE");
            return excel;
        } else if (dataCount < 7) {
            String csv = csvExportService.createCsv(query, tableHeaderSubType, dataCount, 0L, savedRequest, filters);
            requestService.updateStatus(savedRequest.getId(), "DONE");
            return csv;
        } else {
            return asyncExportService.startAsyncExport(query, tableHeaderSubType, request.getId(), filters);
        }
    }

    public void exportWithAsync(ExportEvent exportEvent) {
        asyncExportService.exportWithKafka(exportEvent);
    }


    private Request createRequest(String query, Long dataCount, TableHeader tableHeader) {
        Request request = new Request();
        request.setTableHeader(tableHeader);
        request.setDataCount(dataCount.intValue());
        request.setFilters(query);
        request.setRequestStatus("IN_PROGRESS");
        return requestService.save(request);
    }

}
