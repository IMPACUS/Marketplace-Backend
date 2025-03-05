package com.impacus.maketplace.dto.product.dto;

import com.impacus.maketplace.common.enumType.product.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductTypeDTO {
    private Long productId;
    private ProductType type;
}
