package com.impacus.maketplace.dto.shoppingBasket.response;

import com.impacus.maketplace.entity.product.ShoppingBasket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonShoppingBasketDTO {
    private Long id;
    private Long productOptionId;
    private Long quantity;

    public static CommonShoppingBasketDTO toDTO(ShoppingBasket shoppingBasket) {
        return CommonShoppingBasketDTO.builder()
                .id(shoppingBasket.getId())
                .productOptionId(shoppingBasket.getProductOptionId())
                .quantity(shoppingBasket.getQuantity())
                .build();
    }
}
