package dev.abidino.export.export.service;

import dev.abidino.export.export.entities.ColumnHeader;
import dev.abidino.export.export.repo.ColumnHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ColumnHeaderService {

    private final ColumnHeaderRepository columnHeaderRepository;

    public List<String> getHeaderListByTableHeaderId(Long tableHeaderId) {
        List<ColumnHeader> columnHeaderList = columnHeaderRepository.findAllByTableHeader_IdOrderByOrderNo(tableHeaderId);
        return columnHeaderList.stream()
                .map(ColumnHeader::getHeader)
                .toList();
    }

    public List<ColumnHeader> getColumnHeaderListByTableHeaderId(Long tableHeaderId) {
        return columnHeaderRepository.findAllByTableHeader_IdOrderByOrderNo(tableHeaderId);
    }
}
