package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.utils.CalculatorUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductForAppDTO {
    private Long productId;
    private String name;
    private List<AttachFileDTO> productImageList;
    private String brandName;
    private int appSalePrice; // 판매가
    private DeliveryType deliveryType;
    private int discountPrice; // 할인가
    private double discountRate; // 할인률

    public static ProductForAppDTO toDTO(
            Long productId,
            String name,
            String brandName,
            int appSalePrice,
            DeliveryType deliveryType,
            int discountPrice
    ) {
        return ProductForAppDTO.builder()
                .productId(productId)
                .name(name)
                .brandName(brandName)
                .appSalePrice(appSalePrice)
                .deliveryType(deliveryType)
                .discountPrice(discountPrice)
                .discountRate(CalculatorUtils.calculateDiscountRate(appSalePrice, discountPrice))
                .build();
    }

    public void setProductImageList(List<AttachFileDTO> productImageList) {
        this.productImageList = productImageList;
    }
}
