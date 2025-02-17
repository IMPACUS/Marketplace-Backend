package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
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

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[비즈니스 로직] - 쿠폰 발행 1차 검증 테스트")
class CouponIssuanceValidatorTest {

    @InjectMocks
    private CouponIssuanceValidator couponIssuanceValidator;

    @Mock
    private UserCouponRepository userCouponRepository;

    @Mock
    private PaymentEventRepository paymentEventRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Nested
    @DisplayName("ADMIN이 발행해주는 쿠폰 검증")
    class AdminCouponValidationTests {

        @Test
        @DisplayName("쿠폰 검증 성공")
        public void shouldPassWhenCouponIsNotDeleted() {
            // given
            Coupon coupon = Coupon.builder().isDeleted(false).build();

            // When && then
            assertThatNoException().isThrownBy(() ->
                    couponIssuanceValidator.validateAdminIssuedCouponWithException(coupon));
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 삭제된 쿠폰")
        public void shouldFailWhenCouponIsDeleted() {
            // given
            Coupon coupon = Coupon.builder().isDeleted(true).build();

            // When && Then
            Assertions.assertThatThrownBy(() ->
                            couponIssuanceValidator.validateAdminIssuedCouponWithException(coupon))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.IS_DELETED_COUPON);
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
         *         <li>선착순 쿠폰일 경우 선착 순 발급 수 값이 발급한 수량보다 높아야 한다.({@code firstCount > quantityIssued})</li>
         *         <li>지급형 쿠폰</li>
         *         <li>1회성 쿠폰</li>
         *         <li>기간 설정 되어 있을 경우 확인</li>
         *         <li>발급 중지 상태 X</li>
         *         <li>삭제된 쿠폰 X</li>
         *     </ol>
         * </p>
         */

        @Test
        @DisplayName("쿠폰 검증 성공")
        public void shouldPassWhenPaymentTargetIsAll() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUED)    // 발급 중지 상태 X
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())).thenReturn(false);

            // when && then
            assertThatNoException().isThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon));
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 삭제된 쿠폰")
        public void shouldThrowsExceptionWhenCouponIsDeleted() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(true)   // 삭제 O
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUED)    // 발급 중지 상태 X
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when && then
            assertThatThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.IS_DELETED_COUPON);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 선착순 설정")
        public void shouldThrowsExceptionWhenCouponIsOverFirstCount() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.FIRST)   // 선착순 설정 X
                    .firstCount(100)
                    .quantityIssued(100L)
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 X
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when && then
            assertThatThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.END_FIRST_COUNT_COUPON);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 발급 상태 중지")
        public void shouldThrowsExceptionWhenCouponStatusIsStop() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.STOP)    // 발급 중지 상태 O
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when && then
            assertThatThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.IS_STOP_COUPON);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 지급형 쿠폰 X")
        public void shouldThrowsExceptionWhenCouponIsNotProvisionCoupon() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.EVENT)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when && then
            assertThatThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 1회성 쿠폰 X")
        public void shouldThrowsExceptionWhenCouponIssueTypeIsNotOneTime() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.PERSISTENCE)   // 1회성 타입 X
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            // when && then
            assertThatThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 발급 받은 이력 존재 O")
        public void shouldThrowsExceptionWhenCouponIsAlreadyIssuedToUser() {
            // given
            Long userId = 1L;
            Coupon coupon = Coupon.builder()
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.UNSET)   // 지정 기간 설정 X
                    .build();

            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())).thenReturn(true);

            assertThatThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.ALREADY_ISSUED_COUPON);
        }

        @Test
        @DisplayName("쿠폰 검증 실패 - 기간 설정 조건 불만족")
        public void shouldThrowsExceptionWhenCouponPeriodIsNotSatisfied() {
            // given
            Long userId = 1L;
            Coupon coupon1 = Coupon.builder()
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.SET)   // 지정 기간 설정 X
                    .periodStartAt(LocalDate.now().minusDays(2))
                    .periodEndAt(LocalDate.now().minusDays(1))
                    .build();

            Coupon coupon2 = Coupon.builder()
                    .isDeleted(false)   // 삭제 X
                    .paymentTarget(PaymentTarget.ALL)   // 선착순 설정 X
                    .couponType(CouponType.PROVISION)   // 지급형 쿠폰 X
                    .couponIssueType(CouponIssueType.ONETIME)   // 1회성 타입
                    .statusType(CouponStatusType.ISSUING)    // 발급 중지 상태 O
                    .periodType(PeriodType.SET)   // 지정 기간 설정 X
                    .periodStartAt(LocalDate.now().plusDays(1))
                    .periodEndAt(LocalDate.now().plusDays(2))
                    .build();

            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon1.getId())).thenReturn(false);
            when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon2.getId())).thenReturn(false);

            assertThatThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon1))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
            assertThatThrownBy(() ->
                    couponIssuanceValidator.validateProvisionCouponWithException(userId, coupon2))
                    .extracting(e -> ((CustomException) e).getErrorType())
                    .isEqualTo(CouponErrorType.INVALID_COUPON_REGISTER_EXCEPTION);
        }



    }

}