package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDetailDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShoppingBasketCustomRepository {
    Slice<ShoppingBasketDetailDTO> findAllShoppingBasketByUserId(Long userId, Pageable pageable);
}
