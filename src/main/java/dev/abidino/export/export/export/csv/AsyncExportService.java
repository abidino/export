package dev.abidino.export.export.export.csv;

import com.google.gson.Gson;
import dev.abidino.export.export.api.ExportType;
import dev.abidino.export.export.api.Filter;
import dev.abidino.export.export.entities.Request;
import dev.abidino.export.export.service.RequestService;
import dev.abidino.export.kafka.ExportEvent;
import dev.abidino.export.kafka.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static dev.abidino.export.export.ExportConstant.LIMIT_FOR_BIG_DATA;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncExportService {

    private final CsvExportService csvExportService;
    private final Producer producer;
    private final RequestService requestService;

    public void exportWithKafka(ExportEvent exportEvent) {
        Optional<Request> optionalRequest = requestService.findById(exportEvent.getRequestId());
        if (optionalRequest.isEmpty()) {
            return;
        }
        Request request = optionalRequest.get();
        csvExportService.createCsv(exportEvent.getQuery(), exportEvent.getExportType(), exportEvent.getLimit(), exportEvent.getOffset(), request, exportEvent.getFilters());
        Long newOffset = exportEvent.getOffset() + exportEvent.getLimit();
        if (request.getDataCount() > newOffset) {
            sendMessage(request.getId(), exportEvent.getQuery(), exportEvent.getExportType(), newOffset, exportEvent.getLimit(), exportEvent.getFilters());
            return;
        }
        requestService.updateStatus(request.getId(), "DONE");
        log.info("all file were created");
    }

    public String startAsyncExport(String query, ExportType exportType, Long requestId, List<Filter> filterList) {
        sendMessage(requestId, query, exportType, 0L, LIMIT_FOR_BIG_DATA, filterList);
        return "Isleme alinmistir : requestId " + requestId;
    }

    private void sendMessage(Long requestId, String query, ExportType exportType, Long offset, Long limit, List<Filter> filterList) {
        Gson gson = new Gson();
        ExportEvent exportEvent = new ExportEvent(requestId, query, offset, exportType, limit, filterList);
        String json = gson.toJson(exportEvent);
        producer.sendMessage(json);
    }


}
