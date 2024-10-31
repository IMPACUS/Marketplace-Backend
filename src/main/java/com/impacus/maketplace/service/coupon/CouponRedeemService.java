package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.model.ValidateOrderCouponInfoDTO;
import com.impacus.maketplace.dto.coupon.model.ValidateProductCouponInfoDTO;
import com.impacus.maketplace.dto.coupon.model.ValidatedPaymentCouponInfosDTO;
import com.impacus.maketplace.dto.payment.model.CouponValidationRequestDTO;
import com.impacus.maketplace.dto.payment.model.PaymentCouponDTO;
import com.impacus.maketplace.entity.coupon.PaymentEventCoupon;
import com.impacus.maketplace.entity.coupon.PaymentOrderCoupon;
import com.impacus.maketplace.repository.coupon.PaymentEventCouponRepository;
import com.impacus.maketplace.repository.coupon.PaymentOrderCouponRepository;
import com.impacus.maketplace.repository.coupon.querydsl.CouponCustomRepositroy;
import com.impacus.maketplace.repository.coupon.querydsl.dto.PaymentUserCouponInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
     * 사용하는 모든 쿠폰에 대한 쿠폰 조회 및 검증
     */
    public ValidatedPaymentCouponInfosDTO getValidatedPaymentCouponInfos(Long userId, CouponValidationRequestDTO couponValidationRequestDTO) {

        if (couponValidationRequestDTO.isEmpty()) {
            return ValidatedPaymentCouponInfosDTO.createEmptyValidatedCouponsDTO();
        }

        // 1. 쿠폰 리스트 가져오기
        List<PaymentUserCouponInfo> paymentUserCouponInfos = couponCustomRepositroy.findPaymentUserCouponInfos(userId, couponValidationRequestDTO.getUserCouponIds());

        if (couponValidationRequestDTO.getUserCouponCount() != paymentUserCouponInfos.size()) {
            throw new CustomException(CouponErrorType.INVALID_ACCESS_USER_COUPON);
        }

        // 2. userCouponId를 key값으로 가지는 Map 생성
        Map<Long, PaymentUserCouponInfo> paymentUserCouponInfoMap = paymentUserCouponInfos.stream()
                .collect(Collectors.toMap(
                        PaymentUserCouponInfo::getUserCouponId,
                        item -> item
                ));

        // 3.1 상품에 적용된 쿠폰 검증
        couponValidationRequestDTO.getProductInfosForCoupon().forEach(productInfo -> {
            productInfo.getAppliedCouponIds()
                    .forEach(couponId -> {
                        PaymentUserCouponInfo userCouponDTO = paymentUserCouponInfoMap.get(couponId);
                        couponValidationService.validateCouponForProductWithException(ValidateProductCouponInfoDTO.fromDto(userCouponDTO), productInfo.getProductType(), productInfo.getMarketName(), productInfo.getProductPrice(), productInfo.getQuantity());
                    });
        });

        // 3.2 주문에 적용된 쿠폰 검증
        couponValidationRequestDTO.getUserCouponIds()
                .forEach(couponId -> {
                    PaymentUserCouponInfo userCouponDTO = paymentUserCouponInfoMap.get(couponId);
                    couponValidationService.validateCouponForOrderWithException(ValidateOrderCouponInfoDTO.fromDto(userCouponDTO), couponValidationRequestDTO.getOrderTotalPrice());
                });

        // 4.1 상품에 적용된 쿠폰 반환 데이터 생성
        Map<Long, List<PaymentCouponDTO>> productCoupons = couponValidationRequestDTO.getProductInfosForCoupon().stream()
                .collect(Collectors.toMap(
                        CouponValidationRequestDTO.ProductCouponValidationData::getProductId,
                        productCouponValidationData -> productCouponValidationData.getAppliedCouponIds().stream()
                                .map(userCouponId -> {
                                    PaymentUserCouponInfo userCouponDTO = paymentUserCouponInfoMap.get(userCouponId);
                                    return new PaymentCouponDTO(userCouponId, userCouponDTO.getBenefitType(), userCouponDTO.getBenefitValue());
                                }).toList()
                ));

        // 4.2 주문에 적용된 쿠폰 반환 데이터 생성
        List<PaymentCouponDTO> orderCoupons = couponValidationRequestDTO.getOrderCouponIds().stream()
                .map(userCouponId -> {
                    PaymentUserCouponInfo userCouponDTO = paymentUserCouponInfoMap.get(userCouponId);
                    return new PaymentCouponDTO(userCouponId, userCouponDTO.getBenefitType(), userCouponDTO.getBenefitValue());
                }).toList();

        return new ValidatedPaymentCouponInfosDTO(productCoupons, orderCoupons);
    }

    /**
     * 상품에 대한 쿠폰 적용 유효성 검증 후 필요한 정보 가져오기
     */
    public List<PaymentCouponDTO> getPaymentCouponForProductAfterValidation(Long userId, List<Long> usedUserCouponIds, ProductType productType, String marketName, int appSalesPrice, Long quantity) {

        if (usedUserCouponIds == null || usedUserCouponIds.isEmpty()) return new ArrayList<>();

        // 1. 쿠폰 리스트 가져오기
        List<PaymentUserCouponInfo> userCouponInfos = couponCustomRepositroy.findPaymentUserCouponInfos(userId, usedUserCouponIds);

        if (userCouponInfos.size() != usedUserCouponIds.size()) {
            throw new CustomException(CouponErrorType.INVALID_ACCESS_USER_COUPON);
        }

        // 2. 쿠폰 하나씩 검증
        userCouponInfos.forEach(coupon -> {
            couponValidationService.validateCouponForProductWithException(ValidateProductCouponInfoDTO.fromDto(coupon), productType, marketName, (long) appSalesPrice, quantity);
        });

        List<PaymentCouponDTO> list = new ArrayList<>();
        userCouponInfos.forEach(coupon -> {
            list.add(new PaymentCouponDTO(coupon.getUserCouponId(), coupon.getBenefitType(), coupon.getBenefitValue()));
        });

        return list;
    }

    /**
     * 주문 대한 쿠폰 적용 유효성 검증 후 필요한 정보 가져오기
     */
    public List<PaymentCouponDTO> getPaymentCouponForOrderAfterValidation(Long userId, List<Long> usedUserCouponsIds, Long totalPrice) {

        if (usedUserCouponsIds == null || usedUserCouponsIds.isEmpty()) return new ArrayList<>();

        // 1. 쿠폰 리스트 가져오기
        List<PaymentUserCouponInfo> coupons = couponCustomRepositroy.findPaymentUserCouponInfos(userId, usedUserCouponsIds);

        if (coupons.size() != usedUserCouponsIds.size()) {
            throw new CustomException(CouponErrorType.INVALID_ACCESS_USER_COUPON);
        }

        // 2. 쿠폰 하나씩 검증
        coupons.forEach(coupon -> {
            couponValidationService.validateCouponForOrderWithException(ValidateOrderCouponInfoDTO.fromDto(coupon), totalPrice);
        });

        List<PaymentCouponDTO> list = new ArrayList<>();
        coupons.forEach(coupon -> {
            list.add(new PaymentCouponDTO(coupon.getUserCouponId(), coupon.getBenefitType(), coupon.getBenefitValue()));
        });

        return list;
    }

}
