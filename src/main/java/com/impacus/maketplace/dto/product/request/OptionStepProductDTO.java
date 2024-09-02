package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OptionStepProductDTO {

    @NotNull
    private List<CreateProductOptionDTO> productOptions;

    @NotNull
    private Integer weight;

    @ValidEnum(enumClass = ProductStatus.class)
    private ProductStatus productStatus;

    @NotBlank
    private String description;

    @ValidEnum(enumClass = ProductType.class)
    private ProductType type;

    public TemporaryProduct toEntity() {
        return new TemporaryProduct(this);
    }
}
