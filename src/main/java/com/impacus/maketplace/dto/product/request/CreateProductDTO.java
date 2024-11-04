package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO extends ProductDTO {
    private Long sellerId;

    @NotNull
    private boolean doesUseTemporaryProduct;

    public Product toEntity(Long sellerId) {
        return new Product(sellerId, this);
    }

    public TemporaryProduct toTemporaryEntity() {
        return new TemporaryProduct(this);
    }

}
