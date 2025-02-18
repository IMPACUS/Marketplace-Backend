package com.impacus.maketplace.dto.coupon.model;

import com.impacus.maketplace.common.enumType.payment.PaymentType;
import com.impacus.maketplace.entity.coupon.Coupon;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CouponConditionCheckResultDTO {
    private final boolean valid;                 // 쿠폰 발행 유효성 체크 결과
    private final boolean conditionSet;          // 쿠폰 기간 설정 조건 세팅 유무
    private final Coupon coupon;                 // 발급 대상 쿠폰
    private final List<Long> triggerIds;        // 발급을 위해 트리거가 된 결제 이벤트 ID 리스트
    private final Type type;                    // 타입


    public static CouponConditionCheckResultDTO fail() {
        return new CouponConditionCheckResultDTO(false, true,null, Collections.emptyList(), Type.NONE);
    }

    public static CouponConditionCheckResultDTO pass(Coupon coupon) {
        return new CouponConditionCheckResultDTO(true, false, coupon, Collections.emptyList(), Type.NONE);
    }

    public static CouponConditionCheckResultDTO getSuccessPaymentEventDTO(Coupon coupon, List<Long> paymentEventIds) {
        return new CouponConditionCheckResultDTO(true, true, coupon, paymentEventIds, Type.ORDER);
    }

    public static CouponConditionCheckResultDTO getSuccessPaymentOrderDTO(Coupon coupon, Long paymentOrderId) {
        return new CouponConditionCheckResultDTO(true, true, coupon, List.of(paymentOrderId), Type.PRODUCT);
    }

    enum Type {
        ORDER,
        PRODUCT,
        NONE
    }
}
