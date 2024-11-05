package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebProductSpecificationDTO {
    private String description;
    private int weight;
    private ProductStatus productStatus;
    private ProductType type;
}
