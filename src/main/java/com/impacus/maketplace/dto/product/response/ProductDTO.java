package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.DeliveryType;
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
                         @JsonProperty(value = "createAt", required = true) LocalDateTime createAt) {
    public ProductDTO(Product product) {
        this(product.getId(),
                product.getName(),
                product.getAppSalesPrice(),
                product.getProductNumber(),
                product.getDeliveryType(),
                product.getCreateAt());
    }

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(product);
    }
}
