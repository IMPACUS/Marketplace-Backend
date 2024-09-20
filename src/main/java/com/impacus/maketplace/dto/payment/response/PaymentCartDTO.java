package com.impacus.maketplace.dto.payment.response;

import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentCartDTO {
    private String storeId;     // 고객 정보
    private String paymentKey;  // 결제 ID
    private Long cartId;    // 카트 번호
    private String orderName;   // 주문명
    private Long totalDiscountedAmount;   // 금액
    private String currency = "KRW";    // 결제 통화
    private String channelKey;      // 채널 키 - 스마트로, 카카오 페이
    private PaymentMethod paymentMethod;       // 결제 수단
    private CheckoutCustomerDTO customer;   // 고객 정보
}
