package dev.abidino.export.export;

import dev.abidino.export.export.api.ExportRequest;
import dev.abidino.export.export.api.ExportResponse;
import dev.abidino.export.export.api.Filter;
import dev.abidino.export.export.api.RequestStatus;
import dev.abidino.export.export.entities.Request;
import dev.abidino.export.export.export.csv.AsyncExportService;
import dev.abidino.export.export.export.csv.CsvExportService;
import dev.abidino.export.export.export.excel.ExcelExportService;
import dev.abidino.export.export.service.QueryExecuteService;
import dev.abidino.export.export.service.RequestService;
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

    public ExportResponse export(ExportRequest exportRequest) {
        List<Filter> filters = exportRequest.query().filters();
        String query = exportRequest.query().query();
        Long dataCount = queryExecuteService.executeCountQuery(query, filters);

        if (dataCount == 0) {
            throw new RuntimeException("data not found");
        }

        Request request = requestService.save(exportRequest, dataCount);

        if (dataCount < 5) {
            ExportResponse exportResponse = excelExportService.createExcel(query, dataCount, 0L, request, filters);
            updateRequest(request, exportResponse);
            return exportResponse;
        } else if (dataCount < 7) {
            ExportResponse exportResponse = csvExportService.createCsv(query, dataCount, 0L, request, filters);
            updateRequest(request, exportResponse);
            return exportResponse;
        } else {
            return asyncExportService.startAsyncExport(query, request.getId(), filters);
        }
    }

    private void updateRequest(Request request, ExportResponse exportResponse) {
        request.addMediaId(exportResponse.mediaId());
        request.setRequestStatus(RequestStatus.DONE);
        requestService.save(request);
    }

    public void exportWithAsync(ExportEvent exportEvent) {
        asyncExportService.createCsv(exportEvent);
    }
}
