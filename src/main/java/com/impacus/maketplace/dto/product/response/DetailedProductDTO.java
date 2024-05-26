package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.common.utils.CalculatorUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.entity.product.ProductOption;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DetailedProductDTO {
    private Long id;
    private String name;
    private List<AttachFileDTO> productImageList;

    private int appSalePrice; // 판매가
    private int discountPrice; // 할인가
    private double discountRate; // 할인률
    private ProductType type;
    private List<ProductOptionDetailDTO> options;
    @JsonProperty("isExistedWishlist")
    private boolean isExistedWishlist; //  찜 여부
    @JsonProperty("isFreeShipping")
    private boolean isFreeShipping; // 무료 배송 여부
    private int deliveryFee; // 배송비
    private String brandName;
    private String description;
    @JsonProperty("isOutOfStock")
    private boolean isOutOfStock;
    private ProductDeliveryTimeDTO deliveryTime;

    private float averageRating; // 평균 평점
    private Long reviewCnt;
    private Long wishlistCnt;
    private int maxEarnablePoints; // 최대 적림 포인트


    @QueryProjection
    public DetailedProductDTO(Long id,
                              String name,
                              int appSalePrice,
                              int discountPrice,
                              ProductType type,
                              List<ProductOption> options,
                              Long wishlistId,
                              int deliveryFee,
                              String brandName,
                              String description,
                              ProductDeliveryTimeDTO deliveryTime
    ) {
        this.id = id;
        this.name = name;
        this.appSalePrice = appSalePrice;
        this.discountPrice = discountPrice;
        this.type = type;
        this.discountRate = CalculatorUtils.calculateDiscountRate(appSalePrice, discountPrice);
        this.isExistedWishlist = wishlistId != null;
        this.isFreeShipping = deliveryFee == 0;
        this.deliveryFee = deliveryFee;
        this.brandName = brandName;
        this.description = description;
        this.deliveryTime = deliveryTime;
        setOptionData(options);

        this.averageRating = 5.0f;
        this.reviewCnt = 123L;
        this.maxEarnablePoints = 100;
    }

    public void setOptionData(List<ProductOption> options) {
        this.isOutOfStock = true;
        List<ProductOptionDetailDTO> productOptionDetailDTOs = new ArrayList<>();

        for (ProductOption option : options) {
            if (option.getStock() > 0) {
                this.isOutOfStock = false;
            }

            productOptionDetailDTOs.add(new ProductOptionDetailDTO(
                    option.getId(),
                    option.getColor(),
                    option.getSize(),
                    option.getStock()
            ));
        }

        this.options = productOptionDetailDTOs;
    }

    public void setWishlistCnt(Long wishlistCnt) {
        this.wishlistCnt = wishlistCnt;
    }
}
