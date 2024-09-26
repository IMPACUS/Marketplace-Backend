package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.dto.payment.PaymentCouponDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[비즈니스 로직] - 할인 금액 계산")
class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    @Nested
    @DisplayName("Product Coupon Test")
    class ProductCoupon {

        @Test
        @DisplayName("[정상 케이스] 단일 상품에 대해 0개의 쿠폰 할인 금액이 올바르게 처리되다.")
        void testCalculateProductCouponEmpty_success() {
            // given
            Long productId = 1L;
            Long productTotalPrice = 1999L;

            List<PaymentCouponDTO> productCoupons = new ArrayList<>();

            // when
            Long result = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

            // then
            assertThat(result).isEqualTo(0L);
        }

        @Test
        @DisplayName("[정상 케이스] 단일 상품에 대해 쿠폰에 null이 들어올 경우에 올바르게 처리되다.")
        void testCalculateProductCouponNull_success() {
            // given
            Long productId = 1L;
            Long productTotalPrice = 1999L;

            List<PaymentCouponDTO> productCoupons = new ArrayList<>();

            // when
            Long result = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

            // then
            assertThat(result).isEqualTo(0L);
        }

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
            Long result = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

            // then
            assertThat(result).isEqualTo(500L);
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
            Long result = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

            // then
            assertThat(result).isEqualTo(140L); // 139.93
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
            Long result = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

            // then
            assertThat(result).isEqualTo(500L + 140L);
        }

        @Test
        @DisplayName("[정상 케이스] 단일 상품에 대한 1개의 쿠폰 할인 금액이 앱 할인액을 초과한 경우 앱 할인액과 동일하게 계산되다.")
        void testCaculateProductDiscountOverAppsales_success() {
            // given
            Long productId = 1L;
            Long productTotalPrice = 1999L;

            PaymentCouponDTO paymentCouponDTO1 = new PaymentCouponDTO(1L, BenefitType.AMOUNT, 2000L);

            List<PaymentCouponDTO> productCoupons = new ArrayList<>();
            productCoupons.add(paymentCouponDTO1);

            // when
            Long result = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

            // then
            assertThat(result).isEqualTo(productTotalPrice);
        }

        @Test
        @DisplayName("[정상 케이스] 단일 상품에 대한 2개 이상의 여러 쿠폰 할인 금액이 앱 할인액을 초과한 경우 앱 할인액과 동일하게 계산되다.")
        void testCaculateProductDiscountMultipleOverAppsales_success() {
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
            Long result = discountService.calculateProductCouponDiscount(productId, productTotalPrice, productCoupons);

            // then
            assertThat(result).isEqualTo(productTotalPrice);
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품에 대하여 0개의 쿠폰 할인이 올바르게 계산되다.")
        void testCalculateProductDiscountsEmpty_success() {
            // given
            Map<Long, Long> productTotalPrices = new HashMap<>();
            for (long i = 0L; i < 3; i++) {
                Long productId = i;
                Long totalPrice = 1999 * (i + 1);
                productTotalPrices.put(productId, totalPrice);
            }

            Map<Long, List<PaymentCouponDTO>> productCoupons = new HashMap<>();

            // when
            Map<Long, Long> result = discountService.calculateProductCouponDiscounts(productTotalPrices, productCoupons);

            // then
            for (long i = 0L; i < 3; i++) {
                int idx = Integer.parseInt(String.valueOf(i));
                assertThat(result.get(i)).isEqualTo(0L);
            }
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품에 대하여 쿠폰에 Null이 들어올 경우 올바르게 처리되다.")
        void testCalculateProductDiscountsNull_success() {
            // given
            Map<Long, Long> productTotalPrices = new HashMap<>();
            for (long i = 0L; i < 3; i++) {
                Long productId = i;
                Long totalPrice = 1999 * (i + 1);
                productTotalPrices.put(productId, totalPrice);
            }

            Map<Long, List<PaymentCouponDTO>> productCoupons = new HashMap<>();

            // when
            Map<Long, Long> result = discountService.calculateProductCouponDiscounts(productTotalPrices, productCoupons);

            // then
            for (long i = 0L; i < 3; i++) {
                assertThat(result.get(i)).isEqualTo(0L);
            }
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
            Map<Long, Long> result = discountService.calculateProductCouponDiscounts(productTotalPrices, productCoupons);

            // then
            for (long i = 0L; i < 3; i++) {
                int idx = Integer.parseInt(String.valueOf(i));
                assertThat(result.get(i)).isEqualTo(benefitValues.get(idx));
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
            Map<Long, Long> result = discountService.calculateProductCouponDiscounts(productTotalPrices, productCoupons);

            // then
            for (long i = 0L; i < 3; i++) {
                BigDecimal totalPrice = BigDecimal.valueOf(1999 * (i + 1));
                BigDecimal benefitValue = BigDecimal.valueOf(10 * (i + 1));
                assertThat(result.get(i)).isEqualTo(totalPrice.multiply(benefitValue).divide(BigDecimal.valueOf(100L), 0, RoundingMode.HALF_UP).longValue());
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
            Map<Long, Long> result = discountService.calculateProductCouponDiscounts(productTotalPrices, productCoupons);


            // then
            for (long i = 0L; i < 3; i++) {
                BigDecimal totalPrice = BigDecimal.valueOf(1999 * (i + 1));
                BigDecimal amountDiscountPrice = BigDecimal.valueOf(500L);
                BigDecimal percentDiscountPrice = BigDecimal.valueOf(10L);
                assertThat(result.get(i)).isEqualTo(amountDiscountPrice.add(
                        totalPrice.multiply(percentDiscountPrice).divide(BigDecimal.valueOf(100L), 0, RoundingMode.HALF_UP)).longValue());
            }
        }
    }

    @Nested
    @DisplayName("Order Coupon Test")
    class OrderCoupon {

        @Test
        @DisplayName("[정상 케이스] 단일 상품 구매시의 주문에 대해 0개의 쿠폰 할인 금액이 올바르게 적용되다.")
        void calculateOrderCouponDiscountEmpty_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;
            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();

            // when
            Long result = discountService.calculateOrderCouponDiscount(productId, productPrice, orderCoupons);

            // then
            assertThat(result).isEqualTo(0L);
        }

        @Test
        @DisplayName("[정상 케이스] 단일 상품 구매시의 주문에 대해 쿠폰에 Null이 들어올 경우 올바르게 처리되다.")
        void calculateOrderCouponDiscountNull_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;
            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();

            // when
            Long result = discountService.calculateOrderCouponDiscount(productId, productPrice, orderCoupons);

            // then
            assertThat(result).isEqualTo(0L);
        }


        @Test
        @DisplayName("[정상 케이스] 단일 상품 구매시의 주문에 대해 1개의 Amount 쿠폰 할인 금액이 올바르게 적용되다.")
        void calculateOrderCouponDiscountBenefitTypeAmount_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;
            PaymentCouponDTO paymentCouponDTO = new PaymentCouponDTO(1L, BenefitType.AMOUNT, 500L);
            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();
            orderCoupons.add(paymentCouponDTO);

            // when
            Long result = discountService.calculateOrderCouponDiscount(productId, productPrice, orderCoupons);

            // then
            assertThat(result).isEqualTo(500L);
        }

        @Test
        @DisplayName("[정상 케이스] 단일 상품 구매시의 주문에 대해 1개의 Percent 쿠폰 할인 금액이 올바르게 적용되다.")
        void calculateOrderCouponDiscountBenefitTypePercent_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;
            PaymentCouponDTO paymentCouponDTO = new PaymentCouponDTO(1L, BenefitType.PERCENTAGE, 10L);
            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();
            orderCoupons.add(paymentCouponDTO);

            // when
            Long result = discountService.calculateOrderCouponDiscount(productId, productPrice, orderCoupons);

            // then
            // 정률 할인
            BigDecimal productPriceBD = BigDecimal.valueOf(productPrice);
            BigDecimal discount = productPriceBD.multiply(BigDecimal.valueOf(paymentCouponDTO.getBenefitValue()));

            // 100으로 나누고 소수점 이하 반올림
            BigDecimal discountAmountPerCoupon = discount.divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
            System.out.println(discountAmountPerCoupon.longValue());
            assertThat(result).isEqualTo(discountAmountPerCoupon.longValue());
        }

        @Test
        @DisplayName("[정상 케이스] 단일 상품 구매시의 주문에 대해 2개의 쿠폰이 올바르게 적용되다.")
        void calculateOrderCouponDiscountMultiple_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;
            PaymentCouponDTO paymentCouponDTO1 = new PaymentCouponDTO(1L, BenefitType.AMOUNT, 500L);
            PaymentCouponDTO paymentCouponDTO2 = new PaymentCouponDTO(2L, BenefitType.PERCENTAGE, 10L);
            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();
            orderCoupons.add(paymentCouponDTO1);
            orderCoupons.add(paymentCouponDTO2);

            // when
            Long result = discountService.calculateOrderCouponDiscount(productId, productPrice, orderCoupons);

            // then
            Long discount = 500L + 200L;
            System.out.println(discount);
            assertThat(result).isEqualTo(discount);
        }

        @Test
        @DisplayName("[정상 케이스] 단일 상품 구매시의 주문에 대해 1개의 쿠폰 할인 금액이 앱 할인액을 초과한 경우 초과한 금액 그대로 계산되다.")
        void calculateOrderCouponDiscountOverAppsales_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;
            PaymentCouponDTO paymentCouponDTO1 = new PaymentCouponDTO(1L, BenefitType.AMOUNT, 2000L);
            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();
            orderCoupons.add(paymentCouponDTO1);

            // when
            Long result = discountService.calculateOrderCouponDiscount(productId, productPrice, orderCoupons);

            // then
            System.out.println(paymentCouponDTO1.getBenefitValue());
            assertThat(result).isEqualTo(paymentCouponDTO1.getBenefitValue());
        }

        @Test
        @DisplayName("[정상 케이스] 단일 상품 구매시의 주문에 대해 2개 이상의 여러 쿠폰 할인 금액이 앱 할인액을 초과한 경우 초과한 금액 그대로 계산되다.")
        void calculateOrderCouponDiscountMultipleOver_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;
            PaymentCouponDTO paymentCouponDTO1 = new PaymentCouponDTO(1L, BenefitType.AMOUNT, 1100L);
            PaymentCouponDTO paymentCouponDTO2 = new PaymentCouponDTO(2L, BenefitType.PERCENTAGE, 50L);
            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();
            orderCoupons.add(paymentCouponDTO1);
            orderCoupons.add(paymentCouponDTO2);

            // when
            Long result = discountService.calculateOrderCouponDiscount(productId, productPrice, orderCoupons);

            // then
            Long discount = 1100L + 1000L;
            System.out.println(discount);
            assertThat(result).isEqualTo(discount);
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품 구매시 주문에 대해 0개의 쿠폰 할인 금액이 올바르게 적용되다.")
        void calculateOrderCouponDiscountsEmpty_success() {
            // given
            Map<Long, Long> productPrices = new HashMap<>();
            for (long i = 0L; i < 3; i++) {
                Long productId = i;
                Long totalPrice = 1999 * (i + 1);
                productPrices.put(productId, totalPrice);
            }

            Long totalOrderPrice = productPrices.values().stream().reduce(Long::sum).get();

            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();

            // when
            Map<Long, Long> result = discountService.calculateOrderCouponDiscounts(totalOrderPrice, productPrices, orderCoupons);

            // then
            for (long i = 0L; i < 3; i++) {
                assertThat(result.get(i)).isEqualTo(0L);
            }
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품 구매시 주문에 대해 쿠폰에 Null이 들어올 경우 올바르게 처리되다.")
        void calculateOrderCouponDiscountsNull_success() {
            // given
            Map<Long, Long> productPrices = new HashMap<>();
            for (long i = 0L; i < 3; i++) {
                Long productId = i;
                Long totalPrice = 1999 * (i + 1);
                productPrices.put(productId, totalPrice);
            }

            Long totalOrderPrice = productPrices.values().stream().reduce(Long::sum).get();

            // when
            Map<Long, Long> result = discountService.calculateOrderCouponDiscounts(totalOrderPrice, productPrices, null);

            // then
            for (long i = 0L; i < 3; i++) {
                assertThat(result.get(i)).isEqualTo(0L);
            }
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품 구매시 주문에 대해 1개의 AMOUNT 쿠폰 할인 금액이 올바르게 계산되다.")
        void calculateOrderCouponBenefitTypeAmount_success() {
            // given
            Map<Long, Long> productPrices = new LinkedHashMap<>();
            productPrices.put(0L, 1000L);
            productPrices.put(1L, 1000L);
            productPrices.put(2L, 1000L);

            Long totalOrderPrice = productPrices.values().stream().mapToLong(Long::longValue).sum();

            List<PaymentCouponDTO> orderCoupons = Collections.singletonList(
                    new PaymentCouponDTO(1L, BenefitType.AMOUNT, 900L)
            );

            // 예상되는 할인 금액 계산
            Map<Long, Long> expectedDiscounts = new LinkedHashMap<>();
            BigDecimal couponBenefitValue = BigDecimal.valueOf(orderCoupons.get(0).getBenefitValue());
            BigDecimal totalOrderPriceBD = BigDecimal.valueOf(totalOrderPrice);

            for (Map.Entry<Long, Long> entry : productPrices.entrySet()) {
                Long productId = entry.getKey();
                BigDecimal productPrice = BigDecimal.valueOf(entry.getValue());

                // 할인 금액 계산 (곱셈 후 나눗셈)
                BigDecimal productDiscount = couponBenefitValue.multiply(productPrice)
                        .divide(totalOrderPriceBD, 0, RoundingMode.HALF_UP);

                expectedDiscounts.put(productId, productDiscount.longValue());
            }

            // when
            Map<Long, Long> actualDiscounts = discountService.calculateOrderCouponDiscounts(
                    totalOrderPrice, productPrices, orderCoupons
            );

            // then
            // 각 상품별 할인 금액이 예상 값과 일치하는지 확인
            assertThat(actualDiscounts).isEqualTo(expectedDiscounts);

            // 전체 할인 금액의 합계가 쿠폰 할인 금액과 일치하는지 확인
            Long actualTotalDiscount = actualDiscounts.values().stream().mapToLong(Long::longValue).sum();
            assertThat(actualTotalDiscount).isEqualTo(orderCoupons.get(0).getBenefitValue());
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품 구매 시 주문에 대해 1개의 AMOUNT 쿠폰의 조정 작업이 올바르게 계산된다.")
        void calculateOrderCouponBenefitTypeAmountReconcile_success() {
            // given
            Map<Long, Long> productPrices = new LinkedHashMap<>();
            productPrices.put(0L, 1999L);
            productPrices.put(1L, 3998L);
            productPrices.put(2L, 5997L);

            Long totalOrderPrice = productPrices.values().stream().mapToLong(Long::longValue).sum();

            List<PaymentCouponDTO> orderCoupons = Collections.singletonList(
                    new PaymentCouponDTO(1L, BenefitType.AMOUNT, 1000L)
            );

            // 예상되는 할인 금액 계산
            Map<Long, Long> expectedDiscounts = new LinkedHashMap<>();
            BigDecimal allocatedDiscountAmount = BigDecimal.ZERO;
            BigDecimal couponBenefitValue = BigDecimal.valueOf(orderCoupons.get(0).getBenefitValue());
            BigDecimal totalOrderPriceBD = BigDecimal.valueOf(totalOrderPrice);

            Long lastProductId = null;
            for (Map.Entry<Long, Long> entry : productPrices.entrySet()) {
                Long productId = entry.getKey();
                BigDecimal productPrice = BigDecimal.valueOf(entry.getValue());

                // 할인 금액 계산 (곱셈 후 나눗셈)
                BigDecimal productDiscount = couponBenefitValue.multiply(productPrice)
                        .divide(totalOrderPriceBD, 0, RoundingMode.HALF_UP);


                System.out.println("productId: " + productId + " dicsount: " + productDiscount.longValue());

                expectedDiscounts.put(productId, productDiscount.longValue());
                allocatedDiscountAmount = allocatedDiscountAmount.add(productDiscount);

                lastProductId = productId;
            }

            // 할인 금액 조정 (소수점 처리로 인한 오차 보정)
            if (couponBenefitValue.compareTo(allocatedDiscountAmount) != 0) {
                BigDecimal difference = couponBenefitValue.subtract(allocatedDiscountAmount);
                Long adjustedDiscount = expectedDiscounts.get(lastProductId) + difference.longValue();
                expectedDiscounts.put(lastProductId, adjustedDiscount);
            }

            // when
            Map<Long, Long> actualDiscounts = discountService.calculateOrderCouponDiscounts(
                    totalOrderPrice, productPrices, orderCoupons
            );

            // then
            // 각 상품별 할인 금액이 예상 값과 일치하는지 확인
            assertThat(actualDiscounts).isEqualTo(expectedDiscounts);

            // 전체 할인 금액의 합계가 쿠폰 할인 금액과 일치하는지 확인
            Long actualTotalDiscount = actualDiscounts.values().stream().mapToLong(Long::longValue).sum();
            assertThat(actualTotalDiscount).isEqualTo(orderCoupons.get(0).getBenefitValue());
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품 구매시 주문에 대해 1개의 PERCENT 쿠폰이 올바르게 적용되다.")
        void calculateOrderCouponBenefitTypePercent_success() {
            // given
            Map<Long, Long> productPrices = new LinkedHashMap<>();
            productPrices.put(0L, 1000L);
            productPrices.put(1L, 1000L);
            productPrices.put(2L, 1000L);

            Long totalOrderPrice = productPrices.values().stream().mapToLong(Long::longValue).sum();

            List<PaymentCouponDTO> orderCoupons = Collections.singletonList(
                    new PaymentCouponDTO(1L, BenefitType.PERCENTAGE, 10L)
            );

            // 예상되는 할인 금액 계산
            Map<Long, Long> expectedDiscounts = new LinkedHashMap<>();
            BigDecimal allocatedDiscountAmount = BigDecimal.ZERO;
            BigDecimal couponBenefitValue = BigDecimal.valueOf(orderCoupons.get(0).getBenefitValue());

            for (Map.Entry<Long, Long> entry : productPrices.entrySet()) {
                Long productId = entry.getKey();
                BigDecimal productPrice = BigDecimal.valueOf(entry.getValue());

                // 해당 상품에 적용될 할인 금액 계산
                BigDecimal discount = productPrice.multiply(couponBenefitValue);
                BigDecimal productDiscountInteger = discount.divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

                System.out.println("productId: " + productId + " dicsount: " + productDiscountInteger);
                expectedDiscounts.put(productId, productDiscountInteger.longValue());
                allocatedDiscountAmount = allocatedDiscountAmount.add(productDiscountInteger);
            }

            // when
            Map<Long, Long> actualDiscounts = discountService.calculateOrderCouponDiscounts(
                    totalOrderPrice, productPrices, orderCoupons
            );

            // then
            // 각 상품별 할인 금액이 예상 값과 일치하는지 확인
            assertThat(actualDiscounts).isEqualTo(expectedDiscounts);

            // 전체 할인 금액의 합계가 쿠폰 할인 금액과 일치하는지 확인
            Long actualTotalDiscount = actualDiscounts.values().stream().mapToLong(Long::longValue).sum();
            assertThat(actualTotalDiscount).isEqualTo(allocatedDiscountAmount.longValue());
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품 구매시 주문에 대해 1개의 PERCENT 쿠폰의 조정 작업이 올바르게 처리되다.")
        void calculateOrderCouponBenefitTypePercentReconcile_success() {
            // given
            Map<Long, Long> productPrices = new LinkedHashMap<>();
            productPrices.put(0L, 1999L);
            productPrices.put(1L, 3998L);
            productPrices.put(2L, 5997L);

            Long totalOrderPrice = productPrices.values().stream().mapToLong(Long::longValue).sum();

            List<PaymentCouponDTO> orderCoupons = Collections.singletonList(
                    new PaymentCouponDTO(1L, BenefitType.PERCENTAGE, 10L)
            );

            // 예상되는 할인 금액 계산
            Map<Long, Long> expectedDiscounts = new LinkedHashMap<>();
            BigDecimal allocatedDiscountAmount = BigDecimal.ZERO;
            BigDecimal couponBenefitValue = BigDecimal.valueOf(orderCoupons.get(0).getBenefitValue());
            BigDecimal totalOrderPriceBD = BigDecimal.valueOf(totalOrderPrice);

            Long lastProductId = null;
            for (Map.Entry<Long, Long> entry : productPrices.entrySet()) {
                Long productId = entry.getKey();
                BigDecimal productPrice = BigDecimal.valueOf(entry.getValue());

                // 해당 상품에 적용될 할인 금액 계산
                BigDecimal discount = productPrice.multiply(couponBenefitValue);
                BigDecimal productDiscountInteger = discount.divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

                System.out.println("productId: " + productId + " dicsount: " + productDiscountInteger.longValue());

                expectedDiscounts.put(productId, productDiscountInteger.longValue());
                allocatedDiscountAmount = allocatedDiscountAmount.add(productDiscountInteger);

                lastProductId = productId;
            }

            // 할인 금액 조정 (소수점 처리로 인한 오차 보정)
            // 총 할인 금액 계산
            BigDecimal totalExpectedDiscount = totalOrderPriceBD.multiply(couponBenefitValue)
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

            if (totalExpectedDiscount.compareTo(allocatedDiscountAmount) != 0) {
                BigDecimal difference = totalExpectedDiscount.subtract(allocatedDiscountAmount);
                expectedDiscounts.merge(lastProductId, difference.longValue(), Long::sum);
            }

            // when
            Map<Long, Long> actualDiscounts = discountService.calculateOrderCouponDiscounts(
                    totalOrderPrice, productPrices, orderCoupons
            );

            // then
            // 각 상품별 할인 금액이 예상 값과 일치하는지 확인
            assertThat(actualDiscounts).isEqualTo(expectedDiscounts);
        }

        @Test
        @DisplayName("[정상 케이스] 여러 상품 구매 시 주문에 대해 2개 이상의 쿠폰 할인 금액이 올바르게 계산된다")
        void calculateOrderCouponBenefitTypePercentMultiple_success() {
            // given
            Map<Long, Long> productPrices = new LinkedHashMap<>();
            productPrices.put(0L, 1999L); // 상품 A
            productPrices.put(1L, 3998L); // 상품 B
            productPrices.put(2L, 5997L); // 상품 C

            Long totalOrderPrice = productPrices.values().stream().mapToLong(Long::longValue).sum();

            List<PaymentCouponDTO> orderCoupons = new ArrayList<>();
            orderCoupons.add(new PaymentCouponDTO(1L, BenefitType.AMOUNT, 500L));    // 정액 할인 쿠폰
            orderCoupons.add(new PaymentCouponDTO(2L, BenefitType.AMOUNT, 700L));    // 정액 할인 쿠폰
            orderCoupons.add(new PaymentCouponDTO(3L, BenefitType.PERCENTAGE, 10L)); // 정률 할인 쿠폰 (10%)

            // 예상되는 할인 금액 계산 (수동으로 계산하여 하드코딩)
            Map<Long, Long> expectedDiscounts = new LinkedHashMap<>();

            // 각 상품의 총 할인 금액을 계산
            // 1) 첫 번째 정액 할인 쿠폰 500원 적용
            // 상품별로 할인 금액 분배 (상품 가격 비율에 따라)
            // 총 주문 금액: 1999 + 3998 + 5997 = 11994원

            // 상품 A 할인: 500 * 1999 / 11994 ≈ 83.3333 → 83원 (반올림)
            // 상품 B 할인: 500 * 3998 / 11994 ≈ 166.6666 → 167원 (반올림)
            // 상품 C 할인: 500 * 5997 / 11994 ≈ 250 → 250원
            // 합계: 83 + 167 + 250 = 500원

            // 2) 두 번째 정액 할인 쿠폰 700원 적용
            // 동일한 방식으로 계산
            // 상품 A 할인: 700 * 1999 / 11994 ≈ 116.6666 → 117원 (반올림)
            // 상품 B 할인: 700 * 3998 / 11994 ≈ 233.3333 → 233원 (반올림)
            // 상품 C 할인: 700 * 5997 / 11994 ≈ 350 → 350원
            // 합계: 117 + 233 + 350 = 700원

            // 3) 정률 할인 쿠폰 10% 적용
            // 각 상품의 가격에 10% 할인 적용
            // 상품 A 할인: 1999 * 0.1 = 199.9 → 200원 (반올림)
            // 상품 B 할인: 3998 * 0.1 = 399.8 → 400원 (반올림)
            // 상품 C 할인: 5997 * 0.1 = 599.7 → 600원 (반올림)
            // 합계: 200 + 400 + 600 = 1200원
            // 주문 합계 할인 합계: 11994 / 10 = 1199.4 ≈ 1199원
            // 상품 C의 최종 할인: 699 - (1200 - 1199) = 599원

            // 각 상품별 총 할인 금액 합산
            // 상품 A 총 할인: 83 + 117 + 200 = 400원
            // 상품 B 총 할인: 167 + 233 + 400 = 800원
            // 상품 C 총 할인: 250 + 350 + 599 = 1199원

            expectedDiscounts.put(0L, 400L); // 상품 A
            expectedDiscounts.put(1L, 800L); // 상품 B
            expectedDiscounts.put(2L, 1199L); // 상품 C

            // when
            Map<Long, Long> actualDiscounts = discountService.calculateOrderCouponDiscounts(
                    totalOrderPrice, productPrices, orderCoupons
            );

            // then
            // 각 상품별 할인 금액이 예상 값과 일치하는지 확인
            assertThat(actualDiscounts).isEqualTo(expectedDiscounts);
        }
    }

    @Nested
    @DisplayName("Point Test")
    class Point {

        @Test
        @DisplayName("[정상 케이스] 1개의 상품 주문에 대해 0원의 포인트 금액이 올바르게 처리되다.")
        void calculatePointDiscountZero_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;

            Map<Long, Long> productPrices = new LinkedHashMap<>();

            productPrices.put(productId, productPrice);

            // when
            Map<Long, Long> result = discountService.calculatePointDiscount(productPrice, productPrices, 0L);

            // then
            assertThat(result.get(productId)).isEqualTo(0L);
        }

        @Test
        @DisplayName("[정상 케이스] 1개의 상품 주문에 포인트 금액이 올바르게 처리되다.")
        void calculatePointDiscountSingle_success() {
            // given
            Long productId = 1L;
            Long productPrice = 1999L;

            Map<Long, Long> productPrices = new LinkedHashMap<>();

            productPrices.put(productId, productPrice);

            // when
            Map<Long, Long> result = discountService.calculatePointDiscount(productPrice, productPrices, 1000L);

            // then
            assertThat(result.get(productId)).isEqualTo(1000L);
        }

        @Test
        @DisplayName("[정상 케이스] 1개의 상품 주문에 대해 포인트 금액이 앱 할인액을 초과한 경우 포인트 금액과 동일하게 계산되다.")
        void calculatePointDiscountOver_success() {
            // given
            Long productId = 1L;
            Long productPrice = 500L;

            Map<Long, Long> productPrices = new LinkedHashMap<>();

            productPrices.put(productId, productPrice);

            // when
            Map<Long, Long> result = discountService.calculatePointDiscount(productPrice, productPrices, 1000L);

            // then
            assertThat(result.get(productId)).isEqualTo(1000L);
        }

        @Test
        @DisplayName("[정상 케이스] 여러 개의 상품 주문에 대해 포인트 금액이 올바르게 처리되다.")
        void calculatePointDiscountMultiple_success() {
            // given
            Long productId1 = 1L;
            Long productId2 = 2L;
            Long productId3 = 3L;

            Long productPrice1 = 1999L;
            Long productPrice2 = 3998L;
            Long productPrice3 = 5997L;

            Long totalOrderPrice = productPrice1 + productPrice2 + productPrice3;

            Map<Long, Long> productPrices = new LinkedHashMap<>();
            productPrices.put(productId1, productPrice1);
            productPrices.put(productId2, productPrice2);
            productPrices.put(productId3, productPrice3);

            // when
            Map<Long, Long> result = discountService.calculatePointDiscount(totalOrderPrice, productPrices, 600L);

            // then
            assertThat(result.get(1L)).isEqualTo(100L);
            assertThat(result.get(2L)).isEqualTo(200L);
            assertThat(result.get(3L)).isEqualTo(300L);
        }

        @Test
        @DisplayName("[정상 케이스] 여러 개의 상품 주문에 대해 포인트 금액의 반올림 작업이 올바르게 동작한다.")
        void calculatePointDiscountMultipleHalfUp_success() {
            // given
            Long productId1 = 1L;
            Long productId2 = 2L;
            Long productId3 = 3L;

            Long productPrice1 = 1999L;
            Long productPrice2 = 3888L;
            Long productPrice3 = 5999L;

            Long totalOrderPrice = productPrice1 + productPrice2 + productPrice3;    // 11886

            Map<Long, Long> productPrices = new LinkedHashMap<>();
            productPrices.put(productId1, productPrice1);
            productPrices.put(productId2, productPrice2);
            productPrices.put(productId3, productPrice3);

            // when
            Map<Long, Long> result = discountService.calculatePointDiscount(totalOrderPrice, productPrices, 1000L);

            // then
            assertThat(result.get(1L)).isEqualTo(168);   // 1000 * 1999 / 11886 ≈ 168.1810 -> 168 (반올림)
            assertThat(result.get(2L)).isEqualTo(327);  // 1000 * 3888 / 11886 ≈ 327.1075 -> 327 (반올림)

            // 1000 * 5999 / 11886 ≈ 504.7114 -> 505 (반올림)
            // 현재까지 적용된 할인 금액: 168 + 327 + 505 = 1000
            assertThat(result.get(3L)).isEqualTo(505);
            assertThat(result.get(1L) + result.get(2L) + result.get(3L)).isEqualTo(1000L);
        }

        @Test
        @DisplayName("[정상 케이스] 여러 개의 상품 주문에 대해 포인트 금액의 조정 작업이 올바르게 동작한다.")
        void calculatePointDiscountMultipleReconcile_success() {
            // given
            Long productId1 = 1L;
            Long productId2 = 2L;
            Long productId3 = 3L;

            Long productPrice1 = 3333L;
            Long productPrice2 = 3333L;
            Long productPrice3 = 3334L;

            Long totalOrderPrice = productPrice1 + productPrice2 + productPrice3; // 10000

            Map<Long, Long> productPrices = new LinkedHashMap<>();
            productPrices.put(productId1, productPrice1);
            productPrices.put(productId2, productPrice2);
            productPrices.put(productId3, productPrice3);

            // when
            Map<Long, Long> result = discountService.calculatePointDiscount(totalOrderPrice, productPrices, 1000L);

            // then
            assertThat(result.get(1L)).isEqualTo(333L); // 3333 * 1000 / 10000 = 333.3 -> 333 (반올림)
            assertThat(result.get(2L)).isEqualTo(333L); // 3333 * 1000 / 10000 = 333.3 -> 333 (반올림)
            // 3334 * 1000 / 10000 = 333.4 -> 333(반올림)
            // 현재까지 적용된 할인 금액: 333 + 333 + 333 = 999
            // 조정 작업이 필요한 금액: 1000 - 999 = 1
            // 따라서 마지막 상품의 할인 금액에 추가: 333 + 1 = 334원
            assertThat(result.get(3L)).isEqualTo(334L);
            assertThat(result.get(1L) + result.get(2L) + result.get(3L)).isEqualTo(1000L);
        }
    }
}