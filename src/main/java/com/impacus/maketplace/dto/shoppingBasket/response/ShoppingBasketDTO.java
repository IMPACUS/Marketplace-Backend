package com.impacus.maketplace.dto.shoppingBasket.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.product.DeliveryFeeRule;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.AppProductDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
public class ShoppingBasketDTO {
    private Long groupId; // 묶음 배송 그룹 아이디
    private DeliveryFeeRule deliveryFeeRule;
    private List<ProductShoppingBasketDTO> products;

    public ShoppingBasketDTO(
            Long groupId,
            DeliveryFeeRule deliveryFeeRule
    ) {
        this.groupId = groupId;
        this.deliveryFeeRule = deliveryFeeRule;
        this.products = new ArrayList<>();

    }

    public ShoppingBasketDTO (ProductShoppingBasketDTO product) {
        this.products = List.of(product);
    }
}
