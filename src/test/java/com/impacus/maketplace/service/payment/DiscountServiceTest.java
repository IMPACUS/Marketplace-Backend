package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.dto.payment.PaymentCouponDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[비즈니스 로직] - 할인 금액 계산")
class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    @Test
    @DisplayName("[정상 케이스] 단일 상품에 대한 1개의 Amount 쿠폰 할인 금액이 올바르게 계산되다.")
    void testCalculateProductCouponDiscountBenefitTypeAmount_success() {
        // given
        Long productId = 1L;
        Long productTotalPrice = 1999L;

        PaymentCouponDTO paymentCouponDTO = new PaymentCouponDTO(1L, BenefitType.AMOUNT, 500L);
        List<PaymentCouponDTO> productCoupons = new ArrayList<>();
        productCoupons.add(paymentCouponDTO);

        // when
        Long discount = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

        // then
        assertThat(discount).isEqualTo(500L);
    }

    @Test
    @DisplayName("[정상 케이스] 단일 상품에 대한 1개의 Percent 쿠폰 할인 금액이 올바르게 계산되다.")
    void testCalculateProductDiscountBenefitTypePercent_success() {
        // given
        Long productId = 1L;
        Long productTotalPrice = 1999L;

        PaymentCouponDTO paymentCouponDTO = new PaymentCouponDTO(1L, BenefitType.PERCENTAGE, 7L);
        List<PaymentCouponDTO> productCoupons = new ArrayList<>();
        productCoupons.add(paymentCouponDTO);

        // when
        Long discount = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

        // then
        assertThat(discount).isEqualTo(139L); // 139.93
    }

    @Test
    @DisplayName("[정상 케이스] 단일 상품에 대한 2개 이상의 여러 쿠폰 할인 금액이 올바르게 계산되다.")
    void testCalculateProductDiscountMultiple_success() {
        // given
        Long productId = 1L;
        Long productTotalPrice = 1999L;

        PaymentCouponDTO paymentCouponDTO1 = new PaymentCouponDTO(1L, BenefitType.AMOUNT, 500L);
        PaymentCouponDTO paymentCouponDTO2 = new PaymentCouponDTO(1L, BenefitType.PERCENTAGE, 7L);
        List<PaymentCouponDTO> productCoupons = new ArrayList<>();
        productCoupons.add(paymentCouponDTO1);
        productCoupons.add(paymentCouponDTO2);

        // when
        Long discount = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

        // then
        assertThat(discount).isEqualTo(500L + 139L);
    }

    @Test
    @DisplayName("[정상 케이스] 단일 상품에 대한 2개 이상의 여러 쿠폰 할인 금액이 앱 할인액을 초과한 경우 앱 할인액과 동일하게 계산되다.")
    void testCaculateProductDiscountOverAppsales_success() {
        // given
        Long productId = 1L;
        Long productTotalPrice = 1999L;

        PaymentCouponDTO paymentCouponDTO1 = new PaymentCouponDTO(1L, BenefitType.AMOUNT, 1000L);
        PaymentCouponDTO paymentCouponDTO2 = new PaymentCouponDTO(1L, BenefitType.PERCENTAGE, 50L);

        List<PaymentCouponDTO> productCoupons = new ArrayList<>();
        productCoupons.add(paymentCouponDTO1);
        productCoupons.add(paymentCouponDTO1);
        productCoupons.add(paymentCouponDTO2);

        // when
        Long discount = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

        // then
        assertThat(discount).isEqualTo(productTotalPrice);
    }


    @Test
    @DisplayName("[정상 케이스] 여러 상품에 대하여 각각 적용한 1개의 AMOUNT 쿠폰 할인이 올바르게 계산되다.")
    void testCalculateProductDiscountsSingleCouponAmount_success() {
        // given
        Map<Long, Long> productTotalPrices = new HashMap<>();
        for (long i = 0L; i < 3; i++) {
            Long productId = i;
            Long totalPrice = 1999 * (i + 1);
            productTotalPrices.put(productId, totalPrice);
        }

        List<Long> benefitValues = new ArrayList<>();
        Map<Long, List<PaymentCouponDTO>> productCoupons = new HashMap<>();
        for (long i = 0L; i < 3; i++) {
            Long userCouponId = i;
            BenefitType benefitType = BenefitType.AMOUNT;
            Long benefitValue = 1000 * i;
            List<PaymentCouponDTO> list = new ArrayList<>();
            list.add(new PaymentCouponDTO(userCouponId, benefitType, benefitValue));
            productCoupons.put(i, list);
            benefitValues.add(benefitValue);
        }

        // when
        Map<Long, Long> discounts = discountService.calculateProductCouponDiscounts(productTotalPrices, productCoupons);

        // then
        for (long i = 0L; i < 3; i++) {
            int idx = Integer.parseInt(String.valueOf(i));
            assertThat(discounts.get(i)).isEqualTo(benefitValues.get(idx));
        }
    }

    @Test
    @DisplayName("[정상 케이스] 여러 상품에 대하여 각각 적용한 1개의 PERCENT 쿠폰 할인이 올바르게 계산되다.")
    void testCalculateProductDiscountsSingleCouponPercent_success() {
        // given
        Map<Long, Long> productTotalPrices = new HashMap<>();
        for (long i = 0L; i < 3; i++) {
            Long productId = i;
            Long totalPrice = 1999 * (i + 1);
            productTotalPrices.put(productId, totalPrice);
        }

        Map<Long, List<PaymentCouponDTO>> productCoupons = new HashMap<>();
        for (long i = 0L; i < 3; i++) {
            Long userCouponId = i;
            BenefitType benefitType = BenefitType.PERCENTAGE;
            Long benefitValue = 10L * (i + 1);
            List<PaymentCouponDTO> list = new ArrayList<>();
            list.add(new PaymentCouponDTO(userCouponId, benefitType, benefitValue));
            productCoupons.put(i, list);
        }

        // when
        Map<Long, Long> discounts = discountService.calculateProductCouponDiscounts(productTotalPrices, productCoupons);

        // then
        for (long i = 0L; i < 3; i++) {
            BigDecimal totalPrice = BigDecimal.valueOf(1999 * (i + 1));
            BigDecimal benefitValue = BigDecimal.valueOf(10 * (i + 1));
            assertThat(discounts.get(i)).isEqualTo(totalPrice.multiply(benefitValue).divide(BigDecimal.valueOf(100L), 0, RoundingMode.FLOOR).longValue());
        }
    }

    @Test
    @DisplayName("[정상 케이스] 여러 상품에 대하여 각각 적용한 2개의 AMOUNT, PERCENT 쿠폰 할인이 올바르게 계산되다.")
    void testCalculateProductDiscountsMultipleCoupon_success() {
        // given
        Map<Long, Long> productTotalPrices = new HashMap<>();
        for (long i = 0L; i < 3; i++) {
            Long productId = i;
            Long totalPrice = 1999 * (i + 1);
            productTotalPrices.put(productId, totalPrice);
        }

        Map<Long, List<PaymentCouponDTO>> productCoupons = new HashMap<>();
        for (long i = 0L; i < 6; i += 2) {
            List<PaymentCouponDTO> list = new ArrayList<>();
            list.add(new PaymentCouponDTO(i, BenefitType.AMOUNT, 500L));
            list.add(new PaymentCouponDTO(i + 1, BenefitType.PERCENTAGE, 10L));
            productCoupons.put(i / 2, list);
        }

        // when
        Map<Long, Long> discounts = discountService.calculateProductCouponDiscounts(productTotalPrices, productCoupons);


        // then
        for (long i = 0L; i < 3; i++) {
            BigDecimal totalPrice = BigDecimal.valueOf(1999 * (i + 1));
            BigDecimal amountDiscountPrice = BigDecimal.valueOf(500L);
            BigDecimal percentDiscountPrice = BigDecimal.valueOf(10L);
            assertThat(discounts.get(i)).isEqualTo(amountDiscountPrice.add(
                    totalPrice.multiply(percentDiscountPrice).divide(BigDecimal.valueOf(100L), 0, RoundingMode.FLOOR)).longValue());
        }
    }
}