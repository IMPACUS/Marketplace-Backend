package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DetailStepProductDTO {
    @NotNull
    private CreateProductDetailInfoDTO productDetail;

    @NotNull
    private CreateClaimInfoDTO claim;

    public TemporaryProduct toEntity() {
        return new TemporaryProduct();
    }
}
