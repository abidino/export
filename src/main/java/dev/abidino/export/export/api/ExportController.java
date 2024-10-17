package dev.abidino.export.export.api;

import dev.abidino.export.export.ExportApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExportController {
    private final ExportApplicationService exportApplicationService;

    @PostMapping("/export")
    public ExportResponse export(@RequestBody ExportRequest exportRequest) {
        return exportApplicationService.export(exportRequest);
    }
}
