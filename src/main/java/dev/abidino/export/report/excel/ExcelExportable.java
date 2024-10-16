package dev.abidino.export.report.excel;

public interface ExcelExportable {
    String[] excelHeaders();
    Object[] excelData();
}