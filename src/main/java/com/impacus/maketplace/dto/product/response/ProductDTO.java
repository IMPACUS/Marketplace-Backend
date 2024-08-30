package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ProductDTO {

    @JsonProperty(value = "id", required = true)
    private Long id;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "price", required = true)
    private int price;

    @JsonProperty(value = "productNumber", required = true)
    private String productNumber;

    @JsonProperty(value = "deliveryType", required = true)
    private DeliveryType deliveryType;

    @JsonProperty(value = "type", required = true)
    private ProductType type;

    @JsonProperty(value = "createAt", required = true)
    private LocalDateTime createAt;

    public ProductDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getAppSalesPrice(),
                product.getProductNumber(),
                product.getDeliveryType(),
                product.getType(),
                product.getCreateAt()
        );
    }

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(product);
    }
}

