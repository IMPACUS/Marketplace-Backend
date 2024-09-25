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

    // 단일 상품 구매시 개별 상품 쿠폰 할인 계산
    public Long calculateProductCouponDiscount(Long productId, Long productPrice, List<PaymentCouponDTO> productCoupons) {
        Map<Long, Long> productPrices = Collections.singletonMap(productId, productPrice);
        Map<Long, List<PaymentCouponDTO>> productCouponsMap = Collections.singletonMap(productId, productCoupons);
        Map<Long, Long> discount = calculateProductCouponDiscounts(productPrices, productCouponsMap);
        return discount.get(productId);
    }


    // 개별 상품 쿠폰 할인 계산
    public Map<Long, Long> calculateProductCouponDiscounts(Map<Long, Long> productPrices,
                                                           Map<Long, List<PaymentCouponDTO>> productCoupons) {

        Map<Long, Long> productCouponDiscounts = new HashMap<>();

        Set<Long> productIds = productPrices.keySet();

        for (Long productId : productIds) {
            Long price = productPrices.get(productId);

            List<PaymentCouponDTO> coupons = productCoupons.get(productId);

            BigDecimal totalDiscountAmount = BigDecimal.ZERO;
            BigDecimal productPrice = BigDecimal.valueOf(price);

            if (coupons != null) {
                for (PaymentCouponDTO coupon : coupons) {
                    BigDecimal benefitValue = BigDecimal.valueOf(coupon.getBenefitValue());
                    if (coupon.getBenefitType() == BenefitType.AMOUNT) {
                        // 정액 할인
                        totalDiscountAmount = totalDiscountAmount.add(benefitValue);
                    } else {
                        if (benefitValue.compareTo(BigDecimal.ZERO) == 0) continue;
                        // 정률 할인
                        BigDecimal discount = productPrice.multiply(benefitValue);

                        // 100으로 나누고 소수점 이하 버림
                        BigDecimal discountAmountPerCoupon = discount.divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR);

                        totalDiscountAmount = totalDiscountAmount.add(discountAmountPerCoupon);
                    }
                }

                // 할인 금액이 상품 가격을 초과하지 않도록 제한
                totalDiscountAmount = totalDiscountAmount.min(productPrice);
            }

            // 할인 금액을 Long 타입으로 변환하여 저장
            productCouponDiscounts.put(productId, totalDiscountAmount.longValue());
        }

        return productCouponDiscounts;
    }

    // 단일 상품 구매시 주문 전체 쿠폰 할인 계산
    public Long calculateOrderCouponDiscount(Long productId, Long productPrice, List<PaymentCouponDTO> orderCoupons) {
        Map<Long, Long> productPrices = Collections.singletonMap(productId, productPrice);
        Map<Long, Long> discount = calculateOrderCouponDiscounts(productPrice, productPrices, orderCoupons);
        return discount.get(productId);
    }


    // 주문 전체 쿠폰 할인 계산 및 상품별 분배
    public Map<Long, Long> calculateOrderCouponDiscounts(Long totalOrderPrice, Map<Long, Long> productPrices, List<PaymentCouponDTO> orderCoupons) {

        Map<Long, Long> orderCouponDiscounts = new HashMap<>();
        Set<Long> productIds = productPrices.keySet();
        BigDecimal totalOrderPriceBD = BigDecimal.valueOf(totalOrderPrice);

        for (PaymentCouponDTO coupon : orderCoupons) {
            BigDecimal allocatedDiscountAmount = BigDecimal.ZERO;
            BigDecimal couponBenefitValue = BigDecimal.valueOf(coupon.getBenefitValue());
            Long lastProductId = null;

            if (coupon.getBenefitType() == BenefitType.AMOUNT) {
                // 정액 할인 쿠폰 처리
                for (Long productId : productIds) {
                    Long productPriceLong = productPrices.get(productId);
                    BigDecimal productPrice = BigDecimal.valueOf(productPriceLong);

                    // 상품 가격이 전체 주문 가격에서 차지하는 비율 계산
                    BigDecimal productProportion = productPrice.divide(totalOrderPriceBD, 4, RoundingMode.FLOOR);

                    // 해당 상품에 적용될 할인 금액 계산
                    BigDecimal productDiscount = couponBenefitValue.multiply(productProportion);
                    BigDecimal productDiscountInteger = productDiscount.setScale(0, RoundingMode.FLOOR);

                    // 할인 금액 누적
                    orderCouponDiscounts.merge(productId, productDiscountInteger.longValue(), Long::sum);

                    allocatedDiscountAmount = allocatedDiscountAmount.add(productDiscountInteger);
                    lastProductId = productId;
                }

                // 할인 금액 조정 (소수점 처리로 인한 오차 보정)
                if (couponBenefitValue.compareTo(allocatedDiscountAmount) != 0) {
                    BigDecimal difference = couponBenefitValue.subtract(allocatedDiscountAmount);
                    orderCouponDiscounts.merge(lastProductId, difference.longValue(), Long::sum);
                }
            } else {
                // 정률 할인 쿠폰 처리
                for (Long productId : productIds) {
                    Long productPriceLong = productPrices.get(productId);
                    BigDecimal productPrice = BigDecimal.valueOf(productPriceLong);

                    // 해당 상품에 적용될 할인 금액 계산
                    BigDecimal discount = productPrice.multiply(couponBenefitValue);
                    BigDecimal productDiscountInteger = discount.divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR);

                    // 할인 금액 누적
                    orderCouponDiscounts.merge(productId, productDiscountInteger.longValue(), Long::sum);

                    allocatedDiscountAmount = allocatedDiscountAmount.add(productDiscountInteger);
                    lastProductId = productId;
                }

                // 총 할인 금액 계산
                BigDecimal totalExpectedDiscount = totalOrderPriceBD.multiply(couponBenefitValue)
                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR);

                // 할인 금액 조정 (소수점 처리로 인한 오차 보정)
                if (totalExpectedDiscount.compareTo(allocatedDiscountAmount) != 0) {
                    BigDecimal difference = totalExpectedDiscount.subtract(allocatedDiscountAmount);
                    orderCouponDiscounts.merge(lastProductId, difference.longValue(), Long::sum);
                }
            }
        }

        return orderCouponDiscounts;
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
