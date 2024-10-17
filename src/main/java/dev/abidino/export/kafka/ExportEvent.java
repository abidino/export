package dev.abidino.export.kafka;

import dev.abidino.export.export.api.ExportType;
import dev.abidino.export.export.api.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExportEvent {
    private Long requestId;
    private String query;
    private Long offset;
    private ExportType exportType;
    private Long limit;
    private List<Filter> filters;
}
