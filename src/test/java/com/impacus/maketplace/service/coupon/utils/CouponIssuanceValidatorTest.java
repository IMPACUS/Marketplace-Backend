package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[비즈니스 로직] - 쿠폰 발행 검증 테스트")
class CouponIssuanceValidatorTest {

    @InjectMocks
    private CouponIssuanceValidator couponIssuanceValidator;

    @Mock
    private UserCouponRepository userCouponRepository;

    @Mock
    private PaymentEventRepository paymentEventRepository;

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Nested
    @DisplayName("ADMIN이 발행해주는 쿠폰 검증")
    class AdminCouponValidationTests {

        @Test
        @DisplayName("쿠폰 검증 성공")
        void shouldPassWhenCouponIsNotDeleted() {
            // given
            Coupon coupon = Coupon.builder().isDeleted(false).build();

            // When & then
            assertThatNoException().isThrownBy(() -> couponIssuanceValidator.validateAdminIssuedCouponWithException(coupon));
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 삭제된 쿠폰")
        void shouldFailWhenCouponIsDeleted() {
            // given
            Coupon coupon = Coupon.builder().isDeleted(true).build();

            // When & Then
            Assertions.assertThatThrownBy(() -> couponIssuanceValidator.validateAdminIssuedCouponWithException(coupon)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.IS_DELETED_COUPON);
        }
    }

    @Nested
    @DisplayName("Provision 쿠폰 검증")
    class ProvisionCouponValidationTests {

        /**
         * Provision 쿠폰 검증 통과 조건
         *
         * <p>
         *     <ol>
         *         <li>삭제된 쿠폰 X</li>
         *         <li>발급 중지 상태 X</li>
         *         <li>선착순 쿠폰일 경우 선착 순 발급 수 값이 발급한 수량보다 높아야 한다.({@code firstCount > quantityIssued})</li>
         *         <li>지급형 쿠폰</li>
         *         <li>1회성 쿠폰</li>
         *         <li>기간 설정 되어 있을 경우 확인</li>
         *     </ol>
         * </p>
         */

        @Test
        @DisplayName("쿠폰 검증 성공")
        void shouldPassWhenAllConditionsAreSatisfied() {
            // given
            Long userId = 1L;
            Coupon coupon1 = Coupon.builder()
                    .id(1L)
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUED)    // 발급 중지 상태 X
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            Coupon coupon2 = Coupon.builder()
                    .id(2L)
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 O
                    .quantityIssued(0L)
                    .firstCount(100)
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUED)    // 발급 중지 상태 X
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            Coupon coupon3 = Coupon.builder()
                    .id(3L)
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 O
                    .quantityIssued(1L)
                    .firstCount(100)
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 X
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 O
                    .periodStartAt(LocalDate.now().minusDays(1))
                    .periodEndAt(LocalDate.now().plusDays(1))
                    .build();


            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon1.getId())).thenReturn(false);
            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon2.getId())).thenReturn(false);
            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon3.getId())).thenReturn(false);


            // when & then
            assertThatNoException().isThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon1));
            assertThatNoException().isThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon2));
            assertThatNoException().isThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon3));
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 삭제된 쿠폰")
        void shouldThrowExceptionWhenCouponIsDeleted() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .id(1L)
                    .isDeleted(true)   // 삭제 O
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUED)    // 발급 중지 상태 X
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when & then
            assertThatThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.IS_DELETED_COUPON);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 선착순 설정")
        void shouldThrowExceptionWhenCouponIsOverFirstCount() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder().isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.FIRST)   // 선착순 설정 X
                    .firstCount(100).quantityIssued(100L).couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 X
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when & then
            assertThatThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.END_FIRST_COUNT_COUPON);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 발급 상태 중지")
        void shouldThrowExceptionWhenCouponStatusIsStop() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder().isDeleted(false)   // 삭제 X
                    .id(1L)
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.STOP)    // 발급 중지 상태 O
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when & then
            assertThatThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.IS_STOP_COUPON);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 지급형 쿠폰 X")
        void shouldThrowExceptionWhenCouponIsNotProvisionCoupon() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder().isDeleted(false)   // 삭제 X
                    .id(1L)
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.EVENT)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when & then
            assertThatThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 1회성 쿠폰 X")
        void shouldThrowExceptionWhenCouponIssueTypeIsNotOneTime() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder().isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.PERSISTENCE)   // 1회성 타입 X
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when & then
            assertThatThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 발급 받은 이력 존재 O")
        void shouldThrowExceptionWhenCouponIsAlreadyIssuedToUser() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder().isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())).thenReturn(true);

            assertThatThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.ALREADY_ISSUED_COUPON);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 기간 설정 조건 불만족")
        void shouldThrowExceptionWhenCouponPeriodIsNotSatisfied() {
            // given
            Long userId = 1L;
            Coupon coupon1 = Coupon.builder().isDeleted(false)   // 삭제 X
                    .id(1L)
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.SET)   // 지정 기간 설정 X
                    .periodStartAt(LocalDate.now().minusDays(2)).periodEndAt(LocalDate.now().minusDays(1)).build();

            Coupon coupon2 = Coupon.builder().isDeleted(false)   // 삭제 X
                    .id(2L)
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.SET)   // 지정 기간 설정 X
                    .periodStartAt(LocalDate.now().plusDays(1)).periodEndAt(LocalDate.now().plusDays(2)).build();

            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon1.getId())).thenReturn(false);
            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon2.getId())).thenReturn(false);

            assertThatThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon1)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
            assertThatThrownBy(() -> couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon2)).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }


    }

    @Nested
    @DisplayName("Event 쿠폰 공통 검증")
    class EventCouponValidationTests {

        /**
         * 이벤트 쿠폰 검증 통과 조건
         *
         * <p>
         *     <ol>
         *         <li>삭제된 쿠폰 X</li>
         *         <li>발급 중지 상태 X</li>
         *         <li>선착순 쿠폰일 경우 선착 순 발급 수 값이 발급한 수량보다 높아야 한다.({@code firstCount > quantityIssued})</li>
         *         <li>이벤트형 쿠폰</li>
         *         <li>쿠폰 이벤트 타입 일치</li>
         *         <li>일회성 쿠폰일 경우 발급 이력 X</li>
         *     </ol>
         * </p>
         */

        @Test
        @DisplayName("쿠폰 검증 성공")
        void shouldPassWhenAllConditionsAreSatisfied() {
            // given
            Long userId = 1L;
            Coupon coupon1 = Coupon.builder()
                    .id(1L)
                    .isDeleted(false)       // 삭제된 쿠폰 X
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_ORDER) // 이벤트 타입 설정
                    .couponIssueType(CouponIssueType.ONETIME)   // 일회성 쿠폰
                    .build();

            Coupon coupon2 = Coupon.builder()
                    .id(2L)
                    .isDeleted(false)       // 삭제된 쿠폰 X
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .quantityIssued(0L)
                    .firstCount(4)
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.SCHEDULING_PAYMENT_PRODUCT) // 이벤트 타입 설정
                    .couponIssueType(CouponIssueType.PERSISTENCE)   // 지속형 쿠폰
                    .build();

            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon1.getId())).thenReturn(false);

            // when & then
            assertThat(couponIssuanceValidator.validateEventCoupon(userId, coupon1, EventType.PAYMENT_ORDER)).isTrue();
            assertThat(couponIssuanceValidator.validateEventCoupon(userId, coupon2, EventType.SCHEDULING_PAYMENT_PRODUCT)).isTrue();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 삭제된 쿠폰")
        void shouldFailWhenCouponIsDeleted() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(true)       // 삭제된 쿠폰 O
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_ORDER) // 이벤트 타입 설정
                    .couponIssueType(CouponIssueType.PERSISTENCE)   // 일회성 쿠폰
                    .build();

            // when & then
            assertThat(couponIssuanceValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_ORDER)).isFalse();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 발급 중지 상태")
        void shouldFailWhenCouponStatusIsStop() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)       // 삭제된 쿠폰 X
                    .statusType(CouponStatusType.STOP)    // 발급 중지 상태 O
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_ORDER) // 이벤트 타입 설정
                    .couponIssueType(CouponIssueType.PERSISTENCE)   // 일회성 쿠폰
                    .build();

            // when & then
            assertThat(couponIssuanceValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_ORDER)).isFalse();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 선착순 설정 불만족")
        void shouldFailWhenCouponIssuedQuantityIsOverFirstCount() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)       // 삭제된 쿠폰 X
                    .statusType(CouponStatusType.ISSUED)    // 발급 중지 상태 X
                    .paymentTarget(PaymentTarget.FIRST)   // 선착순 설정 O
                    .quantityIssued(100L)
                    .firstCount(100)
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_ORDER) // 이벤트 타입 설정
                    .couponIssueType(CouponIssueType.PERSISTENCE)   // 일회성 쿠폰
                    .build();

            // when & then
            assertThat(couponIssuanceValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_ORDER)).isFalse();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 이벤트형 쿠폰 X")
        void shouldFailWhenCouponIsNotEventCoupon() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)       // 삭제된 쿠폰 X
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_ORDER) // 이벤트 타입 설정
                    .couponIssueType(CouponIssueType.PERSISTENCE)   // 일회성 쿠폰
                    .build();

            // when & then
            assertThat(couponIssuanceValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_ORDER)).isFalse();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 발급 이력 존재")
        void shouldFailWhenCouponIsAlreadyIssuedToUser() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)       // 삭제된 쿠폰 X
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_ORDER) // 이벤트 타입 설정
                    .couponIssueType(CouponIssueType.ONETIME)   // 일회성 쿠폰
                    .build();

            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())).thenReturn(true);

            // when & then
            assertThat(couponIssuanceValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_ORDER)).isFalse();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 이벤트 타입 미일치")
        void shouldFailWhenCouponEventTypeMismatch() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)       // 삭제된 쿠폰 X
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_ORDER) // 이벤트 타입 설정
                    .couponIssueType(CouponIssueType.PERSISTENCE)   // 일회성 쿠폰
                    .build();

            // when & then
            assertThat(couponIssuanceValidator.validateEventCoupon(userId, coupon, EventType.PAYMENT_PRODUCT)).isFalse();
        }
    }

    @Nested
    @DisplayName("Payment Order 쿠폰 검증")
    class PaymentOrderCoupon {
        private final String TEST_BRAND_NAME = "TEST_BRAND";

        /**
         * Payment Order Coupon 검증 통과 조건
         *
         * <p>
         *     <ol>
         *         <li>이벤트형 쿠폰</li>
         *         <li>이벤트 타입이 결제 상품과 관련된 타입</li>
         *         <li>발급 적용 범위가 브랜드일 경우, 해당 상품의 브랜드명과 발급 적용 범위 값이 일치</li>
         *         <li>쿠폰 지급 조건 확인</li>
         *     </ol>
         * </p>
         */
        @Test
        @DisplayName("쿠폰 검증 성공")
        void shouldPassWhenAllConditionsAreSatisfied() {
            // given
            Long paymentOrderId = 1L;

            PaymentOrder paymentOrder = PaymentOrder.builder()
                    .id(1L)
                    .sellerId(1L)
                    .quantity(1L)
                    .amount(10000L)
                    .build();


            Coupon coupon1 = Coupon.builder()
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_PRODUCT)   // PRODUCT와 관련된 이벤트
                    .issueCoverageType(CoverageType.ALL)    // 모든 브랜드 적용
                    .issueConditionType(StandardType.UNLIMITED) // 지급 조건 X
                    .build();

            Coupon coupon2 = Coupon.builder()
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.SCHEDULING_PAYMENT_PRODUCT)   // PRODUCT와 관련된 이벤트
                    .issueCoverageType(CoverageType.BRAND)    //   특정 브랜드 적용
                    .issueCoverageSubCategoryName(TEST_BRAND_NAME)
                    .issueConditionType(StandardType.UNLIMITED) // 지급 조건 X
                    .build();

            Coupon coupon3 = Coupon.builder()
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_PRODUCT)   // RPODUCT와 관련된 이벤트
                    .issueCoverageType(CoverageType.ALL)    // 모든 브랜드 적용
                    .issueConditionType(StandardType.LIMIT) // 지급 조건 O
                    .issueConditionValue(10_000L)    // 10,000원 이상 상품 구매시
                    .build();

            when(paymentOrderRepository.findById(paymentOrderId)).thenReturn(Optional.of(paymentOrder));
            when(sellerRepository.findMarketNameBySellerId(paymentOrder.getSellerId())).thenReturn(TEST_BRAND_NAME);

            // when & then
            assertThat(couponIssuanceValidator.validatePaymentOrderCoupon(coupon1, paymentOrderId)).isTrue();
            assertThat(couponIssuanceValidator.validatePaymentOrderCoupon(coupon2, paymentOrderId)).isTrue();
            assertThat(couponIssuanceValidator.validatePaymentOrderCoupon(coupon3, paymentOrderId)).isTrue();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 이벤트형 타입 쿠폰 X")
        void shouldFailWhenCouponTypeIsNotEventCoupon() {
            // given
            Long paymentOrderId = 1L;

            Coupon coupon = Coupon.builder()
                    .couponType(CouponType.PROVISION)       // 이벤트형 쿠폰 X
                    .eventType(EventType.PAYMENT_PRODUCT)   // PRODUCT와 관련된 이벤트
                    .issueCoverageType(CoverageType.ALL)    // 모든 브랜드 적용
                    .issueConditionType(StandardType.UNLIMITED) // 지급 조건 X
                    .build();

            // when & then
            assertThat(couponIssuanceValidator.validatePaymentOrderCoupon(coupon, paymentOrderId)).isFalse();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 결제 상품 관련 이벤트 X")
        void shouldFailWhenCouponTypeIsNotRelatedPaymentProduct() {
            // given
            Long paymentOrderId = 1L;

            PaymentOrder paymentOrder = PaymentOrder.builder()
                    .id(1L)
                    .sellerId(1L)
                    .quantity(1L)
                    .amount(10000L)
                    .build();

            Coupon coupon = Coupon.builder()
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_ORDER)   // PRODUCT와 관련된 이벤트 X
                    .issueCoverageType(CoverageType.ALL)    // 모든 브랜드 적용
                    .issueConditionType(StandardType.UNLIMITED) // 지급 조건 X
                    .build();

            when(paymentOrderRepository.findById(paymentOrderId)).thenReturn(Optional.of(paymentOrder));

            // when & then
            assertThat(couponIssuanceValidator.validatePaymentOrderCoupon(coupon, paymentOrderId)).isFalse();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 브랜드명 불일치")
        void shouldFailWhenCouponIssueCoverageValueMismatch() {
            // given
            Long paymentOrderId = 1L;

            PaymentOrder paymentOrder = PaymentOrder.builder()
                    .id(1L)
                    .sellerId(1L)
                    .quantity(1L)
                    .amount(10000L)
                    .build();

            Coupon coupon = Coupon.builder()
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_PRODUCT)   // PRODUCT와 관련된 이벤트 O
                    .issueCoverageType(CoverageType.BRAND)    // 특정 브랜드 적용
                    .issueCoverageSubCategoryName(TEST_BRAND_NAME)
                    .issueConditionType(StandardType.UNLIMITED) // 지급 조건 X
                    .build();

            when(paymentOrderRepository.findById(paymentOrderId)).thenReturn(Optional.of(paymentOrder));
            when(sellerRepository.findMarketNameBySellerId(paymentOrder.getSellerId())).thenReturn("MISMATCH_BRAND_NAME");

            // when & then
            assertThat(couponIssuanceValidator.validatePaymentOrderCoupon(coupon, paymentOrderId)).isFalse();
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 지급 조건 불만족")
        void shouldFailWhenCouponIssueConditionIsNotSatisfied() {
            Long paymentOrderId = 1L;

            PaymentOrder paymentOrder = PaymentOrder.builder()
                    .id(1L)
                    .sellerId(1L)
                    .quantity(1L)
                    .amount(10000L)
                    .build();

            Coupon coupon = Coupon.builder()
                    .couponType(CouponType.EVENT)       // 이벤트형 쿠폰
                    .eventType(EventType.PAYMENT_PRODUCT)   // PRODUCT와 관련된 이벤트 O
                    .issueCoverageType(CoverageType.ALL)    // 모든 브랜드 적용
                    .issueConditionType(StandardType.LIMIT) // 지급 조건 X
                    .issueConditionValue(20000L)        // 20,000원 이상 상품 구매시
                    .build();

            when(paymentOrderRepository.findById(paymentOrderId)).thenReturn(Optional.of(paymentOrder));

            // when & then
            assertThat(couponIssuanceValidator.validatePaymentOrderCoupon(coupon, paymentOrderId)).isFalse();
        }
    }
}