package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.enumType.payment.PaymentType;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Event 금액 테스트")
public class PaymentEventTest {

    private final String PAYMENT_KEY = "paymentKey";
    private final String ORDER_ID = "orderId";
    private final String ORDER_NAME = "orderName";

    @Test
    @DisplayName("1개의 상품에 할인이 없는 상황에서 금액이 올바르게 계산되다.")
    void calcualteAmountNoDiscount_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 0L, 0L, 0L, 0);

        paymentEvent.getPaymentOrders().add(paymentOrder);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(0L);
        assertThat(totalGreenLabelDiscount).isEqualTo(0L);
        assertThat(totalEcoDiscount).isEqualTo(0L);
        assertThat(totalDiscount).isEqualTo(0L);
        assertThat(totalAmount).isEqualTo(1000L);
        assertThat(totalDiscountedAmount).isEqualTo(1000L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(1000L);
    }

    @Test
    @DisplayName("여러개의 상품에 할인이 없는 상황에서 금액이 올바르게 계산되다.")
    void calcualteAmountNoDiscountMultiple_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder1 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 0L, 0L, 0L, 0);
        PaymentOrder paymentOrder2 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 2000L, 0L, 0L, 0L, 0);
        PaymentOrder paymentOrder3 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 3000L, 0L, 0L, 0L, 0);


        paymentEvent.getPaymentOrders().add(paymentOrder1);
        paymentEvent.getPaymentOrders().add(paymentOrder2);
        paymentEvent.getPaymentOrders().add(paymentOrder3);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(0L);
        assertThat(totalGreenLabelDiscount).isEqualTo(0L);
        assertThat(totalEcoDiscount).isEqualTo(0L);
        assertThat(totalDiscount).isEqualTo(0L);
        assertThat(totalAmount).isEqualTo(6000L);
        assertThat(totalDiscountedAmount).isEqualTo(6000L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(6000L);
    }

    @Test
    @DisplayName("1개의 상품에 에코 할인이 적용된 상황에서 금액이 올바르게 계산되다.")
    void calculateAmountEcoDiscount_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 100L, 0L, 0L, 0);

        paymentEvent.getPaymentOrders().add(paymentOrder);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(0L);
        assertThat(totalGreenLabelDiscount).isEqualTo(0L);
        assertThat(totalEcoDiscount).isEqualTo(100L);
        assertThat(totalDiscount).isEqualTo(100L);
        assertThat(totalAmount).isEqualTo(1000L);
        assertThat(totalDiscountedAmount).isEqualTo(900L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(900L);
    }

    @Test
    @DisplayName("여러개의 상품에 에코 할인이 적용된 상황에서 금액이 올바르게 계산되다.")
    void calculateAmountEcoDiscountMultiple_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder1 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 100L, 0L, 0L, 0);
        PaymentOrder paymentOrder2 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 2000L, 200L, 0L, 0L, 0);
        PaymentOrder paymentOrder3 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 3000L, 300L, 0L, 0L, 0);


        paymentEvent.getPaymentOrders().add(paymentOrder1);
        paymentEvent.getPaymentOrders().add(paymentOrder2);
        paymentEvent.getPaymentOrders().add(paymentOrder3);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(0L);
        assertThat(totalGreenLabelDiscount).isEqualTo(0L);
        assertThat(totalEcoDiscount).isEqualTo(600L);
        assertThat(totalDiscount).isEqualTo(600L);
        assertThat(totalAmount).isEqualTo(6000L);
        assertThat(totalDiscountedAmount).isEqualTo(5400L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(5400L);
    }

    @Test
    @DisplayName("1개의 상품에 그린 라벨 포인트 할인이 적용된 상황에서 금액이 올바르게 계산되다.")
    void calculateAmountGreenLabelPointDiscount_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 0L, 100L, 0L, 0);

        paymentEvent.getPaymentOrders().add(paymentOrder);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(0L);
        assertThat(totalGreenLabelDiscount).isEqualTo(100L);
        assertThat(totalEcoDiscount).isEqualTo(0L);
        assertThat(totalDiscount).isEqualTo(100L);
        assertThat(totalAmount).isEqualTo(1000L);
        assertThat(totalDiscountedAmount).isEqualTo(900L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(900L);
    }

    @Test
    @DisplayName("여러 개의 상품에 그린 라벨 포인트 할인이 적용된 상황에서 금액이 올바르게 계산되다.")
    void calculateAmountGreenLabelPointDiscountMultiple_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder1 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 0L, 100L, 0L, 0);
        PaymentOrder paymentOrder2 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 2000L, 0L, 200L, 0L, 0);
        PaymentOrder paymentOrder3 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 3000L, 0L, 300L, 0L, 0);


        paymentEvent.getPaymentOrders().add(paymentOrder1);
        paymentEvent.getPaymentOrders().add(paymentOrder2);
        paymentEvent.getPaymentOrders().add(paymentOrder3);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(0L);
        assertThat(totalGreenLabelDiscount).isEqualTo(600L);
        assertThat(totalEcoDiscount).isEqualTo(0L);
        assertThat(totalDiscount).isEqualTo(600L);
        assertThat(totalAmount).isEqualTo(6000L);
        assertThat(totalDiscountedAmount).isEqualTo(5400L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(5400L);
    }

    @Test
    @DisplayName("1개의 상품에 쿠폰 할인이 적용된 상황에서 금액이 올바르게 계산되다.")
    void calculateAmountCouponDiscount_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 0L, 0L, 100L, 0);

        paymentEvent.getPaymentOrders().add(paymentOrder);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(100L);
        assertThat(totalGreenLabelDiscount).isEqualTo(0L);
        assertThat(totalEcoDiscount).isEqualTo(0L);
        assertThat(totalDiscount).isEqualTo(100L);
        assertThat(totalAmount).isEqualTo(1000L);
        assertThat(totalDiscountedAmount).isEqualTo(900L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(900L);
    }

    @Test
    @DisplayName("여러 개의 상품에 쿠폰 할인이 적용된 상황에서 금액이 올바르게 계산되다.")
    void calculateAmountCouponDiscountMultiple_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder1 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 0L, 0L, 100L, 0);
        PaymentOrder paymentOrder2 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 2000L, 0L, 0L, 200L, 0);
        PaymentOrder paymentOrder3 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 3000L, 0L, 0L, 300L, 0);


        paymentEvent.getPaymentOrders().add(paymentOrder1);
        paymentEvent.getPaymentOrders().add(paymentOrder2);
        paymentEvent.getPaymentOrders().add(paymentOrder3);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(600L);
        assertThat(totalGreenLabelDiscount).isEqualTo(0L);
        assertThat(totalEcoDiscount).isEqualTo(0L);
        assertThat(totalDiscount).isEqualTo(600L);
        assertThat(totalAmount).isEqualTo(6000L);
        assertThat(totalDiscountedAmount).isEqualTo(5400L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(5400L);
    }

    @Test
    @DisplayName("1개의 상품에 여러 할인이 적용된 상황에서 금액이 올바르게 계산되다.")
    void calculateAmountDiscount_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 90L, 49L, 100L, 0);

        paymentEvent.getPaymentOrders().add(paymentOrder);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(100L);
        assertThat(totalGreenLabelDiscount).isEqualTo(49L);
        assertThat(totalEcoDiscount).isEqualTo(90L);
        assertThat(totalDiscount).isEqualTo(239L);
        assertThat(totalAmount).isEqualTo(1000L);
        assertThat(totalDiscountedAmount).isEqualTo(761L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(761L);
    }

    @Test
    @DisplayName("여러 개의 상품에 여러 할인이 적용된 상황에서 금액이 올바르게 계산되다.")
    void calculateAmountDiscountMultiple_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder1 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 90L, 49L, 100L, 0);
        PaymentOrder paymentOrder2 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 2000L, 100L, 101L, 200L, 0);
        PaymentOrder paymentOrder3 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 3000L, 110L, 200L, 500L, 0);


        paymentEvent.getPaymentOrders().add(paymentOrder1);
        paymentEvent.getPaymentOrders().add(paymentOrder2);
        paymentEvent.getPaymentOrders().add(paymentOrder3);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(0L);
        assertThat(totalCouponDiscount).isEqualTo(800L);        // 100 + 200 + 500
        assertThat(totalGreenLabelDiscount).isEqualTo(350L);    // 49 + 101 + 200
        assertThat(totalEcoDiscount).isEqualTo(300L);           // 90 + 100 + 110
        assertThat(totalDiscount).isEqualTo(1450L);             // 800 + 350 + 300
        assertThat(totalAmount).isEqualTo(6000L);               // 6000 - 1450
        assertThat(totalDiscountedAmount).isEqualTo(4550L);     // 6000 - 1450
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(4550L);
    }

    @Test
    @DisplayName("1개의 상품에 수수료비가 올바르게 계산되다.")
    void calculateAmountCommissionFee_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 90L, 49L, 100L, 10);

        paymentEvent.getPaymentOrders().add(paymentOrder);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        assertThat(totalCommissionFee).isEqualTo(76L);       // 761 * (10 / 100) => 76 (버림 처리)
        assertThat(totalCouponDiscount).isEqualTo(100L);
        assertThat(totalGreenLabelDiscount).isEqualTo(49L);
        assertThat(totalEcoDiscount).isEqualTo(90L);
        assertThat(totalDiscount).isEqualTo(239L);
        assertThat(totalAmount).isEqualTo(1000L);
        assertThat(totalDiscountedAmount).isEqualTo(761L);
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(685L);     // 761 - 76
    }

    @Test
    @DisplayName("여러 개의 상품에 수수료비가 올바르게 계산되다.")
    void calculateAmountCommissionFeeMultiple_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        PaymentOrder paymentOrder1 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 1000L, 90L, 49L, 100L, 10);
        PaymentOrder paymentOrder2 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 2000L, 100L, 101L, 200L, 10);
        PaymentOrder paymentOrder3 = getPaymentOrder(paymentEvent.getId(), 1L, 1L, 3000L, 110L, 200L, 500L, 5);


        paymentEvent.getPaymentOrders().add(paymentOrder1);
        paymentEvent.getPaymentOrders().add(paymentOrder2);
        paymentEvent.getPaymentOrders().add(paymentOrder3);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        // 1. 첫번째 상품 수수료비: (1000 - 90 - 49 - 100) * (10 / 100) = 761 / 10 => 76
        // 2. 두번째 상품 수수료비: (2000 - 100 - 101 - 200) * (10 / 100) = 1599 / 10 => 159
        // 3. 세번째 상품 수수료비: (3000 - 110 - 200 - 500) * (5 / 100) = 2190 / 20 => 109
        assertThat(totalCommissionFee).isEqualTo(344L);           // 76 + 159 + 109
        assertThat(totalCouponDiscount).isEqualTo(800L);        // 100 + 200 + 500
        assertThat(totalGreenLabelDiscount).isEqualTo(350L);    // 49 + 101 + 200
        assertThat(totalEcoDiscount).isEqualTo(300L);           // 90 + 100 + 110
        assertThat(totalDiscount).isEqualTo(1450L);             // 800 + 350 + 300
        assertThat(totalAmount).isEqualTo(6000L);               // 6000 - 1450
        assertThat(totalDiscountedAmount).isEqualTo(4550L);     // 6000 - 1450
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(4206L);    // 4550 - 344
    }

    @Test
    @DisplayName("여러 개의 상품에 여러 수량을 주문한 상황에서 모든 할인 및 수수료 비율을 적용했을 경우 올바르게 계산되다.")
    void calculateAmountComplicatedSituation_success() {
        // given
        PaymentEvent paymentEvent = getPaymentEvent();

        // 상품 1: 가격 1000원, 수량 3개, 에코 할인 50원, 그린라벨 할인 30원, 쿠폰 할인 100원, 수수료 10%
        // 할인 금액은 총 할인 금액으로 설정
        PaymentOrder paymentOrder1 = getPaymentOrder(
                paymentEvent.getId(),
                1L, // otherId
                3L, // quantity
                1000L, // amount (단가)
                50L, // ecoDiscount (총 할인 금액)
                30L, // greenLabelDiscount (총 할인 금액)
                100L, // couponDiscount (총 할인 금액)
                10 // commissionPercent
        );

        // 상품 2: 가격 2000원, 수량 2개, 에코 할인 100원, 그린라벨 할인 50원, 쿠폰 할인 200원, 수수료 15%
        PaymentOrder paymentOrder2 = getPaymentOrder(
                paymentEvent.getId(),
                2L, // otherId
                2L, // quantity
                2000L, // amount (단가)
                100L, // ecoDiscount
                50L, // greenLabelDiscount
                200L, // couponDiscount
                15 // commissionPercent
        );

        // 상품 3: 가격 5000원, 수량 1개, 에코 할인 200원, 그린라벨 할인 150원, 쿠폰 할인 500원, 수수료 5%
        PaymentOrder paymentOrder3 = getPaymentOrder(
                paymentEvent.getId(),
                3L, // otherId
                1L, // quantity
                5000L, // amount (단가)
                200L, // ecoDiscount
                150L, // greenLabelDiscount
                500L, // couponDiscount
                5 // commissionPercent
        );

        paymentEvent.getPaymentOrders().add(paymentOrder1);
        paymentEvent.getPaymentOrders().add(paymentOrder2);
        paymentEvent.getPaymentOrders().add(paymentOrder3);

        // when
        Long totalCommissionFee = paymentEvent.getTotalCommissionFee();
        Long totalCouponDiscount = paymentEvent.getTotalCouponDiscount();
        Long totalGreenLabelDiscount = paymentEvent.getTotalGreenLabelDiscount();
        Long totalEcoDiscount = paymentEvent.getTotalEcoDiscount();
        Long totalDiscount = paymentEvent.getTotalDiscount();
        Long totalAmount = paymentEvent.getTotalAmount();
        Long totalDiscountedAmount = paymentEvent.getTotalDiscountedAmount();
        Long totalDiscountedAmountWithCommissionFee = paymentEvent.getTotalDiscountedAmountWithCommissionFee();

        // then
        //  상품 1:
        //  총 상품 금액: 1000원 * 3개 = 3000원

        //  총 할인 금액:
        //  에코 할인: 50원 (수량과 무관)
        //  그린라벨 할인: 30원 (수량과 무관)
        //  쿠폰 할인: 100원 (수량과 무관)
        //  총 할인 합계: 50원 + 30원 + 100원 = 180원
        //  할인 적용 금액: 3000원 - 180원 = 2820원
        //  수수료: 2820원 * 10% = 282원

        //  상품 2:
        //  총 상품 금액: 2000원 * 2개 = 4000원
        //  총 할인 금액:
        //  에코 할인: 100원
        //  그린라벨 할인: 50원
        //  쿠폰 할인: 200원
        //  총 할인 합계: 100원 + 50원 + 200원 = 350원
        //  할인 적용 금액: 4000원 - 350원 = 3650원
        //  수수료: 3650원 * 15% = 547.5원 → 버림하여 547원

        //  상품 3:
        //  총 상품 금액: 5000원 * 1개 = 5000원
        //  총 할인 금액:
        //  에코 할인: 200원
        //  그린라벨 할인: 150원
        //  쿠폰 할인: 500원
        //  총 할인 합계: 200원 + 150원 + 500원 = 850원
        //  할인 적용 금액: 5000원 - 850원 = 4150원
        //  수수료: 4150원 * 5% = 207.5원 → 버림하여 207원

        //  전체 합계:
        //  총 수수료: 282원 + 547원 + 207원 = 1036원
        //  총 할인: 180원 + 350원 + 850원 = 1380원
        //  총 상품 금액: 3000원 + 4000원 + 5000원 = 12000원
        //  총 할인 적용 금액: 12000원 - 1380원 = 10620원
        //  총 결제 금액(수수료 포함): 10620원 - 1036원 = 9584원
        assertThat(totalCommissionFee).isEqualTo(1036L);           // 282 + 547 + 207
        assertThat(totalCouponDiscount).isEqualTo(800L);          // 100 + 200 + 500
        assertThat(totalGreenLabelDiscount).isEqualTo(230L);      // 30 + 50 + 150
        assertThat(totalEcoDiscount).isEqualTo(350L);             // 50 + 100 + 200
        assertThat(totalDiscount).isEqualTo(1380L);               // 180 + 350 + 850
        assertThat(totalAmount).isEqualTo(12000L);                // 3000 + 4000 + 5000
        assertThat(totalDiscountedAmount).isEqualTo(10620L);      // 12000 - 1380
        assertThat(totalDiscountedAmountWithCommissionFee).isEqualTo(9584L); // 10620 - 1036
    }


    private PaymentOrder getPaymentOrder(Long paymentEventId, Long otherId, Long quantity, Long amount, Long ecoDiscount, Long greenLabelDiscount, Long couponDiscount, Integer commissionPercent) {
        return PaymentOrder.builder()
                .id(otherId)
                .paymentEventId(paymentEventId)
                .sellerId(otherId)
                .productId(otherId)
                .productOptionHistoryId(otherId)
                .quantity(quantity)
                .orderId(ORDER_ID)
                .amount(amount)
                .ecoDiscount(ecoDiscount)
                .greenLabelDiscount(greenLabelDiscount)
                .couponDiscount(couponDiscount)
                .commissionPercent(commissionPercent)
                .status(PaymentOrderStatus.NOT_STARTED)
                .ledgerUpdated(false)
                .walletUpdated(false)
                .isPaymentDone(false)
                .failedCount(0)
                .threshold(5)
                .build();
    }

    private PaymentEvent getPaymentEvent() {
        return PaymentEvent.builder()
                .id(1L)
                .buyerId(1L)
                .isPaymentDone(false)
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .type(PaymentType.NORMAL)
                .orderName(ORDER_NAME)
                .method(PaymentMethod.CARD)
                .pspRawData(null)
                .approvedAt(LocalDateTime.now())
                .build();
    }
}
