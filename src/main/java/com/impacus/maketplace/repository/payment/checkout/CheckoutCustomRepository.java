package com.impacus.maketplace.repository.payment.checkout;

import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsDTO;

import java.util.List;

public interface CheckoutCustomRepository {
    CheckoutProductWithDetailsDTO findCheckoutProductWithDetails(Long productId, Long productOptionId);
    List<CheckoutProductWithDetailsByCartDTO> findCheckoutProductWithDetailsByCart(List<Long> shoppingBasketIdList);
}
