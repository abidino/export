package dev.abidino.export.report.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static Product createProductFromProductEntity(ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getProductName(), productEntity.getPrice());
    }

    public static Page<Product> createProductFromProductEntity(Page<ProductEntity> pageProductEntity) {
        List<ProductEntity> content = pageProductEntity.getContent();
        List<Product> productList = content.stream()
                .map(ProductMapper::createProductFromProductEntity)
                .toList();

        return new PageImpl<>(productList, pageProductEntity.getPageable(), pageProductEntity.getTotalElements());
    }
}
