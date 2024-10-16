package dev.abidino.export.export.service;

import dev.abidino.export.export.api.ExportType;
import dev.abidino.export.export.entities.TableHeader;
import dev.abidino.export.export.repo.TableHeaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableHeaderService {
    private final TableHeaderRepository tableHeaderRepository;

    public TableHeader getTableHeaderByExportType(ExportType exportType) {
        Optional<TableHeader> optTableHeader = tableHeaderRepository.findByType(exportType);

        if (optTableHeader.isEmpty()) {
            log.error("No product found");
            throw new RuntimeException("No product found");
        }

        return optTableHeader.get();
    }

}
