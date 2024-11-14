package com.impacus.maketplace.repository.payment.checkout;

import com.impacus.maketplace.dto.payment.model.PaymentProductInfoIdDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.BuyerInfoDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductInfoDTO;

import java.util.List;

public interface CheckoutCustomRepository {
    CheckoutProductWithDetailsDTO findCheckoutProductWithDetails(Long productId, Long productOptionId);
    List<CheckoutProductWithDetailsByCartDTO> findCheckoutProductWithDetailsByCart(Long userId, List<Long> shoppingBasketIdList);

    BuyerInfoDTO getBuyerInfo(Long userId);

    CheckoutProductInfoDTO getPaymentProductInfo(Long productId, Long productOptionId, Long sellerId, Boolean usedRegisteredCard, Long registeredCardId);

    List<CheckoutProductInfoDTO> getPaymentProductInfos(List<PaymentProductInfoIdDTO> paymentProductInfoIds, Boolean usedRegisteredCard, Long registeredCardId);
}
