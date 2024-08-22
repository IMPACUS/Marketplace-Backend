package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductQuantityDTO {
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
}
