package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebProductSpecificationDTO {
    private String description;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int weight = 0;

    private ProductStatus productStatus;
    private ProductType type;

    @JsonIgnore
    public boolean isNull() {
        return description == null && weight ==0 && productStatus == null && type == null;
    }
}
