package com.impacus.maketplace.dto.shoppingBasket.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.AppProductDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ShoppingBasketDTO {
    private Long shoppingBasketId;
    private Long quantity;
    private LocalDateTime modifyAt;
    private Long groupId; // 묶음 배송 그룹 아이디
    private int deliveryFee; // 배송비
    @JsonProperty("isFreeShipping")
    private boolean isFreeShipping; // 무료 배송 여부
    private List<ProductShoppingBasketDTO> products;

    @QueryProjection
    public ShoppingBasketDTO(
            Long shoppingBasketId,
            Long quantity,
            LocalDateTime modifyAt,
            AppProductDTO product,
            ProductOptionDTO productOption,
            DeliveryRefundType deliveryFeeType,
            Integer sellerDeliveryFee
    ) {
        this.shoppingBasketId = shoppingBasketId;
        this.quantity = quantity;
        this.modifyAt = modifyAt;
        this.product = product;
        this.productOption = productOption;
    }
}
