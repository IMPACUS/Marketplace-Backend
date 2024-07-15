package com.impacus.maketplace.dto.shoppingBasket.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeShoppingBasketQuantityDTO {
    @NotNull
    private Long shoppingBasketId;

    @NotNull
    private Long quantity;

}
