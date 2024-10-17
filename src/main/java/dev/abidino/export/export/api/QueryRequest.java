package dev.abidino.export.export.api;

import java.util.List;

public record QueryRequest(String query, List<Filter> filters) {
}
