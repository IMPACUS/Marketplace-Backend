package com.impacus.maketplace.service.api;

/**
 * 결제 관련 Api 요청 서비스
 */
public interface PaymentApiService {

    boolean isPaymentOrderConfirmed(Long paymentOrderId);
}
