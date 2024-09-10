package com.impacus.maketplace.dto.shoppingBasket.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.AppProductDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductShoppingBasketDTO {
    private Long productId;
    private String name;
    private List<AttachFileDTO> productImageList;
    private String brandName;
    private Integer deliveryFee;
    private ProductOptionDTO productOption;
    private DeliveryType deliveryType;
    private ProductType type;
    private int discountPrice; // 할인가

    @JsonIgnoreProperties
    private Long groupId; // 묶음 배송 그룹 아이디

    @QueryProjection
    public ProductShoppingBasketDTO(
             Long productId,
             String name,
             List<AttachFileDTO> productImageList,
             String brandName,
             Integer deliveryFee,
             ProductOptionDTO productOption,
             DeliveryType deliveryType,
             ProductType type,
             int discountPrice,
             Long groupId,
             DeliveryRefundType deliveryFeeType,
             Integer sellerDeliveryFee
    ) {
        this.productId = productId;
        this.name = name;
        this.productImageList = productImageList;
        this.brandName = brandName;
        this.deliveryFee = deliveryFee;
        this.productOption = productOption;
        this.deliveryType = deliveryType;
        this.type = type;
        this.discountPrice = discountPrice;
        this.groupId = groupId;


    }
}
