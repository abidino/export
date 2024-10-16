package dev.abidino.export.report.customer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerMapper {

    public static Customer createCustomerFromCustomerEntity(CustomerEntity customerEntity) {
        return new Customer(customerEntity.getId(), customerEntity.getName(), customerEntity.getEmail());
    }

    public static Page<Customer> createCustomerFromCustomerEntity(Page<CustomerEntity> pageCustomerEntity) {
        List<CustomerEntity> content = pageCustomerEntity.getContent();
        List<Customer> customerList = content.stream()
                .map(CustomerMapper::createCustomerFromCustomerEntity)
                .toList();

        return new PageImpl<>(customerList, pageCustomerEntity.getPageable(), pageCustomerEntity.getTotalElements());
    }
}
