package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.payment.PaymentCouponDTO;
import com.impacus.maketplace.entity.coupon.PaymentEventCoupon;
import com.impacus.maketplace.entity.coupon.PaymentOrderCoupon;
import com.impacus.maketplace.repository.coupon.PaymentEventCouponRepository;
import com.impacus.maketplace.repository.coupon.PaymentOrderCouponRepository;
import com.impacus.maketplace.repository.coupon.querydsl.CouponCustomRepositroy;
import com.impacus.maketplace.repository.coupon.querydsl.dto.ValidateUserCouponForOrderDTO;
import com.impacus.maketplace.repository.coupon.querydsl.dto.ValidateUserCouponForProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 사용자가 쿠폰을 사용하는 것과 관련된 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponRedeemService {

    private final CouponCustomRepositroy couponCustomRepositroy;
    private final PaymentEventCouponRepository paymentEventCouponRepository;
    private final PaymentOrderCouponRepository paymentOrderCouponRepository;
    private final CouponValidationService couponValidationService;

    /**
     * Payment Event Coupon(상품에 등록하는 쿠폰) 이력 등록
     */
    @Transactional
    public void registPaymentEventCoupon(Long paymentEventId, Long userCouponId) {
        List<Long> userCouponIds = new ArrayList<>();
        userCouponIds.add(userCouponId);
        registPaymentEventCoupons(paymentEventId, userCouponIds);
    }

    /**
     * Payment Order Coupon(주문에 등록하는 쿠폰) 이력 등록
     */
    @Transactional
    public void registPaymentOrderCoupon(Long paymentOrderId, Long userCouponId) {
        List<Long> userCouponIds = new ArrayList<>();
        userCouponIds.add(userCouponId);
        registPaymentOrderCoupons(paymentOrderId, userCouponIds);
    }

    /**
     * 단일 상품에 대해 여러 개의 쿠폰 등록
     */
    @Transactional
    public void registPaymentEventCoupons(Long paymentEventId, List<Long> userCouponIds) {
        // 0. userCouponIds이 null일 경우 바로 동작 종료
        if (userCouponIds == null) return;

        // 1. PaymentEventCoupon 엔티티 전부 생성
        List<PaymentEventCoupon> paymentEventCoupons = userCouponIds.stream().map(userCouponId ->
                        PaymentEventCoupon.builder()
                                .paymentEventId(paymentEventId)
                                .userCouponId(userCouponId)
                                .isUsed(false)
                                .build()
                )
                .toList();

        // 2. 저장
        paymentEventCouponRepository.saveAll(paymentEventCoupons);
    }

    /**
     * 단일 주문에 대해 여러 개의 쿠폰 등록
     */
    @Transactional
    public void registPaymentOrderCoupons(Long paymentOrderId, List<Long> userCouponIds) {
        // 0. userCouponIds이 null일 경우 바로 동작 종료
        if (userCouponIds == null) return;

        // 1. PaymentOrderCoupon 엔티티 전부 생성
        List<PaymentOrderCoupon> paymentOrderCoupons = userCouponIds.stream().map(userCouponId ->
                        PaymentOrderCoupon.builder()
                                .paymentOrderId(paymentOrderId)
                                .userCouponId(userCouponId)
                                .isUsed(false)
                                .build()
                )
                .toList();

        // 2. 저장
        paymentOrderCouponRepository.saveAll(paymentOrderCoupons);
    }

    /**
     * 여러 Payment Event에 대해 여러 개의 Payment Event Coupon 이력 등록
     * @param paymentEventCouponMap key: productEventId / value: userCouponIds
     */
    @Transactional
    public void registPaymentEventsCoupons(Map<Long, List<Long>> paymentEventCouponMap) {

        // 1. paymentEventId 전부 가져오기
        Set<Long> keys = paymentEventCouponMap.keySet();

        // 2. 반복문을 통해 순차적으로 저장
        for (Long paymentEventId : keys) {
            registPaymentEventCoupons(paymentEventId, paymentEventCouponMap.get(paymentEventId));
        }
    }

    /**
     * 상품에 대한 쿠폰 적용 유효성 검증 후 필요한 정보 가져오기
     */
    public List<PaymentCouponDTO> getPaymentCouponForProductAfterValidation(Long userId, List<Long> usedUserCouponIds, com.impacus.maketplace.common.enumType.product.ProductType productType, String marketName, int appSalesPrice, Long quantity) {

        if (usedUserCouponIds.isEmpty()) return new ArrayList<>();

        // 1. 쿠폰 리스트 가져오기
        List<ValidateUserCouponForProductDTO> coupons = couponCustomRepositroy.findUserCouponInfoForValidateForProductByIds(userId, usedUserCouponIds);

        if (coupons.size() != usedUserCouponIds.size()) {
            throw new CustomException(CouponErrorType.INVALID_ACCESS_USER_COUPON);
        }

        // 2. 쿠폰 하나씩 검증
        coupons.forEach(coupon -> {
            if (!couponValidationService.validateCouponForProduct(coupon, productType, marketName, appSalesPrice, quantity)) {
                throw new CustomException(CouponErrorType.INVALID_APPLIED_USER_COUPON);
            }
        });

        List<PaymentCouponDTO> list = new ArrayList<>();
        coupons.forEach(coupon -> {
            list.add(new PaymentCouponDTO(coupon.getUserCouponId(), coupon.getBenefitType(), coupon.getBenefitValue()));
        });

        return list;
    }

    /**
     * 주문 대한 쿠폰 적용 유효성 검증 후 필요한 정보 가져오기
     */
    public List<PaymentCouponDTO> getPaymentCouponForOrderAfterValidation(Long userId, List<Long> usedUserCouponsIds, Long totalPrice) {

        if (usedUserCouponsIds.isEmpty()) return new ArrayList<>();

        // 1. 쿠폰 리스트 가져오기
        List<ValidateUserCouponForOrderDTO> coupons = couponCustomRepositroy.findUserCouponInfoForValidateForOrderByIds(userId, usedUserCouponsIds);

        if (coupons.size() != usedUserCouponsIds.size()) {
            throw new CustomException(CouponErrorType.INVALID_ACCESS_USER_COUPON);
        }

        // 2. 쿠폰 하나씩 검증
        coupons.forEach(coupon -> {
            if (!couponValidationService.validateCouponForOrder(coupon, totalPrice)) {
                throw new CustomException(CouponErrorType.INVALID_APPLIED_USER_COUPON);
            }
        });

        List<PaymentCouponDTO> list = new ArrayList<>();
        coupons.forEach(coupon -> {
            list.add(new PaymentCouponDTO(coupon.getUserCouponId(), coupon.getBenefitType(), coupon.getBenefitValue()));
        });

        return list;
    }

}
