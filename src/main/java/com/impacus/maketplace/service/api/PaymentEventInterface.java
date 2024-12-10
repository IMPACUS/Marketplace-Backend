package com.impacus.maketplace.service.api;

/**
 * 주문 관련 인터페이스
 */
public interface PaymentEventInterface {

    /**
     * 주문 식별자(String)으로 PK 조회
     *
     * @param orderId 주문 식별자 (주문번호)
     * @return 엔티티 PK
     */
    Long findIdByPaymentId(String orderId);

}
