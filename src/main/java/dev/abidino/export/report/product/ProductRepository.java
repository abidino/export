package dev.abidino.export.report.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "select count(p) from ProductEntity p")
    long getProductCount();
}
