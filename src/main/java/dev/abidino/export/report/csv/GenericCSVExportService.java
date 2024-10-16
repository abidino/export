package dev.abidino.export.report.csv;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class GenericCSVExportService {

    public <T extends CSVExportable> ByteArrayOutputStream createCSVFile(List<T> records, ByteArrayOutputStream outputStream, CSVPrinter csvPrinter) {
        if (records == null || records.isEmpty()) {
            return outputStream;
        }

        try {
            String[] headers = records.get(0).csvHeaders();
            csvPrinter.printRecord((Object[]) headers);

            for (T myData : records) {
                csvPrinter.printRecord((Object[]) myData.csvData());
            }

            csvPrinter.flush();
            return outputStream;
        } catch (Exception e) {
            throw new RuntimeException("CSV dosyası oluşturulurken hata: " + e.getMessage());
        }
    }
}