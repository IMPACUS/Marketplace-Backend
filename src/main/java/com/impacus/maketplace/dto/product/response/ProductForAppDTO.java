package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.common.utils.CalculatorUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
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

    @JsonProperty("isExistedWishlist")
    private boolean isExistedWishlist; //  찜 여부

    @JsonProperty("isFreeShipping")
    private boolean isFreeShipping; // 무료 배송 여부

    ProductType type;
    LocalDateTime createAt;


    @QueryProjection
    public ProductForAppDTO(
            Long productId,
            String name,
            String brandName,
            int appSalePrice,
            DeliveryType deliveryType,
            int discountPrice,
            List<AttachFileDTO> productImageList,
            Long wishlistId,
            int deliveryFee,
            ProductType type,
            LocalDateTime createAt
    ) {
        this.productId = productId;
        this.name = name;
        this.brandName = brandName;
        this.appSalePrice = appSalePrice;
        this.deliveryType = deliveryType;
        this.discountPrice = discountPrice;
        this.productImageList = productImageList;
        this.isExistedWishlist = wishlistId != null;
        this.isFreeShipping = deliveryFee == 0;
        this.discountRate = CalculatorUtils.calculateDiscountRate(appSalePrice, discountPrice);
        this.type = type;
        this.createAt = createAt;
    }
}
