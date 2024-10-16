package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.enumType.error.OrderErrorType;
import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.config.PaymentConfig;
import com.impacus.maketplace.dto.payment.request.AddressInfoDTO;
import com.impacus.maketplace.dto.payment.request.CheckoutSingleDTO;
import com.impacus.maketplace.dto.payment.request.PaymentProductInfoDTO;
import com.impacus.maketplace.dto.payment.response.PaymentSingleDTO;
import com.impacus.maketplace.service.payment.checkout.CheckoutService;
import com.impacus.maketplace.service.payment.utils.PaymentTestDataInitializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutServiceIntegrationTest {

    @Autowired
    CheckoutService checkoutService;

    @Autowired
    PaymentTestDataInitializer paymentTestDataInitializer;

    @Autowired
    PaymentConfig paymentConfig;

    @BeforeAll
    void setup() {
        paymentTestDataInitializer.deleteAllData();
        paymentTestDataInitializer.initializeTestData();
    }

    /**
     * 테스트 설명
     * 결제 초기 단계 테스트를 위한 기본 세팅
     *
     * 1. 판매자 정보
     * - id: 1L
     * - marketName: 테스트마켓
     *
     * 2. 상품 정보
     * 2.1 상품 1
     * - id: 1L
     * - name: 테스트 상품1
     * - marketPrice: 10000
     * - appSalesPrice: 10000
     * - discountPrice: 10000
     * - productStatus: SALES_PROGRESS (판매 진행중)
     * - productType: GENERAL (일반 상품)
     * - 옵션 1: 재고 100 개(id: 1L), 옵션 2: 제고 0개(id: 2L), 옵션 3: 삭제된 옵션(id: 3L)
     * - 옵션 히스토리 정상 저장(id 동일)
     *
     * 2.2 상품 2
     * - id: 2L
     * - name: 테스트 상품2
     * - marketPrice: 10000
     * - appSalesPrice: 10000
     * - discountPrice: 8000
     * - productStatus: SALES_PROGRESS (판매 진행중)
     * - productType: GREEN_TAG (에코 상품)
     * - 옵션 1: 재고 100개(id: 4L), 옵션 2: 재고 0개(id: 5L), 옵션 3: 삭제된 옵션(id: 6L)
     * - 옵션 히스토리 정상 저장(id 동일)
     *
     * 2.3 상품 3
     * - id: 3L
     * - name: 테스트 상품3
     * - marketPrice: 20000
     * - appSalesPrice: 20000
     * - discountPrice: 16999
     * - productStatus: SALES_PROGRESS (판매 진행중)
     * - productType: GREEN_TAG (에코 상품)
     * - 옵션 1: 재고 100개(id: 7L), 옵션 2: 재고 0개(id: 8L), 옵션 3: 삭제된 옵션(id: 9L)
     * - 옵션 히스토리 정상 저장(id 동일)
     *
     * 2.4 상품 4
     * - id: 4L
     * - name: 테스트 상품4
     * - marketPrice: 10000
     * - appSalesPrice: 10000
     * - discountPrice: 8000
     * - productStatus: SALES_STOP (판매 중지)
     * - productType: GREEN_TAG (에코 상품)
     * - 옵션 1: 재고 100개(id: 10L), 옵션 2: 재고 0개(id: 11L), 옵션 3: 삭제된 옵션(id: 12L)
     * - 옵션 히스토리 정상 저장(id 동일)
     *
     * 3. 사용자(소비자) 정보
     * - id: 1L
     * - email: test@test.com
     * - password: testpassword
     * - name: testName
     * - userIdName: tsetUserIdName
     * - phoneNumber: 000-0000-0000
     *
     * 4. 그린 포인트 정보
     * - id: 1L
     * - userId: 1L
     * - greenLabelPoint: 10000L (보유 포인트: 10000 포인트)
     *
     * 5. 쿠폰 정보
     * 5.1 쿠폰 1
     * - id: 1L
     * - code: "test1"
     * - benefitType: PERCENTAGE (할인 적용 방식)
     * - benefitValue: 10L (10% 할인)
     * - productType: ALL (모든 상품 적용)
     * - coverageType: ALL (모든 상품 적용)
     * - brandName: null
     * - standardType: UNLIMITED (최소 적용 가능 금액 X)
     * - useStandardValue: null
     *
     * 5.2 쿠폰 2
     * - id: 2L
     * - code: "test2"
     * - benefitType: PERCENTAGE (할인 적용 방식)
     * - benefitValue: 10L (10% 할인)
     * - productType: ALL (모든 상품 적용)
     * - coverageType: BRAND (특정 브랜드 적용)
     * - brandName: 테스트마켓 (올바르게 적용)
     * - standardType: UNLIMITED (최소 적용 가능 금액 X)
     * - useStandardValue: null
     *
     * 5.3 쿠폰 3
     * - id: 3L
     * - code: "test3"
     * - benefitType: PERCENTAGE (할인 적용 방식)
     * - benefitValue: 10L (10% 할인)
     * - productType: ALL (모든 상품 적용)
     * - coverageType: BRAND (특정 브랜드 적용)
     * - brandName: 틀린마켓이름 (틀린 마켓 이름 적용)
     * - standardType: UNLIMITED (최소 적용 가능 금액 X)
     * - useStandardValue: null
     *
     * 5.4 쿠폰 4
     * - id: 4L
     * - code: "test4"
     * - benefitType: PERCENTAGE (할인 적용 방식)
     * - benefitValue: 20L (20% 할인)
     * - productType: ALL (모든 상품 적용)
     * - coverageType: ALL (모든 상품 적용)
     * - brandName: null
     * - standardType: LIMIT (최소 적용 가능 금액 적용)
     * - useStandardValue: 10000L (1만원)
     *
     * 5.5 쿠폰 5
     * - id: 5L
     * - code: "test5"
     * - benefitType: PERCENTAGE (할인 적용 방식)
     * - benefitValue: 10L (10% 할인)
     * - productType: BASIC (일반 상품 적용)
     * - coverageType: BRAND (특정 브랜드 적용)
     * - brandName: 테스트마켓 (올바르게 적용)
     * - standardType: LIMIT (최소 적용 가능 금액 적용)
     * - useStandardValue: 10000L (1만원)
     *
     * 5.6 쿠폰 6
     * - id: 6L
     * - code: "test6"
     * - benefitType: PERCENTAGE (할인 적용 방식)
     * - benefitValue: 10L (10% 할인)
     * - productType: ECO_GREEN (그린 태그 상품 적용)
     * - coverageType: ALL (모든 상품 적용)
     * - brandName: null
     * - standardType: LIMIT (최소 적용 가능 금액 적용)
     * - useStandardValue: 10000L (1만원)
     *
     * 5.7 쿠폰 7
     * - id: 7L
     * - code: "test7"
     * - benefitType: AMOUNT (할인 적용 방식)
     * - benefitValue: 5000L (5000원 할인)
     * - productType: ALL (모든 상품 적용)
     * - coverageType: ALL (모든 상품 적용)
     * - brandName: null
     * - standardType: UNLIMITED (최소 적용 가능 금액 X)
     * - useStandardValue: null
     *
     * 5.8 쿠폰 8
     * - id: 8L
     * - code: "test8"
     * - benefitType: AMOUNT (할인 적용 방식)
     * - benefitValue: 5000L (5000원 할인)
     * - productType: ALL (모든 상품 적용)
     * - coverageType: ALL (모든 상품 적용)
     * - brandName: null
     * - standardType: LIMITED (최소 적용 가능 금액 적용)
     * - useStandardValue: 20000L (2만원)
     *
     * 5.9 쿠폰 9
     * - id: 9L
     * - code: "test9"
     * - 이미 사용한 쿠폰
     *
     * 5.10 쿠폰 10
     * - id: 10L
     * - code: "test10"
     * - 만료된 쿠폰
     *
     * 5.11 쿠폰 11
     * - id: 11L
     * - code: "test11"
     * - 지급 실패한 쿠폰
     *
     * 6. 사용자 쿠폰
     * 사용자 쿠폰 id는 위와 동일하고 조건도 동일 (쿠폰 9, 10, 11은 사용되면 안되는 쿠폰)
     */

    @Nested
    class CheckoutSingle {

        @Test
        @DisplayName("[정상 케이스] - 모든 할인 적용 없이 초기 결제 처리가 올바르게 동작하다.")
        void checkoutSingle_success () {
            // given
            Long userId = 1L;
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(null)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 1: 10000원 짜리 일반 상품
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.CARD, false, null, 10000L);

            // when
            PaymentSingleDTO result = checkoutService.checkoutSingle(userId, checkoutSingleDTO);

            // then
            assertThat(result.getTotalDiscountedAmount()).isEqualTo(10000L);
            assertThat(result.getStoreId()).isEqualTo(paymentConfig.getStoreId());
            assertThat(result.getChannelKey()).isEqualTo(paymentConfig.getChannelKeyByPaymentMethod(checkoutSingleDTO.getMethod()));
        }

        @Test
        @DisplayName("[정상 케이스] - 1개의 쿠폰 할인만 적용했을 경우 초기 결제 처리가 올바르게 동작하다.")
        void checkoutSingleDiscountedCoupon_success() {
            // given
            Long userId = 1L;
            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 1: 10000원 짜리 일반 상품 + 10% 할인
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.KAKAO_PAY, false, null, 9000L);

            // when
            PaymentSingleDTO result = checkoutService.checkoutSingle(userId, checkoutSingleDTO);

            // then
            assertThat(result.getTotalDiscountedAmount()).isEqualTo(9000L);
            assertThat(result.getStoreId()).isEqualTo(paymentConfig.getStoreId());
            assertThat(result.getChannelKey()).isEqualTo(paymentConfig.getChannelKeyByPaymentMethod(PaymentMethod.KAKAO_PAY));
        }

        @Test
        @DisplayName("[정상 케이스] 여러 개의 쿠폰 할인을 적용했을 경우 초기 결제 처리가 올바르게 처리되다.")
        void checkoutSingleDiscountedCoupons_success() {
            // given
            Long userId = 1L;
            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            appliedCouponForProductIds.add(7L);
            ArrayList<Long> appliedCommonUserCouponIds = new ArrayList<>();
            appliedCouponForProductIds.add(6L);

            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(2L)
                    .productOptionId(4L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 2: 8000원 짜리 상품 -> 결제 금액: 8000 - (10000 * 10/100 + 5000) - (10000 * 10/100) = 1000
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, 0L, PaymentMethod.CARD, false, null, 1000L);

            // when
            PaymentSingleDTO result = checkoutService.checkoutSingle(userId, checkoutSingleDTO);

            // then
            assertThat(result.getTotalDiscountedAmount()).isEqualTo(1000L);
        }

        @Test
        @DisplayName("[정상 케이스] - 모든 할인 금액을 적용했을 경우 올바르게 측정된다.")
        void checkoutSingleDiscounted_success() {
            // given
            Long userId = 1L;
            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            ArrayList<Long> appliedCommonUserCouponIds = new ArrayList<>();
            appliedCouponForProductIds.add(6L);

            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(2L)
                    .productOptionId(4L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 2: 8000원 상품 -> 8000 - (10000 * 10/100) - (10000 * 10/100) - (1000) = 5000
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, 1000L, PaymentMethod.CARD, false, null, 5000L);

            // when
            PaymentSingleDTO result = checkoutService.checkoutSingle(userId, checkoutSingleDTO);

            // then
            assertThat(result.getTotalDiscountedAmount()).isEqualTo(5000L);
        }

        @Test
        @DisplayName("[정상 케이스] - 모든 쿠폰 할인 금액이 총 상품 가격을 넘어섰을 경우 0원으로 측정되다.")
        void checkoutSingleDiscountedCouponsOverAmount_success() {
            // given
            Long userId = 1L;
            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            appliedCouponForProductIds.add(4L);
            appliedCouponForProductIds.add(7L);
            ArrayList<Long> appliedCommonUserCouponIds = new ArrayList<>();
            appliedCouponForProductIds.add(6L);

            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(2L)
                    .productOptionId(4L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 2: 8000원 상품 -> 8000 - (10000 * 10/100 + 10000 * 20/100 + 5000) - (10000 * 10/100) = -1000 -> 0
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, 0L, PaymentMethod.CARD, false, null, 0L);

            // when
            PaymentSingleDTO result = checkoutService.checkoutSingle(userId, checkoutSingleDTO);

            // then
            assertThat(result.getTotalDiscountedAmount()).isEqualTo(0L);
        }

        @Test
        @DisplayName("[예외 케이스] - 쿠폰 할인 금액이 총 상품 가격을 넘어섰을 경우 포인트 적용은 불가능하다. [INVALID_USE_POINT]")
        void checkoutSingleCanNotAppliedPointThenDiscountedCouponsOverAmount_failed() {
            // given
            Long userId = 1L;
            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            appliedCouponForProductIds.add(4L);
            appliedCouponForProductIds.add(7L);
            ArrayList<Long> appliedCommonUserCouponIds = new ArrayList<>();
            appliedCouponForProductIds.add(6L);

            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(2L)
                    .productOptionId(4L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 2: 8000원 상품 -> 8000 - (10000 * 10/100 + 10000 * 20/100 + 5000) - (10000 * 10/100) = -1000 -> 0 then 포인트 적용 불가
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, 1000L, PaymentMethod.CARD, false, null, 0L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(PaymentErrorType.INVALID_USE_POINT);
        }

        @Test
        @DisplayName("[예외 케이스] - 판매 중지된 상품은 구매할 수 없다. [SALE_STOP_ORDER_PRODUCT]")
        void checkoutSingleSalesStopProduct_failed() {
            // given
            Long userId = 1L;

            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(4L)
                    .productOptionId(10L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(null)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 4: 판매 중지된 상품
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.CARD, false, null, 8000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(OrderErrorType.SALE_STOP_ORDER_PRODUCT);
        }

        @Test
        @DisplayName("[예외 케이스] - 옵션이 삭제된 상품은 구매할 수 없다. [DELETED_ORDER_PRODUCT_OPTION]")
        void checkoutSingleDeletedProductOption_failed() {
            // given
            Long userId = 1L;

            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(3L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(null)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 1: 옵션이 삭제된 상품
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.CARD, false, null, 10000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(OrderErrorType.DELETED_ORDER_PRODUCT_OPTION);
        }

        @Test
        @DisplayName("[예외 케이스] - 상품의 재고가 부족한 경우 구매할 수 없다. [OUT_OF_STOCK_ORDER_PRODUCT]")
        void checkoutSingleOutOfStockProduct_failed() {
            // given
            Long userId = 1L;

            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(2L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(null)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 1: 재고가 0인 상품
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.CARD, false, null, 10000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(OrderErrorType.OUT_OF_STOCK_ORDER_PRODUCT);
        }

        @Test
        @DisplayName("[정상 케이스] - 할인 과정에서 소수점은 반올림으로 처리되다.")
        void checkoutSingleRoundedProcessWhenDiscountPointIsDecimalPoint_success() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            ArrayList<Long> appliedCommonUserCouponIds = new ArrayList<>();
            appliedCommonUserCouponIds.add(4L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(3L)
                    .productOptionId(7L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 1개: : 앱 판매가가 19999원짜리인 할인된 가격 16999원 상품 1개 -> 16999 - (19999 * 10/100) - (19999 * 20/100) = 16999 - 2000 - 4000 = 10999
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, 0L, PaymentMethod.CARD, false, null, 10999L);

            // when
            PaymentSingleDTO result = checkoutService.checkoutSingle(userId, checkoutSingleDTO);

            // then
            assertThat(result.getTotalDiscountedAmount()).isEqualTo(10999L);
        }

        @Test
        @DisplayName("[예외 케이스] - 중복 쿠폰은 적용할 수 없다. [DUPLICATE_USE_USER_COUPON]")
        void checkoutSingleDuplicatedCoupon_failed() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            ArrayList<Long> appliedCommonUserCouponIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, 0L, PaymentMethod.CARD, false, null, 8000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(PaymentErrorType.DUPLICATE_USE_USER_COUPON);
        }

        @Test
        @DisplayName("[예외 케이스] - 이미 사용한 쿠폰은 적용할 수 없다. [INVALID_ACCESS_USER_COUPON]")
        void checkoutSingleAppliedUsedCoupon_failed() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(9L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.CARD, false, null, 5000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(CouponErrorType.INVALID_ACCESS_USER_COUPON);
        }

        @Test
        @DisplayName("[예외 케이스] - 만료된 쿠폰은 사용할 수 없다. [INVALID_ACCESS_USER_COUPON]")
        void checkoutSingleAppliedExpiredCoupon_failed() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(10L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.CARD, false, null, 5000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(CouponErrorType.INVALID_ACCESS_USER_COUPON);
        }

        @Test
        @DisplayName("[예외 케이스] - 지급 실패한 쿠폰은 사용할 수 없다.")
        void checkoutSingleAppliedIssueFailCoupon_failed() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(11L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.KAKAO_PAY, false, null, 5000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(CouponErrorType.INVALID_ACCESS_USER_COUPON);
        }

        @Test
        @DisplayName("[예외 케이스] - 에코 상품 타입 체크가 올바르지 않은 경우 [INVALID_APPLIED_USER_COUPON]")
        void checkoutSingleProductTypeMismatchCouponType_failed() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(6L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.CARD, false, null, 9000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(CouponErrorType.INVALID_APPLIED_USER_COUPON);
        }

        @Test
        @DisplayName("[예외 케이스] - 브랜드 이름이 일치하지 않는 경우 [INVALID_APPLIED_USER_COUPON]")
        void checkoutSingleMismatchBrandName_faild() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(3L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, null, 0L, PaymentMethod.CARD, false, null, 9000L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(CouponErrorType.INVALID_APPLIED_USER_COUPON);
        }

        @Test
        @DisplayName("[예외 케이스] - 총 주문 금액이 적용 가능한 금액보다 낮을 경우")
        void checkoutSingleInsufficientTotalAmount() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCommonUserCouponIds = new ArrayList<>();
            appliedCommonUserCouponIds.add(8L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(1L)
                    .productOptionId(1L)
                    .quantity(1L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(null)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, 0L, PaymentMethod.CARD, false, null, 0L);

            // when
            CustomException exception = assertThrows(CustomException.class, () ->
                    checkoutService.checkoutSingle(userId, checkoutSingleDTO));

            // then
            assertThat(exception.getErrorType()).isEqualTo(CouponErrorType.INVALID_APPLIED_USER_COUPON);
        }


        @Test
        @DisplayName("[정상 케이스] 모든 복잡한 할인 및 여러 수량으로 상품 구매시 올바르게 처리되다.")
        void checkoutSingleComplicatedDiscountedProducts_success() {
            // given
            Long userId = 1L;

            ArrayList<Long> appliedCouponForProductIds = new ArrayList<>();
            appliedCouponForProductIds.add(1L);
            appliedCouponForProductIds.add(7L);
            ArrayList<Long> appliedCommonUserCouponIds = new ArrayList<>();
            appliedCommonUserCouponIds.add(4L);
            PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                    .productId(3L)
                    .productOptionId(7L)
                    .quantity(3L)
                    .sellerId(1L)
                    .appliedCouponForProductIds(appliedCouponForProductIds)
                    .build();

            AddressInfoDTO addressInfoDTO = createAddressInfoDTO();

            // 상품 3개: 앱 판매가가 19999원짜리인 할인된 가격 16999원 상품 3개 -> 16999 * 3 - (19999 * 3 * 10/100 + 5000) - (19999 * 3 * 20/100) = 50997 - (5999.7 + 5000) - 11999.4 -> 27998 - 3000 = 24998
            CheckoutSingleDTO checkoutSingleDTO = new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, 3000L, PaymentMethod.KAKAO_PAY, false, null, 24998L);

            // when
            PaymentSingleDTO result = checkoutService.checkoutSingle(userId, checkoutSingleDTO);

            // then
            assertThat(result.getTotalDiscountedAmount()).isEqualTo(24998L);
        }


        private AddressInfoDTO createAddressInfoDTO() {
            return AddressInfoDTO.builder()
                    .name("배송지1")
                    .receiver("받는 이")
                    .postalCode("000-000")
                    .address("메인 주소지")
                    .detailAddress("상세 주소지")
                    .connectNumber("000-0000-0000")
                    .memo(null)
                    .build();
        }
    }
}
