package dev.abidino.export.report.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerExportService customerExportService;
    private final CustomerRepository customerRepository;

    public String export(){
        long customerCount = customerRepository.getCustomerCount();
        if (customerCount < 100000) {
            return customerExportService.exportExcel();
        } else if (customerCount < 1000000){
            return customerExportService.exportCsv();
        }

        return customerExportService.exportExcel();
    }
}
