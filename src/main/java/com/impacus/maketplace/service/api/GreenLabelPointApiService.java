package com.impacus.maketplace.service.api;

import java.util.List;

/**
 * 그린 라벨 포인트 지급 API
 */
public interface GreenLabelPointApiService {
    /**
     * 상품 구매 포인트를 지급하는 함수
     *
     * @param userId     포인트 지급 대상 사용자 ID
     * @param productIds 포인트 지급 상품
     * @param paymentId  PaymentEvent.paymentId
     * @return
     */
    boolean payGreenLabelPointForProductPurchase(
            Long userId,
            List<Long> productIds,
            String paymentId
    );
}
