package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.EventType;
import com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.service.coupon.utils.CouponIssuanceValidator;
import com.impacus.maketplace.service.coupon.utils.CouponPeriodConditionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 이벤트 쿠폰 처리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventCouponService {

    private final CouponRepository couponRepository;
    private final CouponIssuanceValidator couponValidator;
    private final CouponPeriodConditionChecker eventCouponPeriodConditionChecker;

    /**
     * 회원가입 시 발행되는 쿠폰 처리
     */
    public void issueSignupCoupon(Long userId) {
    }

    /**
     * 주문별 이벤트 처리
     */
    public void issuePaymentEventCoupon(Long userId, Long paymentEventId) {

        // 1. 전체 쿠폰 조회
        List<Coupon> coupons = couponRepository.findAllActiveCoupons();

        // 2. 1차 검증 & 2차 검증
        List<CouponConditionCheckResultDTO> availablePaymentEventCoupons = coupons.stream()
                .filter(coupon -> couponValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_ORDER))
                .filter(coupon -> couponValidator.validatePaymentEventCoupon(coupon, paymentEventId))
                .map(coupon -> eventCouponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEventId))
                .toList();

    }

    /**
     * 주문 상품별 이벤트 처리
     * @param userId
     */
    public void issuePaymentOrderCoupon(Long userId, Long paymentOrderId) {
        List<Coupon> coupons = couponRepository.findAllActiveCoupons();

        List<Coupon> filterdCoupons = coupons.stream()
                .filter(coupon -> couponValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_PRODUCT))
                .toList();

        List<Coupon> availablePaymentOrderCoupons = filterdCoupons.stream()
                .filter(coupon -> couponValidator.validatePaymentOrderCoupon(coupon, paymentOrderId))
                .toList();

    }

    /**
     * SNS 태그 이벤트 처리
     * @param userId
     */
    public void issueSocialMediaCoupon(Long userId) {

    }
}
