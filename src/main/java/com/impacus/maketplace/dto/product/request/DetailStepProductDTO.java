package com.impacus.maketplace.dto.product.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DetailStepProductDTO {
    @NotNull
    private CreateProductDetailInfoDTO productDetail;

    @NotNull
    private CreateClaimInfoDTO claim;
}
