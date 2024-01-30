package com.impacus.maketplace.dto.wishlist.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.utils.CalculatorUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class WishlistDetailDTO {
    private Long wishlistId;
    private Long productId;
    private String name;
    private List<AttachFileDTO> productImageList;
    private String brandName;
    private int appSalePrice; // 판매가
    private DeliveryType deliveryType;
    private int discountPrice; // 할인가
    private float discountRate; // 할인률


    @QueryProjection
    public WishlistDetailDTO(
            Long wishlistId,
            Long productId,
            String name,
            String brandName,
            int appSalePrice,
            DeliveryType deliveryType,
            int discountPrice
    ) {
        this.wishlistId = wishlistId;
        this.productId = productId;
        this.name = name;
        this.brandName = brandName;
        this.appSalePrice = appSalePrice;
        this.deliveryType = deliveryType;
        this.discountPrice = discountPrice;
        this.discountRate = CalculatorUtils.calculateDiscountRate(appSalePrice, discountPrice);
    }

    public void setProductImageList(List<AttachFileDTO> productImageList) {
        this.productImageList = productImageList;
    }
}
