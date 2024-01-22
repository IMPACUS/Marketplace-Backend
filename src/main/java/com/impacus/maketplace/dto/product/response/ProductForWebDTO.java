package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductForWebDTO {
    private Long id;
    private String name;
    private int price;
    private String productNumber;
    private DeliveryType deliveryType;
    private ProductStatus productStatus;
    private Long stock;
    private LocalDateTime createAt;
    private List<ProductOptionDTO> options;

    @QueryProjection
    public ProductForWebDTO(
            Long id,
            String name,
            int price,
            String productNumber,
            DeliveryType deliveryType,
            ProductStatus productStatus,
            Long stock,
            LocalDateTime createAt,
            List<ProductOptionDTO> productOptionList
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productNumber = productNumber;
        this.deliveryType = deliveryType;
        this.productStatus = productStatus;
        this.stock = stock;
        this.createAt = createAt;
        this.options = productOptionList;
    }
}
