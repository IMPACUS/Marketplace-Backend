package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.dto.payment.PaymentCouponDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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


    // 여러 상품에 대한 테스트 케이스 작성
}