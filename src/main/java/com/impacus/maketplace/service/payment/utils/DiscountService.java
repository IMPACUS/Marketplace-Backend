package com.impacus.maketplace.service.payment.utils;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.payment.model.DiscountInfoDTO;
import com.impacus.maketplace.dto.payment.model.PaymentCouponDTO;
import com.impacus.maketplace.dto.payment.model.ProductPricingDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class DiscountService {

    // 단일 상품 구매시 개별 상품 쿠폰 할인 계산
    public Long calculateProductCouponDiscount(Long productId, Long productPrice, List<PaymentCouponDTO> productCoupons) {
        if (productCoupons == null) return 0L;

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

                        // 100으로 나누고 소수점 이하 반올림
                        BigDecimal discountAmountPerCoupon = discount.divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

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
        if (orderCoupons == null || orderCoupons.isEmpty()) return 0L;

        Map<Long, Long> productPrices = Collections.singletonMap(productId, productPrice);
        Map<Long, Long> discount = calculateOrderCouponDiscounts(productPrice, productPrices, orderCoupons);
        return discount.get(productId);
    }


    // 주문 전체 쿠폰 할인 계산 및 상품별 분배
    public Map<Long, Long> calculateOrderCouponDiscounts(Long totalOrderPrice, Map<Long, Long> productPrices, List<PaymentCouponDTO> orderCoupons) {

        Map<Long, Long> orderCouponDiscounts = new HashMap<>();
        Set<Long> productIds = productPrices.keySet();
        BigDecimal totalOrderPriceBD = BigDecimal.valueOf(totalOrderPrice);

        if (orderCoupons == null || orderCoupons.isEmpty()) {
            productIds.forEach(id -> orderCouponDiscounts.put(id, 0L));
            return orderCouponDiscounts;
        }

        for (PaymentCouponDTO coupon : orderCoupons) {
            BigDecimal allocatedDiscountAmount = BigDecimal.ZERO;
            BigDecimal couponBenefitValue = BigDecimal.valueOf(coupon.getBenefitValue());
            Long lastProductId = null;

            if (coupon.getBenefitType() == BenefitType.AMOUNT) {
                // 정액 할인 쿠폰 처리
                for (Long productId : productIds) {
                    Long productPriceLong = productPrices.get(productId);
                    BigDecimal productPrice = BigDecimal.valueOf(productPriceLong);

                    // 할인 금액 계산 (곱셈 후 나눗셈)
                    BigDecimal productDiscount = couponBenefitValue.multiply(productPrice)
                            .divide(totalOrderPriceBD, 0, RoundingMode.HALF_UP);

                    // 할인 금액 누적
                    orderCouponDiscounts.merge(productId, productDiscount.longValue(), Long::sum);

                    allocatedDiscountAmount = allocatedDiscountAmount.add(productDiscount);
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
                    BigDecimal productDiscountInteger = discount.divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

                    // 할인 금액 누적
                    orderCouponDiscounts.merge(productId, productDiscountInteger.longValue(), Long::sum);

                    allocatedDiscountAmount = allocatedDiscountAmount.add(productDiscountInteger);
                    lastProductId = productId;
                }

                // 총 할인 금액 계산
                BigDecimal totalExpectedDiscount = totalOrderPriceBD.multiply(couponBenefitValue)
                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

                // 할인 금액 조정 (소수점 처리로 인한 오차 보정)
                if (totalExpectedDiscount.compareTo(allocatedDiscountAmount) != 0) {
                    BigDecimal difference = totalExpectedDiscount.subtract(allocatedDiscountAmount);
                    orderCouponDiscounts.merge(lastProductId, difference.longValue(), Long::sum);
                }
            }
        }

        return orderCouponDiscounts;
    }

    public Long calculatePointDiscount(Long productId, Long totalPrice, Long usedPointAmount) {
        if (usedPointAmount == 0L) return 0L;

        Map<Long, Long> productPrices = Collections.singletonMap(productId, totalPrice);
        Map<Long, Long> discount = calculatePointDiscounts(totalPrice, productPrices, usedPointAmount);
        return discount.get(productId);
    }

    // 포인트 할인 계산 및 상품별 분배
    public Map<Long, Long> calculatePointDiscounts(Long totalPrice, Map<Long, Long> productPrices, Long usedPointAmount) {

        Map<Long, Long> pointDiscounts = new HashMap<>();
        Set<Long> productIds = productPrices.keySet();
        BigDecimal totalOrderPriceBD = BigDecimal.valueOf(totalPrice);

        if (usedPointAmount == 0L) {
            productIds.forEach(id -> pointDiscounts.put(id, 0L));
            return pointDiscounts;
        }

        Long lastProductId = null;
        BigDecimal allocatedDiscountAmount = BigDecimal.ZERO;
        BigDecimal usedPointAmountBD = BigDecimal.valueOf(usedPointAmount);
        for (Long productId : productIds) {
            Long productPriceLong = productPrices.get(productId);
            BigDecimal productPrice = BigDecimal.valueOf(productPriceLong);

            BigDecimal productDiscount = usedPointAmountBD.multiply(productPrice)
                    .divide(totalOrderPriceBD, 0, RoundingMode.HALF_UP);

            // 할인 금액 저장
            pointDiscounts.put(productId, productDiscount.longValue());

            // 적용된 할인 금액 누적
            allocatedDiscountAmount = allocatedDiscountAmount.add(productDiscount);

            lastProductId = productId;
        }

        // 할인 금액 조정 (소수점 처리로 인한 오차 보정)
        if (usedPointAmountBD.compareTo(allocatedDiscountAmount) != 0) {
            BigDecimal difference = usedPointAmountBD.subtract(allocatedDiscountAmount);
            pointDiscounts.merge(lastProductId, difference.longValue(), Long::sum);
        }

        return pointDiscounts;
    }
    public DiscountInfoDTO reconcileDiscountAmount(ProductPricingDTO productPricingDTO, Long productCouponDiscount, Long orderCouponDiscount, Long pointDiscount) {
        Map<Long, ProductPricingDTO> productPricingInfo = Collections.singletonMap(productPricingDTO.getProductId(), productPricingDTO);
        Map<Long, Long> productCouponDiscounts = Collections.singletonMap(productPricingDTO.getProductId(), productCouponDiscount);
        Map<Long, Long> orderCouponDiscounts = Collections.singletonMap(productPricingDTO.getProductId(), orderCouponDiscount);
        Map<Long, Long> pointDiscounts = Collections.singletonMap(productPricingDTO.getProductId(), pointDiscount);

        Map<Long, DiscountInfoDTO> discountInfo = reconcileDiscountAmounts(productPricingInfo, productCouponDiscounts, orderCouponDiscounts, pointDiscounts);
        return discountInfo.get(productPricingDTO.getProductId());
    }

    // 할인 금액 조정 및 최종 할인 정보 생성
    public Map<Long, DiscountInfoDTO> reconcileDiscountAmounts(
            Map<Long, ProductPricingDTO> productPricingInfo,
            Map<Long, Long> productCouponDiscounts,
            Map<Long, Long> orderCouponDiscounts,
            Map<Long, Long> pointDiscounts) {

        Set<Long> productIds = productPricingInfo.keySet();

        Map<Long, DiscountInfoDTO> finalDiscountInfo = new HashMap<>();
        Map<Long, DiscountInfoDTO> eligibleDiscounts = new HashMap<>();
        long remainingPointAmount = 0L;

        // 첫 번째 반복문: 할인 정보 생성 및 초기 조정
        for (Long productId : productIds) {
            ProductPricingDTO productPricing = productPricingInfo.get(productId);
            Long productCouponDiscount = productCouponDiscounts.getOrDefault(productId, 0L);
            Long orderCouponDiscount = orderCouponDiscounts.getOrDefault(productId, 0L);
            Long pointDiscount = pointDiscounts.getOrDefault(productId, 0L);

            DiscountInfoDTO discountInfo = DiscountInfoDTO.builder()
                    .productId(productId)
                    .appSalesPrice(productPricing.getAppSalesPrice())
                    .ecoDiscountAmount(productPricing.getEcoDiscountAmount())
                    .productCouponDiscountAmount(productCouponDiscount)
                    .orderCouponDiscountAmount(orderCouponDiscount)
                    .pointDiscountAmount(pointDiscount)
                    .quantity(productPricing.getQuantity())
                    .build();

            if (discountInfo.isNegativeAmount()) {
                // 포인트 할인 조정 및 남은 포인트 금액 누적
                long unallocatedPoint = discountInfo.reconcilePointDiscountAmount();
                remainingPointAmount += unallocatedPoint;
            } else {
                eligibleDiscounts.put(productId, discountInfo);
            }
            finalDiscountInfo.put(productId, discountInfo);
        }

        if (eligibleDiscounts.isEmpty() && remainingPointAmount > 0) {
            throw new CustomException(PaymentErrorType.INVALID_USE_POINT);
        }

        // 포인트 할인 재분배 조정 작업
        while (!eligibleDiscounts.isEmpty() && remainingPointAmount > 0) {
            Set<Long> currentEligibleProductIds = new HashSet<>(eligibleDiscounts.keySet());

            BigDecimal remainingPointBD = BigDecimal.valueOf(remainingPointAmount);
            BigDecimal individualPoint = remainingPointBD.divide(BigDecimal.valueOf(eligibleDiscounts.size()), 0, RoundingMode.HALF_UP);
            BigDecimal totalAllocatedPoint = BigDecimal.ZERO;
            BigDecimal unallocatedPointAmount = BigDecimal.ZERO;
            Long lastEligibleProductId = null;

            List<Long> productsToRemove = new ArrayList<>();

            for (Long productId : currentEligibleProductIds) {
                DiscountInfoDTO discountInfoDTO = eligibleDiscounts.get(productId);

                discountInfoDTO.addPointDiscountAmount(individualPoint.longValue());
                totalAllocatedPoint = totalAllocatedPoint.add(individualPoint);

                if (discountInfoDTO.isNegativeAmount()) {
                    long unallocatedPoint = discountInfoDTO.reconcilePointDiscountAmount();
                    unallocatedPointAmount = unallocatedPointAmount.add(BigDecimal.valueOf(unallocatedPoint));
                    productsToRemove.add(productId);
                } else {
                    lastEligibleProductId = productId;
                }
            }

            // 제거할 상품 ID 처리
            for (Long productId : productsToRemove) {
                eligibleDiscounts.remove(productId);
            }

            // 조정 작업
            if (remainingPointBD.compareTo(totalAllocatedPoint) != 0) {
                if (lastEligibleProductId == null) {
                    throw new CustomException(PaymentErrorType.INVALID_USE_POINT);
                }
                BigDecimal difference = remainingPointBD.subtract(totalAllocatedPoint);
                DiscountInfoDTO discountInfoDTO = eligibleDiscounts.get(lastEligibleProductId);
                discountInfoDTO.addPointDiscountAmount(difference.longValue());

                if (discountInfoDTO.isNegativeAmount()) {
                    long unallocatedPoint = discountInfoDTO.reconcilePointDiscountAmount();
                    unallocatedPointAmount = unallocatedPointAmount.add(BigDecimal.valueOf(unallocatedPoint));
                    eligibleDiscounts.remove(lastEligibleProductId);
                    lastEligibleProductId = null; // 상품이 제거되었음을 표시
                }
            }

            // 남은 포인트 금액 갱신
            remainingPointAmount = unallocatedPointAmount.longValue();
        }

        if (remainingPointAmount > 0) {
            throw new CustomException(PaymentErrorType.INVALID_USE_POINT);
        }

        return finalDiscountInfo;
    }
}
