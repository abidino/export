package dev.abidino.export.export.api;

import java.util.List;

public record ExportRequest(String query, ExportType exportType, List<Filter> filters) {
}
