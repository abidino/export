package dev.abidino.export.report.csv;

public interface CSVExportable {
    String[] csvHeaders();
    String[] csvData();
}