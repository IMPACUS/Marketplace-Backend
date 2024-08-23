package com.impacus.maketplace.dto.coupon.response;

import lombok.Data;

import java.util.List;

@Data
public class AvailableCouponsForCheckoutDTO {
    private List<AvailableCouponsForProductDTO> availableCouponsForProductList;
    private List<AvailableCouponsDTO> availableCouponsForOrderList;

    public AvailableCouponsForCheckoutDTO(List<AvailableCouponsForProductDTO> availableCouponsForProductList, List<AvailableCouponsDTO> availableCouponsForOrderList) {
        this.availableCouponsForProductList = availableCouponsForProductList;
        this.availableCouponsForOrderList = availableCouponsForOrderList;
    }
}
