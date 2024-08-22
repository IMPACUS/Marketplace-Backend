package com.impacus.maketplace.dto.coupon.response;

import lombok.Data;

import java.util.List;

@Data
public class AvailableCouponsForCheckoutDTO {
    private List<AvailableCouponsForProductDTO> availableCouponsForProductDTOList;
    private List<AvailableCouponsDTO> availableCouponsForOrderDTOList;

    public AvailableCouponsForCheckoutDTO(List<AvailableCouponsForProductDTO> availableCouponsForProductDTOList, List<AvailableCouponsDTO> availableCouponsForOrderDTOList) {
        this.availableCouponsForProductDTOList = availableCouponsForProductDTOList;
        this.availableCouponsForOrderDTOList = availableCouponsForOrderDTOList;
    }
}
