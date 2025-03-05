package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import liquibase.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * <h2>쿠폰 발급 시 검증 로직</h2>
 * <p>
 * 이 클래스는 쿠폰 발급과 관련하여
 * <li>관리자 발급</li>
 * <li>지급형 쿠폰 발급</li>
 * <li>이벤트 쿠폰 발급</li>
 * <li>결제 상품/이벤트 쿠폰 발급</li>
 * 등 각 상황별로 쿠폰의 유효성을 검증하는 역할을 수행합니다.
 * </p>
 *
 * <p>
 * 공통 검증 항목: 쿠폰이 삭제되지 않았으며, 발급 중지 상태(STOP)가 아니고,
 * 선착순 쿠폰의 경우 아직 발급 가능한 수량(First Count)이 남아있는지 등
 * 기본적인 유효성을 확인합니다.
 * </p>
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponIssuanceValidator {

    private final UserCouponRepository userCouponRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final SellerRepository sellerRepository;

    /**
     * ADMIN이 사용자에게 쿠폰을 발급하는 경우, 해당 쿠폰의 유효성을 검증합니다.
     *
     * <p>
     * 조건:
     * <ul>
     *   <li>쿠폰이 삭제되지 않았어야 한다.</li>
     * </ul>
     * </p>
     *
     * @param coupon 검증할 쿠폰 엔티티
     * @throws CustomException 쿠폰이 삭제된 경우
     */
    public void validateAdminIssuedCouponWithException(Coupon coupon) {
        // 쿠폰이 삭제되어 있다면 예외 발생
        if (coupon.getIsDeleted()) {
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }
    }


    /**
     * 지급형(Provision) 쿠폰 발급 시 검증 조건
     *
     * <p>
     * 조건:
     * <ol>
     *   <li>쿠폰에 대한 공통 유효성(삭제, 중지, 선착순 수량 등)을 만족해야 한다.</li>
     *   <li>쿠폰 타입이 지급형이어야 한다.</li>
     *   <li>쿠폰 발급 유형이 1회성(Onetime)이어야 한다.</li>
     *   <li>해당 사용자에게 이미 발급된 이력이 없어야 한다.</li>
     *   <li>지정된 기간 내에 있어야 함 (기간이 설정되어 있을 경우).</li>
     * </ol>
     * </p>
     *
     * @param userId 사용자의 아이디
     * @param coupon 검증할 쿠폰 엔티티
     * @throws CustomException 조건 미충족 시 적절한 예외 발생
     */
    public void validateProvisionCouponWithException(Long userId, Coupon coupon) {
        // 1. 쿠폰의 공통 유효성 검증 (삭제, 중지, 선착순 수량 등)
        isCouponEligibleWithException(coupon);

        // 2. 지급형 쿠폰인지 확인
        if (coupon.getCouponType() != CouponType.PROVISION) {
            LogUtils.writeErrorLog("validateProvisionCouponWithException", "지급형 쿠폰 검증 실패: 쿠폰 타입이 PROVISION이 아님.");
            throw new CustomException(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }

        // 3. 쿠폰 발급 유형이 1회성이어야 함
        if (coupon.getCouponIssueType() != CouponIssueType.ONETIME) {
            LogUtils.writeErrorLog("validateProvisionCouponWithException", "지급형 쿠폰 검증 실패: 쿠폰 발급 유형이 ONETIME이 아님.");
            throw new CustomException(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }

        // 4. 해당 사용자가 이미 이 쿠폰을 발급받은 이력이 없어야 함
        if (userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
            LogUtils.writeErrorLog("validateProvisionCouponWithException", "지급형 쿠폰 검증 실패: 쿠폰 발급 받은 이력 존재");
            throw new CustomException(CouponErrorType.ALREADY_ISSUED_COUPON);
        }

        // 5. 기간 설정이 되어 있을 경우 지정된 기간 내에 있는지 확인
        if (!isWithinPeriod(coupon)) {
            LogUtils.writeErrorLog("validateProvisionCouponWithException", "지급형 쿠폰 검증 실패: 쿠폰이 유효 기간 내에 있지 않음.");
            // TODO: 발행 시작하지 않았을 때 발행이 언제 시작하고 언제 종료되는지 프론트에 정보를 담아서 보낼지 결정
            throw new CustomException(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }
    }

    /**
     * 이벤트 쿠폰의 유효성을 검증합니다.
     *
     * <p>
     * 조건:
     * <ol>
     *   <li>쿠폰의 공통 유효성 조건을 만족해야 함.</li>
     *   <li>쿠폰 타입이 이벤트여야 함.</li>
     *   <li>쿠폰의 이벤트 타입이 전달된 이벤트 타입과 일치해야 함.</li>
     *   <li>쿠폰 발급 유형이 일회성인 경우, 사용자가 이미 발급받은 이력이 없어야 함.</li>
     * </ol>
     * </p>
     *
     * @param userId    사용자 아이디
     * @param coupon    검증할 쿠폰 엔티티
     * @param eventType 이벤트 타입
     * @return 모든 조건을 만족하면 true, 그렇지 않으면 false
     */
    public boolean validateEventCoupon(Long userId, Coupon coupon, EventType eventType) {
        // 1. 쿠폰 공통 유효성 검사
        if (!isCouponEligible(coupon)) return false;

        // 2. 쿠폰 타입이 이벤트여야 함
        if (coupon.getCouponType() != CouponType.EVENT) return false;

        // 3. 쿠폰 이벤트 타입이 일치해야 함
        if (coupon.getEventType() != eventType) return false;

        // 4. 일회성 쿠폰인 경우, 이미 발급받은 이력이 없어야 함
        if (coupon.getCouponIssueType() == CouponIssueType.ONETIME
                && userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
            return false;
        }

        return true;
    }


    /**
     * 결제 상품과 관련된 쿠폰의 유효성을 검증합니다.
     *
     * <p>
     * 조건:
     * <ol>
     *   <li>쿠폰 타입이 이벤트여야 하며, 결제 상품 관련 이벤트가 아닌 경우 false</li>
     *   <li>발급 적용 범위가 브랜드인 경우, 해당 결제 주문의 판매자(브랜드)와 일치해야 함</li>
     *   <li>쿠폰 지급 조건(판매가 합계 등)이 충족되어야 함</li>
     * </ol>
     * </p>
     *
     * @param coupon         검증할 쿠폰 엔티티
     * @param paymentOrderId 결제 주문 아이디
     * @return 조건을 만족하면 true, 아니면 false
     */
    public boolean validatePaymentOrderCoupon(Coupon coupon, Long paymentOrderId) {
        return paymentOrderRepository.findById(paymentOrderId)
                .map(paymentOrder -> {
                    // 1. 쿠폰 타입이 EVENT가 아니거나, 결제 상품 관련 이벤트가 아닌 경우
                    if (coupon.getCouponType() != CouponType.EVENT || !EventType.isRelatedPaymentProduct(coupon.getEventType()))
                        return false;

                    // 2. 발급 적용 범위가 브랜드인 경우, 판매자의 브랜드와 일치해야 함
                    if (coupon.getIssueCoverageType() == CoverageType.BRAND) {
                        String brandName = sellerRepository.findMarketNameBySellerId(paymentOrder.getSellerId());
                        if (!coupon.getIssueCoverageSubCategoryName().equals(brandName)) {
                            return false;
                        }
                    }

                    // 3. 쿠폰 지급 조건 확인 (판매가 합계 등)
                    if (!meetsCouponIssueCondition(coupon, paymentOrder)) return false;

                    return true;
                }).orElse(false);   // PaymentOrder가 존재하지 않으면 false 반환
    }

    /**
     * 결제 이벤트와 관련된 쿠폰의 유효성을 검증합니다.
     *
     * <p>
     * 조건:
     * <ol>
     *   <li>쿠폰 타입이 이벤트여야 하며, 결제 주문 관련 이벤트 타입이어야 함</li>
     *   <li>발급 적용 범위가 모든 상품/브랜드여야 함 (특정 브랜드 제외)</li>
     *   <li>쿠폰 지급 조건이 충족되어야 함</li>
     * </ol>
     * </p>
     *
     * @param coupon         검증할 쿠폰 엔티티
     * @param paymentEventId 결제 이벤트 아이디
     * @return 조건을 만족하면 true, 아니면 false
     */
    public boolean validatePaymentEventCoupon(Coupon coupon, Long paymentEventId) {
        return paymentEventRepository.findById(paymentEventId)
                .map(paymentEvent -> {
                    // 1. 결제 이벤트와 관련된 PaymentOrder 목록 조회
                    List<PaymentOrder> paymentOrders = paymentOrderRepository.findByPaymentEventId(paymentEvent.getId()).orElse(Collections.emptyList());

                    if (paymentOrders.isEmpty()) return false;

                    // 1.1 PaymentEvent 엔티티에 PaymentOrder 목록 설정
                    paymentEvent.setPaymentOrders(paymentOrders);

                    // 2. 쿠폰 타입 및 이벤트 타입 검증
                    if (coupon.getCouponType() != CouponType.EVENT || EventType.PAYMENT_ORDER != coupon.getEventType())
                        return false;

                    // 3. 발급 적용 범위 확인: 결제 이벤트 관련 쿠폰은 브랜드 지정이 불가함
                    if (coupon.getIssueCoverageType() == CoverageType.BRAND)
                        return false;

                    // 4. 쿠폰 지급 조건 검증
                    if (coupon.getIssueConditionType() == StandardType.LIMIT && paymentEvent.getTotalAmount() < coupon.getIssueConditionValue())
                        return false;

                    return true;
                }).orElse(false);     // PaymentEvent가 없으면 false 반환
    }

    /**
     * 쿠폰 지급 조건을 확인합니다.
     *
     * <p>
     * 조건:
     * <ol>
     *   <li>쿠폰 지급 조건이 UNLIMITED이면 true</li>
     *   <li>LIMITED인 경우, 해당 결제 주문(들)의 판매가 합계가 쿠폰 지급 조건 값 이상이어야 함</li>
     * </ol>
     * </p>
     *
     * @param coupon        검증할 쿠폰 엔티티
     * @param paymentOrders 결제 주문 리스트
     * @return 지급 조건이 충족되면 true, 아니면 false
     */
    private boolean meetsCouponIssueCondition(Coupon coupon, List<PaymentOrder> paymentOrders) {
        if (coupon.getIssueConditionType() == StandardType.LIMIT) {
            long totalPrice = paymentOrders.stream()
                    .mapToLong(PaymentOrder::getNotDiscountedAmount)
                    .sum();
            return totalPrice >= coupon.getIssueConditionValue();
        }
        return true;
    }

    /**
     * 쿠폰 지급 조건을 확인합니다.
     * (단일 PaymentOrder를 대상으로 할 경우 내부적으로 List로 변환하여 재사용)
     *
     * @param coupon       검증할 쿠폰 엔티티
     * @param paymentOrder 결제 주문 엔티티
     * @return 지급 조건이 충족되면 true, 아니면 false
     */
    private boolean meetsCouponIssueCondition(Coupon coupon, PaymentOrder paymentOrder) {
        return meetsCouponIssueCondition(coupon, List.of(paymentOrder));
    }

    /**
     * 쿠폰의 공통 조건을 확인합니다.
     *
     * <p>
     * 조건:
     * <ol>
     *   <li>쿠폰이 삭제되지 않았고</li>
     *   <li>발급 상태가 중지(STOP) 상태가 아니며</li>
     *   <li>선착순 쿠폰인 경우, 발급 가능한 수량이 남아있어야 함</li>
     * </ol>
     * </p>
     *
     * @param coupon 검증할 쿠폰 엔티티
     * @return 조건을 만족하면 true, 아니면 false
     */
    private boolean isCouponEligible(Coupon coupon) {
        return !coupon.getIsDeleted() && coupon.getStatusType() != CouponStatusType.STOP && isWithinQuota(coupon);
    }

    /**
     * 쿠폰의 공통 조건을 위배할 경우 예외를 발생시킵니다.
     *
     * @param coupon 검증할 쿠폰 엔티티
     * @throws CustomException 위배되는 조건에 따라 적절한 예외 발생
     */
    private void isCouponEligibleWithException(Coupon coupon) {
        if (coupon.getIsDeleted()) {
            LogUtils.writeErrorLog("validateProvisionCouponWithException", "지급형 쿠폰 검증 실패: 쿠폰이 삭제됨.");
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }

        if (coupon.getStatusType() == CouponStatusType.STOP) {
            LogUtils.writeErrorLog("validateProvisionCouponWithException", "지급형 쿠폰 검증 실패: 쿠폰 발행이 중지됨.");
            throw new CustomException(CouponErrorType.IS_STOP_COUPON);
        }

        if (!isWithinQuota(coupon)) {
            LogUtils.writeErrorLog("validateProvisionCouponWithException", "지급형 쿠폰 검증 실패: 쿠폰 발행 수가 선착순 발행 수를 초과함.");
            throw new CustomException(CouponErrorType.END_FIRST_COUNT_COUPON);
        }
    }

    /**
     * 선착순 쿠폰의 발급 가능 여부를 확인합니다.
     *
     * @param coupon 검증할 쿠폰 엔티티
     * @return 선착순 조건을 만족하면 true, 그렇지 않으면 false
     */
    private boolean isWithinQuota(Coupon coupon) {
        return !(coupon.getPaymentTarget().equals(PaymentTarget.FIRST) && coupon.getQuantityIssued() >= coupon.getFirstCount());
    }

    /**
     * 쿠폰에 적용되는 기간 조건을 확인합니다.
     *
     * <p>
     * 기간 타입이 SET인 경우, 현재 날짜가 시작일과 종료일 사이에 있어야 합니다.
     * </p>
     *
     * @param coupon 검증할 쿠폰 엔티티
     * @return 기간 조건을 만족하면 true, 아니면 false
     */
    private boolean isWithinPeriod(Coupon coupon) {
        if (coupon.getPeriodType() == PeriodType.SET) {
            LocalDate now = LocalDate.now();
            return !now.isBefore(coupon.getPeriodStartAt()) && !now.isAfter(coupon.getPeriodEndAt());
        }
        return true;
    }
}
