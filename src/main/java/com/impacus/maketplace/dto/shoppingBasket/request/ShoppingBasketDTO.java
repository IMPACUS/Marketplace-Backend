package com.impacus.maketplace.dto.shoppingBasket.request;

import com.impacus.maketplace.entity.product.ShoppingBasket;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingBasketDTO {
    @NotNull
    private Long productOptionId;
    @NotNull
    private Long quantity;

    public ShoppingBasket toEntity(Long userId) {
        return new ShoppingBasket(this.productOptionId, this.quantity, userId);
    }
}
