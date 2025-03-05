package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("쿠폰 발행 셀렉터 테스트")
class CouponPrioritySelectorTest {

    @InjectMocks
    private CouponPrioritySelector couponPrioritySelector;

    @Test
    @DisplayName("혜택 구분이 다를 경우 AMOUNT 타입이 우선시 되다.")
    void testAmountTypeHasHigherPriority() {
        // given
        Coupon amountCoupon = Coupon.builder().benefitType(BenefitType.AMOUNT).benefitValue(10000L).build();

        Coupon percentCoupon = Coupon.builder().benefitType(BenefitType.PERCENTAGE).benefitValue(10L).build();

        CouponConditionCheckResultDTO amount = CouponConditionCheckResultDTO.pass(amountCoupon);

        CouponConditionCheckResultDTO percent = CouponConditionCheckResultDTO.pass(percentCoupon);

        List<CouponConditionCheckResultDTO> items = List.of(percent, amount);

        // when
        CouponConditionCheckResultDTO result = couponPrioritySelector.selectHighestPriorityCoupon(items);

        // then
        assertThat(result.getCoupon()).isEqualTo(amountCoupon);
    }


    @Test
    @DisplayName("혜택 구분이 동일할 경우 benefitValue 값이 높은 쿠폰이 우선시되다.")
    void testHigherBenefitValueHasPriority() {
        // given
        Coupon lowerAmountCoupon = Coupon.builder().benefitType(BenefitType.AMOUNT).benefitValue(10000L).build();

        Coupon higherAmountCoupon = Coupon.builder().benefitType(BenefitType.AMOUNT).benefitValue(20000L).build();

        CouponConditionCheckResultDTO lower = CouponConditionCheckResultDTO.pass(lowerAmountCoupon);

        CouponConditionCheckResultDTO higher = CouponConditionCheckResultDTO.pass(higherAmountCoupon);

        List<CouponConditionCheckResultDTO> items = List.of(lower, higher);

        // when
        CouponConditionCheckResultDTO result = couponPrioritySelector.selectHighestPriorityCoupon(items);

        // then
        assertThat(result.getCoupon()).isEqualTo(higherAmountCoupon);
    }

    @Test
    @DisplayName("모든 종류의 쿠폰이 있을 경우 우선순위에 맞게 순서대로 작동한다.")
    void testPriorityOrderWithAllCouponTypes() {
        // given
        Coupon lowerAmountCoupon = Coupon.builder().benefitType(BenefitType.AMOUNT).benefitValue(10000L).build();

        Coupon higherAmountCoupon = Coupon.builder().benefitType(BenefitType.AMOUNT).benefitValue(20000L).build();

        Coupon lowerPercentCoupon = Coupon.builder().benefitType(BenefitType.PERCENTAGE).benefitValue(10L).build();

        Coupon higherPercentCoupon = Coupon.builder().benefitType(BenefitType.PERCENTAGE).benefitValue(20L).build();

        CouponConditionCheckResultDTO lowerAmountDTO = CouponConditionCheckResultDTO.pass(lowerAmountCoupon);

        CouponConditionCheckResultDTO higherAmountDTO = CouponConditionCheckResultDTO.pass(higherAmountCoupon);

        CouponConditionCheckResultDTO lowerPercentDTO = CouponConditionCheckResultDTO.pass(lowerPercentCoupon);

        CouponConditionCheckResultDTO higherPercentDTO = CouponConditionCheckResultDTO.pass(higherPercentCoupon);

        List<CouponConditionCheckResultDTO> items = List.of(lowerAmountDTO, lowerPercentDTO, higherPercentDTO, higherAmountDTO);

        // when
        CouponConditionCheckResultDTO firstPriorityResult = couponPrioritySelector.selectHighestPriorityCoupon(items);
        items = items.stream().filter(item -> !item.equals(firstPriorityResult)).toList();
        CouponConditionCheckResultDTO secondPriorityResult = couponPrioritySelector.selectHighestPriorityCoupon(items);
        items = items.stream().filter(item -> !item.equals(secondPriorityResult)).toList();
        CouponConditionCheckResultDTO thirdPriorityResult = couponPrioritySelector.selectHighestPriorityCoupon(items);
        items = items.stream().filter(item -> !item.equals(thirdPriorityResult)).toList();
        CouponConditionCheckResultDTO lastPriorityResult = couponPrioritySelector.selectHighestPriorityCoupon(items);

        // then
        assertThat(firstPriorityResult.getCoupon()).isEqualTo(higherAmountCoupon);
        assertThat(secondPriorityResult.getCoupon()).isEqualTo(lowerAmountCoupon);
        assertThat(thirdPriorityResult.getCoupon()).isEqualTo(higherPercentCoupon);
        assertThat(lastPriorityResult.getCoupon()).isEqualTo(lowerPercentCoupon);
    }
}