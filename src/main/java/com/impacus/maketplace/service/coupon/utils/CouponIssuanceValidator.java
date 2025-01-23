package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * 발급하는 과정에서 발생하는 쿠폰 유효성 검증
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponIssuanceValidator {

    private final UserCouponRepository userCouponRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final SellerRepository sellerRepository;

    /**
     * <h3>ADMIN이 사용자에게 쿠폰을 발급해주는 경우 조건</h3>
     * <p>1. 삭제 되지 않은 경우</p>
     */
    public void validateAdminIssuedCouponWithException(Coupon coupon) {
        // 1. 삭제 여부 확인
        if (coupon.getIsDeleted()) {
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }
    }

    /**
     * <h3>지급형 쿠폰 조건</h3>
     * <p>1. 쿠폰 공통 조건</p>
     * <p>2. 지급형 쿠폰</p>
     * <p>3. 1회성 쿠폰</p>
     * <p>4. 발급 받은 이력 X</p>
     */
    public void validateProvisionCouponWithException(Long userId, Coupon coupon) {
        // 1. 쿠폰 공통 조건 검증
        isCouponEligibleWithException(coupon);

        // 2. 쿠폰 형식 확인 - 지급형
        if (!coupon.getCouponType().equals(CouponType.PROVISION)) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_EVENT_COUPON);
        }

        // 3. 쿠폰 발급 횟수 - 1회성
        if (!coupon.getCouponIssueType().equals(CouponIssueType.ONETIME)) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_PERSISTENCE_COUPON);
        }

        // 4. 발급 이력 검증
        if (userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_ALREADY_ISSUE);
        }
    }

    /**
     * <h3>이벤트 쿠폰 공통 조건</h3>
     * <p>1. 쿠폰 공통 조건</p>
     * <p>2. 이벤트 쿠폰</p>
     * <p>3. 쿠폰 이벤트 타입 일치</p>
     * <p>4. 기간 내 설정이 되어 있을 경우 기간 확인</p>
     * <p>5. 일회성 쿠폰인 경우 발급 받은 이력 X</p>
     */
    public boolean validateEventCoupon(Long userId, Coupon coupon, EventType eventType) {
        // 1. 쿠폰 공통 조건 검증
        if (!isCouponEligible(coupon)) return false;

        // 2. 쿠폰 형식 확인 - 이벤트
        if (!coupon.getCouponType().equals(CouponType.EVENT)) return false;

        // 3. 쿠폰 이벤트 타입 확인
        if (!coupon.getEventType().equals(eventType)) return false;

        // 4. 기간 확인
        if (!isWithinEventPeriod(coupon)) return false;

        // 5. 발급 이력 확인
        if (coupon.getCouponIssueType().equals(CouponIssueType.ONETIME) && userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId()))
            return false;

        return true;
    }

    /**
     * <h3>결제 상품과 관련된 쿠폰 조건</h3>
     * <p>1. 쿠폰 이벤트 타입 확인</p>
     * <p>2. 발급 적용 범위 확인<br/>
     * 2.1 결제 상품과 관련된 쿠폰은 발급 적용 범위가 모든 상품/브랜드 및 특정 브랜드 모두 가능하다.</p>
     * <p>3. 쿠폰 지급 조건 확인</p>
     */
    public boolean validatePaymentOrderCoupon(Coupon coupon, Long paymentOrderId) {
        return paymentOrderRepository.findById(paymentOrderId).map(paymentOrder -> {

            // 1. 쿠폰 이벤트 타입이 EVENT가 아니거나 결제 상품 관련 이벤트가 아닐경우
            if (coupon.getCouponType() != CouponType.EVENT || EventType.isRelatedPaymentProduct(coupon.getEventType()))
                return false;

            // 2. 발급 적용 범위 확인
            if (coupon.getIssueCoverageType() == CoverageType.BRAND) {
                String brandName = sellerRepository.findMarketNameBySellerId(paymentOrder.getSellerId());

                if (!coupon.getIssueCoverageSubCategoryName().equals(brandName)) {
                    return false;
                }
            }

            // 3. 쿠폰 지급 조건 확인 (??? 현재 N만원 이상의 상품 구매의 조건 정책이 구체화되어 있지 않다.)
//            if ()


            return true;
        }).orElse(false);
    }

    /**
     * <h3>결제 이벤트와 관련된 쿠폰 조건</h3>
     * <p>1. 쿠폰 이벤트 타입 확인</p>
     * <p>2. 발급 적용 범위 확인<br/>
     * 2.1 결제 주문과 관련된 쿠폰은 발급 적용 범위가 모든 상품/브랜드에만 해당한다.</p>
     * <p>3. 쿠폰 지급 조건 확인</p>
     */
    public boolean validatePaymentEventCoupon(Coupon coupon, Long paymentEventId) {
        return paymentEventRepository.findById(paymentEventId).map(paymentEvent -> {
            // 1. Payment Event 조회 후 관련된 PaymentOrder 조회
            List<PaymentOrder> paymentOrders = paymentOrderRepository.findByPaymentEventId(paymentEvent.getId()).orElse(Collections.emptyList());

            if (paymentOrders.isEmpty()) return false;

            // 1.1 paymentEvent에 넣어주기
            paymentEvent.setPaymentOrders(paymentOrders);

            // 2. 결제 이벤트 쿠폰 조건 확인

            // 2.1 쿠폰 이벤트 타입 확인
            if (coupon.getCouponType() != CouponType.EVENT || EventType.PAYMENT_ORDER != coupon.getEventType())
                return false;

            // 2.2 발급 적용 범위 확인
            if (coupon.getIssueCoverageType() == CoverageType.BRAND)
                return false;


            return true;
        }).orElse(false);     // PaymentEvent가 없을 경우 false 반환
    }


    /**
     * <h3>쿠폰 공통 조건</h3>
     * <p>1. 삭제되지 않은 경우</p>
     * <p>2. 발급 상태가 중지가 아닌 경우</p>
     * <p>3. 선착순 쿠폰이 아니거나 선착순 쿠폰인 경우 발급 수량에 여유가 있는 경우</p>
     */
    private boolean isCouponEligible(Coupon coupon) {
        return !coupon.getIsDeleted() && !coupon.getStatusType().equals(CouponStatusType.STOP) && isWithinQuota(coupon);
    }

    private void isCouponEligibleWithException(Coupon coupon) {
        if (coupon.getIsDeleted()) {
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }

        if (coupon.getStatusType().equals(CouponStatusType.STOP)) {
            throw new CustomException(CouponErrorType.IS_STOP_COUPON);
        }

        if (!isWithinQuota(coupon)) {
            throw new CustomException(CouponErrorType.END_FIRST_COUNT_COUPON);
        }
    }

    /**
     * <h3>선착순 검증</h3>
     */
    private boolean isWithinQuota(Coupon coupon) {
        return !(coupon.getPaymentTarget().equals(PaymentTarget.FIRST) && coupon.getQuantityIssued() >= coupon.getFirstCount());
    }

    /**
     * <h3>쿠폰 기간 확인</h3>
     * <p>이벤트 쿠폰에 한해서 적용되는 조건</p>
     */
    private boolean isWithinEventPeriod(Coupon coupon) {
        return !(coupon.getPeriodType() == PeriodType.SET && (LocalDate.now().isBefore(coupon.getPeriodStartAt()) || LocalDate.now().isAfter(coupon.getPeriodEndAt())));
    }
}
