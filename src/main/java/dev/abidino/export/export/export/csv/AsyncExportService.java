package dev.abidino.export.export.export.csv;

import com.google.gson.Gson;
import dev.abidino.export.export.api.ExportResponse;
import dev.abidino.export.export.api.ExportStrategyType;
import dev.abidino.export.export.api.Filter;
import dev.abidino.export.export.api.RequestStatus;
import dev.abidino.export.export.entities.Request;
import dev.abidino.export.export.service.RequestService;
import dev.abidino.export.kafka.ExportEvent;
import dev.abidino.export.kafka.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.abidino.export.export.ExportConstant.LIMIT_FOR_BIG_DATA;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncExportService {

    private final CsvExportService csvExportService;
    private final Producer producer;
    private final RequestService requestService;

    public void createCsv(ExportEvent exportEvent) {
        Request request = requestService.findById(exportEvent.getRequestId());
        ExportResponse exportResponse = csvExportService.createCsv(exportEvent.getQuery(), exportEvent.getLimit(), exportEvent.getOffset(), request, exportEvent.getFilters());
        request.addMediaId(exportResponse.mediaId());
        requestService.update(request);
        Long newOffset = exportEvent.getOffset() + exportEvent.getLimit();
        if (request.getDataCount() > newOffset) {
            sendMessage(request.getId(), exportEvent.getQuery(), newOffset, exportEvent.getLimit(), exportEvent.getFilters());
            return;
        }
        requestService.updateStatus(request.getId(), RequestStatus.DONE);
        log.info("all file were created");
    }

    public ExportResponse startAsyncExport(String query, Long requestId, List<Filter> filterList) {
        sendMessage(requestId, query, 0L, LIMIT_FOR_BIG_DATA, filterList);
        return new ExportResponse(null, null, true, "export successfully started", requestId, ExportStrategyType.ASYNC);
    }

    private void sendMessage(Long requestId, String query, Long offset, Long limit, List<Filter> filterList) {
        Gson gson = new Gson();
        ExportEvent exportEvent = new ExportEvent(requestId, query, offset, limit, filterList);
        String json = gson.toJson(exportEvent);
        producer.sendMessage(json);
    }
}
