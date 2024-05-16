package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOptionDetailDTO {
    private Long productOptionId;
    private String color;
    private String size;

    @JsonProperty("isOutOfStock")
    private boolean isOutOfStock;

    @QueryProjection
    public ProductOptionDetailDTO(
            Long productOptionId,
            String color,
            String size,
            Long stock
    ) {
        this.productOptionId = productOptionId;
        this.color = color;
        this.size = size;
        this.isOutOfStock = stock == 0;
    }
}