package com.impacus.maketplace.dto.coupon.response;

import lombok.Data;

import java.util.List;

@Data
public class AvailableCouponsForProductDTO {
    private Long productId;
    private List<AvailableCouponsDTO> availableCouponsDTOList;

    public AvailableCouponsForProductDTO(Long productId, List<AvailableCouponsDTO> availableCouponsDTOList) {
        this.productId = productId;
        this.availableCouponsDTOList = availableCouponsDTOList;
    }
}
