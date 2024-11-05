package com.impacus.maketplace.dto.temporaryProduct.response;

import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TemporaryProductSpecificationDTO {
    private String description;
    private int weight;
    private ProductStatus productStatus;
    private ProductType type;
}
