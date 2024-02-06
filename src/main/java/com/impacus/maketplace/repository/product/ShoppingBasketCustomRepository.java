package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDetailDTO;

import java.util.List;

public interface ShoppingBasketCustomRepository {
    List<ShoppingBasketDetailDTO> findAllShoppingBasketByUserId(Long userId);
}
