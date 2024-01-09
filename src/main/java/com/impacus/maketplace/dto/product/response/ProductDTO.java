package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.entity.product.Product;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDTO(@JsonProperty(value = "id", required = true) Long id,
                         @JsonProperty(value = "name", required = true) String name,
                         @JsonProperty(value = "price", required = true) int price,
                         @JsonProperty(value = "productNumber", required = true) String productNumber,
                         @JsonProperty(value = "deliveryType", required = true) DeliveryType deliveryType,
                         @JsonProperty(value = "createAt", required = true) LocalDateTime createAt,
                         String option,
                         ProductStatus productStatus,
                         Long stock) {
    public ProductDTO(Product product) {
        this(product.getId(),
                product.getName(),
                product.getAppSalesPrice(),
                product.getProductNumber(),
                product.getDeliveryType(),
                product.getCreateAt(),
                null,
                null,
                null);
    }

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(product);
    }

    public static ProductDTO toDTO(Product product, String option, Long stock) {

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getAppSalesPrice(),
                product.getProductNumber(),
                product.getDeliveryType(),
                product.getCreateAt(),
                option,
                product.getProductStatus(),
                stock
        );
    }
}
