package dev.abidino.export.report.product;

import dev.abidino.export.report.csv.CSVExportable;
import dev.abidino.export.report.excel.ExcelExportable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product implements ExcelExportable, CSVExportable {
    private final Long id;
    private final String productName;
    private final double price;

    @Override
    public String[] excelHeaders() {
        return new String[]{"Id", "Product Name", "Price"};
    }

    @Override
    public Object[] excelData() {
        return new Object[]{id, productName, price};
    }

    @Override
    public String[] csvHeaders() {
        return new String[]{"Id","Product Name", "Price"};
    }

    @Override
    public String[] csvData() {
        return new String[]{String.valueOf(id), productName, String.valueOf(price)};
    }
}