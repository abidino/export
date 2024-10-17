package dev.abidino.export.kafka;

import dev.abidino.export.export.api.Filter;
import dev.abidino.export.export.api.TableHeaderSubType;
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
    private TableHeaderSubType tableHeaderSubType;
    private Long limit;
    private List<Filter> filters;
}
