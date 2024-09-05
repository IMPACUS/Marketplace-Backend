package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode()
public class WebProductOptionDTO {
    private Long productOptionId;
    private String color;
    private String size;
    private String name;

    @QueryProjection
    public WebProductOptionDTO(Long productOptionId, String color, String size) {
        this.productOptionId = productOptionId;
        this.color = color;
        this.size = size;
        this.name = String.format("%s/%s", color, size);
    }
}
