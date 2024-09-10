package com.impacus.maketplace.dto.shoppingBasket.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.product.*;
import com.impacus.maketplace.common.utils.ProductUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductShoppingBasketDTO {
    private Long shoppingBasketId;
    private Long productId;
    private String name;
    private List<AttachFileDTO> productImageList;
    private String brandName;
    private Integer deliveryFee;
    private ProductOptionDTO productOption;
    private DeliveryType deliveryType;
    private ProductType type;
    private int discountPrice; // 할인가
    private Long quantity;
    private LocalDateTime modifyAt;
    @JsonProperty("isFreeShipping")
    private boolean isFreeShipping; // 무료 배송 여부

    @JsonIgnoreProperties
    private Long groupId; // 묶음 배송 그룹 아이디

    @JsonIgnoreProperties
    private DeliveryFeeRule deliveryFeeRule;

    @QueryProjection
    public ProductShoppingBasketDTO(
             Long shoppingBasketId,
             Long productId,
             String name,
             List<String> productImages,
             String brandName,
             Integer deliveryFee,
             ProductOptionDTO productOption,
             DeliveryType deliveryType,
             ProductType type,
             int discountPrice,
             Long groupId,
             DeliveryRefundType deliveryFeeType,
             Integer sellerDeliveryFee,
             Long quantity,
             LocalDateTime modifyAt,
             DeliveryFeeRule deliveryFeeRule
    ) {
        this.shoppingBasketId = shoppingBasketId;
        this.productId = productId;
        this.name = name;
        this.productImageList = productImages.stream().map(AttachFileDTO::new).toList();
        this.brandName = brandName;
        this.productOption = productOption;
        this.deliveryType = deliveryType;
        this.type = type;
        this.discountPrice = discountPrice;
        this.groupId = groupId;
        this.quantity = quantity;
        this.modifyAt = modifyAt;
        this.deliveryFee = ProductUtils.getProductDeliveryFee(deliveryFee, deliveryFeeType, sellerDeliveryFee);
        this.isFreeShipping = ProductUtils.checkIsFreeShipping(deliveryFee, deliveryFeeType, sellerDeliveryFee);
        this.deliveryFeeRule = deliveryFeeRule;
    }
}
