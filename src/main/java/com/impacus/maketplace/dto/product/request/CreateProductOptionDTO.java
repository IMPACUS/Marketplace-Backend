package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.entity.product.ProductOption;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductOption;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductOptionDTO {
    private Long productOptionId;
    private String color;
    private String size;
    @NotNull
    private Long stock;

    public ProductOption toEntity(Long productId) {
        return ProductOption.builder()
                .productId(productId)
                .color(this.getColor())
                .size(this.getSize())
                .stock(this.getStock())
                .build();
    }

    public TemporaryProductOption toTemporaryEntity(Long temporaryProductId) {
        return TemporaryProductOption.builder()
                .temporaryProductId(temporaryProductId)
                .color(this.getColor())
                .size(this.getSize())
                .stock(this.getStock())
                .build();
    }

}
