package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.dto.payment.DiscountInfoDTO;
import com.impacus.maketplace.dto.payment.PaymentCouponDTO;
import com.impacus.maketplace.dto.payment.ProductPricingDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class DiscountService {

    public Long calculateProductCouponDiscount(Long productId, Long productTotalPrice, List<PaymentCouponDTO> productCoupons) {
        Map<Long, Long> productPrices = Collections.singletonMap(productId, productTotalPrice);
        Map<Long, List<PaymentCouponDTO>> productCouponsMap = Collections.singletonMap(productId, productCoupons);
        Map<Long, Long> discountMap = calculateProductCouponDiscounts(productPrices, productCouponsMap);
        return discountMap.get(productId);
    }


    // 개별 상품 쿠폰 할인 계산
    public Map<Long, Long> calculateProductCouponDiscounts(Map<Long, Long> productTotalPrices, Map<Long, List<PaymentCouponDTO>> productCoupons) {

        Map<Long, Long> productCouponDiscounts = new HashMap<>();

        Set<Long> productIds = productTotalPrices.keySet();

        for (Long productId : productIds) {
            Long productTotalPrice = productTotalPrices.get(productId);

            List<PaymentCouponDTO> paymentCoupons = productCoupons.get(productId);

            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal productPrice = BigDecimal.valueOf(productTotalPrice);

            if (paymentCoupons != null) {
                for (PaymentCouponDTO paymentCoupon : paymentCoupons) {
                    BigDecimal benefitValue = BigDecimal.valueOf(paymentCoupon.getBenefitValue());
                    if (paymentCoupon.getBenefitType() == BenefitType.AMOUNT) {
                        // 정액 할인
                        discountAmount = discountAmount.add(benefitValue);
                    } else {
                        if (benefitValue.compareTo(BigDecimal.ZERO) == 0) continue;
                        // 정률 할인
                        BigDecimal discount = productPrice.multiply(benefitValue);

                        // 100으로 나누고 소수점 이하 버림
                        BigDecimal discountIntegerPart = discount.divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR);

                        discountAmount = discountAmount.add(discountIntegerPart);
                    }
                }

                // 할인 금액이 상품 가격을 초과하지 않도록 제한
                discountAmount = discountAmount.min(productPrice);
            }

            // 할인 금액을 Long 타입으로 변환하여 저장
            productCouponDiscounts.put(productId, discountAmount.longValue());
        }

        return productCouponDiscounts;
    }


    // 주문 전체 쿠폰 할인 계산 및 상품별 분배
    public Map<Long, Long> calculateOrderCouponDiscount(Long totalOrderPrice, Map<Long, Long> productPrices, List<PaymentCouponDTO> orderCoupons) {
        // 구현 내용
        return null;
    }

    // 포인트 할인 계산 및 상품별 분배
    public Map<Long, Long> calculatePointDiscount(Long totalOrderPrice, Map<Long, Long> productPrices, Long usedPointAmount) {
        // 구현 내용
        return null;
    }

    // 할인 금액 조정 및 최종 할인 정보 생성
    public Map<Long, DiscountInfoDTO> reconcileDiscountAmounts(
            Map<Long, ProductPricingDTO> productPricingInfo,
            Map<Long, Long> productCouponDiscounts,
            Map<Long, Long> orderCouponDiscounts,
            Map<Long, Long> pointDiscounts) {
        // 구현 내용
        return null;
    }
}
