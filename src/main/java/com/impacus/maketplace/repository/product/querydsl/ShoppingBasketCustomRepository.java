package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShoppingBasketCustomRepository {
    Slice<ShoppingBasketDTO> findAllShoppingBasketByUserId(Long userId, Pageable pageable);
}
