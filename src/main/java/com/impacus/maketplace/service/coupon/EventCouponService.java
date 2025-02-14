package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.CouponTriggerType;
import com.impacus.maketplace.common.enumType.coupon.EventType;
import com.impacus.maketplace.common.enumType.coupon.TriggerType;
import com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponTrigger;
import com.impacus.maketplace.entity.coupon.UserCoupon;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.CouponTriggerRepository;
import com.impacus.maketplace.service.coupon.utils.CouponIssuanceManager;
import com.impacus.maketplace.service.coupon.utils.CouponIssuanceValidator;
import com.impacus.maketplace.service.coupon.utils.EventCouponPeriodConditionChecker;
import com.impacus.maketplace.service.coupon.utils.CouponPrioritySelector;
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
    private final EventCouponPeriodConditionChecker couponPeriodConditionChecker;
    private final CouponPrioritySelector couponPrioritySelector;
    private final CouponIssuanceManager couponIssuanceManager;
    private final CouponTriggerRepository couponTriggerRepository;

    /**
     * 회원가입 시 발행되는 쿠폰 처리
     */
    public void issueSignupCoupon(Long userId) {
    }


    /**
     * 결제 주문 단위의 결제 이벤트 쿠폰 발급
     *
     * <p>
     *     <li>발급 가능한 쿠폰 중 가장 우선순위가 높은 것을 선정하여 사용자에게 발급해준다.</li>
     *     <li>발급 가능한 쿠폰 선정 과정은 1차 검증과 2차 검증 및 기간 검증으로 나뉘며, 1차 검증은 Event Coupon 공통 검증이며 2차 검증은 Payment Event Coupon 검증 과정이라고 생각하면 된다.</li>
     *     <li>발급 직전에 발급 수량 검증이 다시 이루어지며 해당 과정에서 예외가 발생 시 다음 우선순위 쿠폰을 발급한다. 만약 다음 쿠폰이 없다면 발급이 취소된다.</li>
     * </p>
     *
     * @param userId    쿠폰을 발급 받을 사용자
     * @param paymentEventId    쿠폰 발급의 트리거가 된 결제 주문 이벤트
     */
    @Transactional
    public void issuePaymentEventCoupon(Long userId, Long paymentEventId) {

        // 1. 전체 쿠폰 조회
        List<Coupon> coupons = couponRepository.findAllActiveCoupons();

        // 2. 1차 검증 & 2차 검증 & 기간 검증
        List<CouponConditionCheckResultDTO> availablePaymentEventCoupons = coupons.stream()
                .filter(coupon -> couponValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_ORDER))
                .filter(coupon -> couponValidator.validatePaymentEventCoupon(coupon, paymentEventId))
                .map(coupon -> couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEventId))
                .toList();

        // 3. 발급한 쿠폰 선택
        CouponConditionCheckResultDTO selectedCouponInfo = couponPrioritySelector.selectHighestPriorityCoupon(availablePaymentEventCoupons);

        // 4. 발급 처리
        // TODO: END_FIRST_COUNT_COUPON Exception 발생할 경우 다른 쿠폰 발행 로직 진행
        UserCoupon userCoupon = couponIssuanceManager.issueCouponToUser(userId, selectedCouponInfo.getCoupon().getId(), TriggerType.ORDER);

        // 5. 결제 주문 쿠폰 발급 이력 저장
        for (Long triggerId : selectedCouponInfo.getTriggerIds()) {
            CouponTrigger couponTrigger = CouponTrigger.builder().userCouponId(userCoupon.getId()).userId(userId).triggerType(CouponTriggerType.ORDER).triggerId(triggerId).build();
            couponTriggerRepository.save(couponTrigger);
        }
    }

    /**
     * 결제 상품 단위의 결제 이벤트 쿠폰 발급
     *
     * <p>
     *     <li>발급 가능한 쿠폰 중 가장 우선순위가 높은 것을 선정하여 사용자에게 발급해준다.</li>
     *     <li>발급 가능한 쿠폰 선정 과정은 결제 주문 쿠폰 발급과 동일하며 1차 검증까지는 동일하다. 단, 2차 검증 및 기간 검증은 별도 정책이 적용되어 필터링된다.</li>
     *     <li>발급 직전에 발급 수량 검증이 다시 이루어지며 해당 과정에서 예외가 발생 시 다음 우선순위 쿠폰을 발급한다. 만약 다음 쿠폰이 없다면 발급이 취소된다.</li>
     * </p>
     *
     * @param userId    쿠폰을 발급 받을 사용자
     * @param paymentOrderId    쿠폰 발급의 트리거가 된 결제 상품 이벤트
     */
    @Transactional
    public void issuePaymentOrderCoupon(Long userId, Long paymentOrderId) {

        // 1. 전체 쿠폰 조회
        List<Coupon> coupons = couponRepository.findAllActiveCoupons();

        // 2. 1차 검증 & 2차 검증 & 기간 검증
        List<CouponConditionCheckResultDTO> availablePaymentOrderCoupons = coupons.stream()
                .filter(coupon -> couponValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_PRODUCT))
                .filter(coupon -> couponValidator.validatePaymentOrderCoupon(coupon, paymentOrderId))
                .map(coupon -> couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentOrderId))
                .toList();

        // 3. 발급한 쿠폰 선택
        CouponConditionCheckResultDTO selectedCouponInfo = couponPrioritySelector.selectHighestPriorityCoupon(availablePaymentOrderCoupons);

        // 4. 발급 처리
        // TODO: END_FIRST_COUNT_COUPON Exception 발생할 경우 다른 쿠폰 발행 로직 진행
        UserCoupon userCoupon = couponIssuanceManager.issueCouponToUser(userId, selectedCouponInfo.getCoupon().getId(), TriggerType.PRODUCT);

        // 5. 결제 상품 쿠폰 발급 이력 저장
        for (Long triggerId : selectedCouponInfo.getTriggerIds()) {
            CouponTrigger couponTrigger = CouponTrigger.builder().userCouponId(userCoupon.getId()).userId(userId).triggerType(CouponTriggerType.PRODUCT).triggerId(triggerId).build();
            couponTriggerRepository.save(couponTrigger);
        }
    }

    /**
     * SNS 태그 이벤트 처리
     * @param userId
     */
    public void issueSocialMediaCoupon(Long userId) {

    }
}
