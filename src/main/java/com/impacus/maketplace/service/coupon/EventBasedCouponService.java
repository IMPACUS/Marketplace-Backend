package com.impacus.maketplace.service.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 이벤트 기반 쿠폰 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class EventBasedCouponService {

    /**
     * 회원가입 시 발행되는 쿠폰 처리
     */
    public void issueSignupCoupon(Long userId) {

    }

    /**
     * 주문별 이벤트 처리
     */
    public void issuePaymentEventCoupon(Long userId) {

    }

    /**
     * 주문 상품별 이벤트 처리
     * @param userId
     */
    public void issuePaymentOrderCouopn(Long userId) {

    }

    /**
     * SNS 태그 이벤트 처리
     * @param userId
     */
    public void issueSocialMediaCoupon(Long userId) {

    }
}
