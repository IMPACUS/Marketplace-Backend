package com.impacus.maketplace.dto.dev.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestValidEnumDTO {
    @ValidEnum(enumClass = ProductStatus.class)
    private ProductStatus productStatus;
}
