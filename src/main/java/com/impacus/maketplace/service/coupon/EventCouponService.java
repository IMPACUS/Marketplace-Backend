package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.EventType;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.service.coupon.utils.CouponIssuanceValidator;
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

    /**
     * 회원가입 시 발행되는 쿠폰 처리
     */
    public void issueSignupCoupon(Long userId) {
    }

    /**
     * 주문별 이벤트 처리
     */
    public void issuePaymentEventCoupon(Long userId, Long paymentEventId) {
        List<Coupon> coupons = couponRepository.findAll();

        List<Coupon> filterdCoupons = coupons.stream()
                .filter(coupon -> couponValidator.validateEventCoupon(userId, coupon, EventType.ORDER))
                .toList();
    }

    /**
     * 주문 상품별 이벤트 처리
     * @param userId
     */
    public void issuePaymentOrderCouopn(Long userId, Long paymentOrderId) {

    }

    /**
     * SNS 태그 이벤트 처리
     * @param userId
     */
    public void issueSocialMediaCoupon(Long userId) {

    }
}
