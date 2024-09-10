package com.impacus.maketplace.dto.coupon.response;

import lombok.Data;

import java.util.List;

@Data
public class AvailableCouponsForProductDTO {
    private Long productId;
    private List<AvailableCouponsDTO> availableCouponsList;

    public AvailableCouponsForProductDTO(Long productId, List<AvailableCouponsDTO> availableCouponsList) {
        this.productId = productId;
        this.availableCouponsList = availableCouponsList;
    }
}
