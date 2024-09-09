package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.common.utils.CalculatorUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.entity.product.ProductOption;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AppProductDetailDTO {
    private Long id;
    private Long sellerId;
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
    private Integer deliveryFee; // 배송비
    private String brandName;
    private String description;
    @JsonProperty("isOutOfStock")
    private boolean isOutOfStock;
    private ProductDeliveryTimeDTO deliveryTime;

    private float averageRating; // 평균 평점
    private Long reviewCnt;
    private Long wishlistCnt;
    private int maxEarnablePoints; // 최대 적립 포인트


    @QueryProjection
    public AppProductDetailDTO(Long id,
                               String name,
                               int appSalePrice,
                               int discountPrice,
                               ProductType type,
                               List<ProductOption> options,
                               Long wishlistId,
                               int deliveryFee,
                               String brandName,
                               String description,
                               ProductDeliveryTimeDTO deliveryTime,
                               List<String> productImages,
                               Long sellerId,
                               DeliveryRefundType deliveryFeeType,
                               Integer sellerDeliveryFee
    ) {
        this.id = id;
        this.name = name;
        this.appSalePrice = appSalePrice;
        this.discountPrice = discountPrice;
        this.type = type;
        this.discountRate = CalculatorUtils.calculateDiscountRate(appSalePrice, discountPrice);
        this.isExistedWishlist = wishlistId != null;
        this.brandName = brandName;
        this.description = description;
        this.deliveryTime = deliveryTime;
        setOptionData(options);
        this.productImageList = productImages.stream().map(x -> new AttachFileDTO(x)).toList();
        this.sellerId = sellerId;

        if (deliveryFeeType == DeliveryRefundType.FREE_SHIPPING) {
            this.isFreeShipping = true;
            this.deliveryFee = 0;
        } else if (deliveryFeeType == DeliveryRefundType.STORE_DEFAULT) {
            this.isFreeShipping = sellerDeliveryFee == 0;
            this.deliveryFee = sellerDeliveryFee;
        } else {
            this.isFreeShipping = deliveryFee == 0;
            this.deliveryFee = deliveryFee;
        }

        // TODO 관련 기능 개발 완료 되면 연결
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
