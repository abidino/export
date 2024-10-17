package dev.abidino.export.export.api;

public record ExportRequest(QueryRequest query, TableHeaderSubType tableHeaderSubType,
                            TableHeaderType tableHeaderType) {
}
