package com.impacus.maketplace.dto.payment.request;

import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckoutCartDTO {
    private List<PaymentProductInfoDTO> paymentProductInfos;   // 결제 상품 정보
    private AddressInfoDTO addressInfoDTO;  // 결제시 입력한 주소지
    private List<Long> appliedCommonUserCouponIds;  // 전체 주문에 적용된 사용자 쿠폰 리스트
    private Long pointAmount;   // 사용한 포인트 금액
    private PaymentMethod method;   // 결제 방식
    private Boolean usedRegisteredCard = false; // 등록된 카드 사용 유무
    private Long registeredCardId;  // 사용한 카드의 id
}
