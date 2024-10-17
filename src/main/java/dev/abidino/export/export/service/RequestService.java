package dev.abidino.export.export.service;

import dev.abidino.export.export.api.ExportRequest;
import dev.abidino.export.export.api.RequestStatus;
import dev.abidino.export.export.entities.ColumnHeader;
import dev.abidino.export.export.entities.Request;
import dev.abidino.export.export.entities.TableHeader;
import dev.abidino.export.export.repo.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final TableHeaderService tableHeaderService;
    private final ColumnHeaderService columnHeaderService;

    public Request findById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public Request update(Request request) {
        return requestRepository.save(request);
    }


    public Request save(ExportRequest exportRequest, Long dataCount) {
        TableHeader tableHeader = tableHeaderService.getTableHeaderByExportType(exportRequest.tableHeaderType(), exportRequest.tableHeaderSubType());
        List<ColumnHeader> headerListByTableHeaderId = columnHeaderService.getColumnHeaderListByTableHeaderId(tableHeader.getId());
        return createRequest(exportRequest.query().query(), dataCount, tableHeader, headerListByTableHeaderId);
    }

    public void updateStatus(Long id, RequestStatus status) {
        Request request = findById(id);
        request.setRequestStatus(status);
        requestRepository.save(request);
    }


    private Request createRequest(String query, Long dataCount, TableHeader tableHeader, List<ColumnHeader> columnHeaders) {
        Request request = new Request();
        request.setTableHeader(tableHeader);
        request.setDataCount(dataCount.intValue());
        request.setFilters(query);
        request.setRequestStatus(RequestStatus.IN_PROGRESS);
        request.addColumnHeader(columnHeaders);
        return save(request);
    }
}
