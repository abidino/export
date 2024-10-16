package dev.abidino.export.report.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query(value = "select count(c) from CustomerEntity c")
    long getCustomerCount();
}
