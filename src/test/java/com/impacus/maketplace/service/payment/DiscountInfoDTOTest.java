package com.impacus.maketplace.service.payment;


import com.impacus.maketplace.dto.payment.model.DiscountInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Discount Info 금액 계산 테스트")
public class DiscountInfoDTOTest {

    @Test
    @DisplayName("1개의 수량의 상품 할인 계산 적용 확인")
    void calculateOneProductAmount() {
        // given
        DiscountInfoDTO discountInfo = DiscountInfoDTO.builder()
                .productId(1L)
                .appSalesPrice(1000L)
                .ecoDiscountAmount(200L)
                .productCouponDiscountAmount(100L)
                .orderCouponDiscountAmount(100L)
                .pointDiscountAmount(100L)
                .quantity(1L)
                .build();

        // when
        boolean isNegativeAmount = discountInfo.isNegativeAmount();
        Long notDiscountedAmount = discountInfo.getNotDiscountedAmount();
        Long ecoDiscountAmount = discountInfo.getEcoDiscountAmount();
        Long ecoDiscountedAmount = discountInfo.getEcoDiscountedAmount();
        Long pointDiscountAmount = discountInfo.getPointDiscountAmount();
        Long finalCouponDiscount = discountInfo.getFinalCouponDiscount();
        Long finalAmount = discountInfo.getFinalAmount();
        Long reconciledPoint = discountInfo.reconcilePointDiscountAmount();

        // then
        assertThat(isNegativeAmount).isFalse();
        assertThat(notDiscountedAmount).isEqualTo(1000L);
        assertThat(ecoDiscountAmount).isEqualTo(200L);
        assertThat(ecoDiscountedAmount).isEqualTo(800L);
        assertThat(pointDiscountAmount).isEqualTo(100L);
        assertThat(finalCouponDiscount).isEqualTo(200L);
        assertThat(finalAmount).isEqualTo(500L);
        assertThat(reconciledPoint).isEqualTo(0);

        // when
        discountInfo.addPointDiscountAmount(600L);
        boolean isNegativeAmountAfterAddPoint = discountInfo.isNegativeAmount();
        Long reconciledPointAfterAddPoint = discountInfo.reconcilePointDiscountAmount();
        Long pointDiscountAmountAfterAddPoint = discountInfo.getPointDiscountAmount();
        Long finalAmountAfterAddPoint = discountInfo.getFinalAmount();

        // then
        assertThat(isNegativeAmountAfterAddPoint).isTrue();
        assertThat(reconciledPointAfterAddPoint).isEqualTo(100L);
        assertThat(pointDiscountAmountAfterAddPoint).isEqualTo(600L);
        assertThat(finalAmountAfterAddPoint).isEqualTo(0L);
    }

    @Test
    @DisplayName("2개 이상의 수량의 상품 할인 계산 적용 확인")
    void calculateMultipleProductAmount() {
        // given
        DiscountInfoDTO discountInfo = DiscountInfoDTO.builder()
                .productId(1L)
                .appSalesPrice(1000L)
                .ecoDiscountAmount(200L)
                .productCouponDiscountAmount(100L)
                .orderCouponDiscountAmount(100L)
                .pointDiscountAmount(100L)
                .quantity(2L)
                .build();


        // when
        boolean isNegativeAmount = discountInfo.isNegativeAmount();
        Long notDiscountedAmount = discountInfo.getNotDiscountedAmount();
        Long ecoDiscountAmount = discountInfo.getEcoDiscountAmount();
        Long ecoDiscountedAmount = discountInfo.getEcoDiscountedAmount();
        Long pointDiscountAmount = discountInfo.getPointDiscountAmount();
        Long finalCouponDiscount = discountInfo.getFinalCouponDiscount();
        Long finalAmount = discountInfo.getFinalAmount();
        Long reconciledPoint = discountInfo.reconcilePointDiscountAmount();

        // then
        assertThat(isNegativeAmount).isFalse();
        assertThat(notDiscountedAmount).isEqualTo(2000L);
        assertThat(ecoDiscountAmount).isEqualTo(400L);
        assertThat(ecoDiscountedAmount).isEqualTo(1600L);
        assertThat(pointDiscountAmount).isEqualTo(100L);
        assertThat(finalCouponDiscount).isEqualTo(200L);
        assertThat(finalAmount).isEqualTo(1300L);
        assertThat(reconciledPoint).isEqualTo(0);

        // when
        discountInfo.addPointDiscountAmount(1500L);
        boolean isNegativeAmountAfterAddPoint = discountInfo.isNegativeAmount();
        Long reconciledPointAfterAddPoint = discountInfo.reconcilePointDiscountAmount();
        Long pointDiscountAmountAfterAddPoint = discountInfo.getPointDiscountAmount();
        Long finalAmountAfterAddPoint = discountInfo.getFinalAmount();

        // then
        assertThat(isNegativeAmountAfterAddPoint).isTrue();
        assertThat(reconciledPointAfterAddPoint).isEqualTo(200L);
        assertThat(pointDiscountAmountAfterAddPoint).isEqualTo(1400L);
        assertThat(finalAmountAfterAddPoint).isEqualTo(0L);
    }
}
