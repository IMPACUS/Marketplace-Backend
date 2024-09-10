package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.dto.shoppingBasket.response.ProductShoppingBasketDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ShoppingBasketCustomRepository {
    List<ProductShoppingBasketDTO> findAllShoppingBasketByUserId(Long userId);
}
