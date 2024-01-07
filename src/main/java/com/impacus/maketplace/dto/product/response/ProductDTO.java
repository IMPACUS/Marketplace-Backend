package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.entity.product.Product;
import lombok.Builder;

@Builder
public record ProductDTO(Long id, String productNumber) {
    public ProductDTO(Product product) {
        this(product.getId(), product.getProductNumber());
    }

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(product);
    }
}
