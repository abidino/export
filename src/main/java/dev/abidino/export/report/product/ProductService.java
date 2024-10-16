package dev.abidino.export.report.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductExportService productExportService;
    private final ProductRepository productRepository;

    public String export() {
        long productCount = productRepository.getProductCount();
        if (productCount < 100000) {
            return productExportService.exportExcel();
        }
        return productExportService.exportCsv();
    }
}
