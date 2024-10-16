package dev.abidino.export.kafka;

import dev.abidino.export.export.api.ExportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExportEvent {
    private Long requestId;
    private String query;
    private Long offset;
    private ExportType exportType;
    private Long limit;
}
