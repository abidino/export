package dev.abidino.export.export.api;

import java.util.List;

public record ExportRequest(String query, List<Filter> filters, TableHeaderSubType tableHeaderSubType,
                            TableHeaderType tableHeaderType) {
}
