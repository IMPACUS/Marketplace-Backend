package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
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

import static org.assertj.core.api.Assertions.assertThat;

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
     * - marketPrice: 10000
     * - appSalesPrice: 10000
     * - discountPrice: 8000
     * - productStatus: SALES_STOP (판매 중지)
     * - productType: GREEN_TAG (에코 상품)
     * - 옵션 1: 재고 100개(id: 7L), 옵션 2: 재고 0개(id: 8L), 옵션 3: 삭제된 옵션(id: 9L)
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
     * - benefitValue: 10L (10% 할인)
     * - productType: ALL (모든 상품 적용)
     * - coverageType: BRAND (특정 브랜드 적용)
     * - brandName: 테스트마켓 (올바르게 적용)
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
