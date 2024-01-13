package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOptionDTO {
    private Long productOptionId;
    private String color;
    private String size;

    @QueryProjection
    public ProductOptionDTO(Long productOptionId, String color, String size) {
        this.productOptionId = productOptionId;
        this.color = color;
        this.size = size;
    }
}
