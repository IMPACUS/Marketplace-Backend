package com.impacus.maketplace.repository.payment.checkout;

import com.impacus.maketplace.repository.payment.checkout.dto.BuyerInfoDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductInfoDTO;

import java.util.List;

public interface CheckoutCustomRepository {
    CheckoutProductWithDetailsDTO findCheckoutProductWithDetails(Long productId, Long productOptionId);
    List<CheckoutProductWithDetailsByCartDTO> findCheckoutProductWithDetailsByCart(List<Long> shoppingBasketIdList);

    BuyerInfoDTO getBuyerInfo(Long userId);

    CheckoutProductInfoDTO getPaymentProductInfo(Long productId, Long productOptionId, Long sellerId, Boolean usedRegisteredCard, Long registeredCardId);
}
