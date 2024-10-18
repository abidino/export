package dev.abidino.export.export.api;

public record ExportResponse(Long mediaId, String base64,
                             boolean isSuccess, String message, Long requestId,
                             ExportStrategyType strategy) {
}
