package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.entity.product.Product;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductDTO(Long id, String name, String productNumber, int price, DeliveryType deliveryType,
                         LocalDateTime createAt) {
    public ProductDTO(Product product) {
        this(product.getId(), product.getName(), product.getProductNumber(), product.getAppSalesPrice(), product.getDeliveryType(), product.getCreateAt());
    }

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(product);
    }
}
