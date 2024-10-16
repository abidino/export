package dev.abidino.export.report.product;

import dev.abidino.export.FileUtil;
import dev.abidino.export.report.csv.GenericCSVExportService;
import dev.abidino.export.report.excel.GenericExcelExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductExportService {
    private final GenericExcelExportService genericExcelExportService;
    private final GenericCSVExportService genericCsvExportService;
    private final ProductRepository productRepository;

    public String exportExcel() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> page;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Records");

        do {
            Page<ProductEntity> pageableProductEntities = productRepository.findAll(pageable);
            page = ProductMapper.createProductFromProductEntity(pageableProductEntities);
            genericExcelExportService.createExcelForReport(page.getContent(), sheet);
            pageable = page.nextPageable();
        } while (page.hasNext());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        FileUtil.convertByteArrayOutputStreamToFile(outputStream, FileUtil.createFileName("product", ".xlsx"));
        return FileUtil.convertToBase64(outputStream);
    }

    public String exportCsv() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> page;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        CSVPrinter csvPrinter;

        try {
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        do {
            Page<ProductEntity> pageableProductEntities = productRepository.findAll(pageable);
            page = ProductMapper.createProductFromProductEntity(pageableProductEntities);
            outputStream = genericCsvExportService.createCSVFile(page.getContent(), outputStream, csvPrinter);
            pageable = page.nextPageable();
        } while (page.hasNext());

        try {
            csvPrinter.close();
            writer.close();
            outputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        FileUtil.convertByteArrayOutputStreamToFile(outputStream, FileUtil.createFileName("product", ".csv"));
        return FileUtil.convertToBase64(outputStream);
    }
}
