package dev.abidino.export.report.customer;

import dev.abidino.export.report.csv.CSVExportable;
import dev.abidino.export.report.excel.ExcelExportable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer implements ExcelExportable, CSVExportable {
    private final Long id;
    private final String name;
    private final String email;

    @Override
    public String[] excelHeaders() {
        return new String[]{"ID", "Name", "Email"};
    }

    @Override
    public Object[] excelData() {
        return new Object[]{id, name, email};
    }

    @Override
    public String[] csvHeaders() {
        return new String[]{"ID", "Name", "Email"};
    }

    @Override
    public String[] csvData() {
        return new String[]{String.valueOf(id), name, email};
    }
}