package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.enumType.payment.PaymentType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.DateUtils;
import com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.PaymentEventCouponRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.payment.querydsl.PaymentEventCustomRepository;
import com.impacus.maketplace.repository.payment.querydsl.dto.PaymentEventPeriodWithOrdersDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

// 단위 테스트 진행
@ExtendWith(MockitoExtension.class)
@DisplayName("[비즈니스 로직] - 결제 이벤트 관련 쿠폰 기간 조건 검증 테스트")
class CouponPeriodConditionCheckerTest {

    @InjectMocks
    private CouponPeriodConditionChecker couponPeriodConditionChecker;

    @Mock
    private PaymentEventRepository paymentEventRepository;

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private PaymentEventCustomRepository paymentEventCustomRepository;

    @Mock
    private PaymentEventCouponRepository paymentEventCouponRepository;

    @Test
    @DisplayName("기간 설정 조건이 UNSET일 경우 pass() 정적 팩토리 메서드로 반환된다.")
    void testPeriodConditionUnsetReturnsPass() {
        // given
        Long userId = 1L;
        Coupon coupon = Coupon.builder().periodType(PeriodType.UNSET).build();

        PaymentEvent paymentEvent = createPaymentEvent(1L);

        // when
        CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.isConditionSet()).isFalse();
        assertThat(result.getCoupon()).isEqualTo(coupon);
    }


    @Test
    @DisplayName("PaymentEvent애 매핑되는 PaymentOrder가 없는 경우 예외를 발생시킨다.")
    void testThrowsExceptionWhenNoPaymentOrderMappedToPaymentEvent() {
        // given
        Long userId = 1L;
        Coupon coupon = Coupon.builder().periodType(PeriodType.UNSET).build();

        PaymentEvent paymentEvent = createPaymentEvent(1L);
        paymentEvent.setPaymentOrders(Collections.emptyList());

        // when
        assertThatThrownBy(() -> couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent)).isInstanceOf(CustomException.class).extracting(e -> ((CustomException) e).getErrorType()).isEqualTo(PaymentErrorType.NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID);
    }

    @Nested
    @DisplayName("월간 기간 조건 검증 테스트")
    class Monthly {

        @BeforeEach
        void setup() {
            try (MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                dateUtils.when(DateUtils::getFirstDayOfCurrentMonth).thenReturn(LocalDate.now().withDayOfMonth(1));
            }
        }

        @Test
        @DisplayName("쿠폰 발급 이력이 없은 상황에서 기간 조건 검증 통과 - 지급 조건 없는 쿠폰")
        void testNoCouponHistoryPassWithoutCondition() {
            // given
            Long userId = 1L;
            Coupon coupon = createMonthlyCoupon(1L, StandardType.UNLIMITED, null, 1L);
            PaymentEvent paymentEvent = createPaymentEvent(1L);
            LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
            LocalDate endDate = LocalDate.now();


            List<PaymentEventPeriodWithOrdersDTO> paymentEventDTOs = createTestPaymentEvents();

            when(paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId())).thenReturn(paymentEventDTOs);
            when(paymentEventCouponRepository.findIdByPaymentEventIdIn(any(List.class))).thenReturn(new HashSet());


            // when
            CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

            // then
            assertThat(result.isValid()).isTrue();
            assertThat(result.isConditionSet()).isTrue();
            assertThat(result.getCoupon()).isEqualTo(coupon);
            assertThat(result.getPaymentEventIds().size()).isEqualTo(7);
        }

        @Test
        @DisplayName("쿠폰 이력이 없은 상황에서 기간 조건 검증 통과 - 지급 조건 있는 쿠폰")
        void testNoCouponHistoryPassWithCondition() {
            // given
            Long userId = 1L;
            Coupon coupon = createMonthlyCoupon(1L, StandardType.LIMIT, 20000L, 3L);
            PaymentEvent paymentEvent = createPaymentEvent(1L);
            LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
            LocalDate endDate = LocalDate.now();


            List<PaymentEventPeriodWithOrdersDTO> paymentEventDTOs = createTestPaymentEvents();

            when(paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId())).thenReturn(paymentEventDTOs);
            when(paymentEventCouponRepository.findIdByPaymentEventIdIn(any(List.class))).thenReturn(new HashSet());


            // when
            CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

            // then
            assertThat(result.isValid()).isTrue();
            assertThat(result.isConditionSet()).isTrue();
            assertThat(result.getCoupon()).isEqualTo(coupon);
            assertThat(result.getPaymentEventIds().size()).isEqualTo(7);
        }

        @Test
        @DisplayName("쿠폰 발급 이력이 없는 상황에서 기간 조건 검증 실패 - 지급 조건 불만족")
        void testNoCouponHistoryFailDueToCondition() {
            // given
            Long userId = 1L;
            Coupon coupon = createMonthlyCoupon(1L, StandardType.LIMIT, 50000L, 4L);
            PaymentEvent paymentEvent = createPaymentEvent(1L);
            LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
            LocalDate endDate = LocalDate.now();


            List<PaymentEventPeriodWithOrdersDTO> paymentEventDTOs = createTestPaymentEvents();

            when(paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId())).thenReturn(paymentEventDTOs);
            when(paymentEventCouponRepository.findIdByPaymentEventIdIn(any(List.class))).thenReturn(new HashSet());

            // when
            CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

            // then
            assertThat(result.isValid()).isFalse();
        }

        @Test
        @DisplayName("쿠폰 발급 이력이 없는 상황에서 기간 조건 검증 실패 - 트리거 이벤트 수 부족")
        void testNoCouponHistoryFailDueToInsufficientEvents() {
            // given
            Long userId = 1L;
            Coupon coupon = createMonthlyCoupon(1L, StandardType.LIMIT, 10000L, 8L);
            PaymentEvent paymentEvent = createPaymentEvent(1L);
            LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
            LocalDate endDate = LocalDate.now();

            List<PaymentEventPeriodWithOrdersDTO> paymentEventDTOs = createTestPaymentEvents();

            when(paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId())).thenReturn(paymentEventDTOs);
            when(paymentEventCouponRepository.findIdByPaymentEventIdIn(any(List.class))).thenReturn(new HashSet());

            // when
            CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

            // then
            assertThat(result.isValid()).isFalse();
        }

        @Test
        @DisplayName("쿠폰 발급 이력이 있는 상황에서 기간 조건 검증 성공 - 지급 조건 없는 쿠폰")
        void testCouponHistoryPassWithoutCondition() {
            // given
            Long userId = 1L;
            Coupon coupon = createMonthlyCoupon(1L, StandardType.UNLIMITED, null, 3L);
            PaymentEvent paymentEvent = createPaymentEvent(1L);
            LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
            LocalDate endDate = LocalDate.now();

            List<PaymentEventPeriodWithOrdersDTO> paymentEventDTOs = createTestPaymentEvents();

            Set<Long> alreadyUsedPaymentEventIds = new HashSet<>();
            paymentEventDTOs.stream().limit(3).forEach(item -> alreadyUsedPaymentEventIds.add(item.getPaymentEventId()));

            when(paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId())).thenReturn(paymentEventDTOs);
            when(paymentEventCouponRepository.findIdByPaymentEventIdIn(any(List.class))).thenReturn(alreadyUsedPaymentEventIds);

            // when
            CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

            // then
            assertThat(result.isValid()).isTrue();
            assertThat(result.isConditionSet()).isTrue();
            assertThat(result.getCoupon()).isEqualTo(coupon);
            assertThat(result.getPaymentEventIds().size()).isEqualTo(4);
        }

        @Test
        @DisplayName("쿠폰 발급 이력이 있는 상황에서 기간 조건 검증 성공 - 지급 조건 있는 쿠폰")
        void testCouponHistoryPassWithCondition() {
            // given
            Long userId = 1L;
            Coupon coupon = createMonthlyCoupon(1L, StandardType.LIMIT, 20000L, 3L);
            PaymentEvent paymentEvent = createPaymentEvent(1L);
            LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
            LocalDate endDate = LocalDate.now();

            List<PaymentEventPeriodWithOrdersDTO> paymentEventDTOs = createTestPaymentEvents();

            Set<Long> alreadyUsedPaymentEventIds = new HashSet<>();
            paymentEventDTOs.stream().limit(3).forEach(item -> alreadyUsedPaymentEventIds.add(item.getPaymentEventId()));

            when(paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId())).thenReturn(paymentEventDTOs);
            when(paymentEventCouponRepository.findIdByPaymentEventIdIn(any(List.class))).thenReturn(alreadyUsedPaymentEventIds);

            // when
            CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

            // then
            assertThat(result.isValid()).isTrue();
            assertThat(result.isConditionSet()).isTrue();
            assertThat(result.getCoupon()).isEqualTo(coupon);
            assertThat(result.getPaymentEventIds().size()).isEqualTo(4);
        }

        @Test
        @DisplayName("쿠폰 발급 이력이 있는 상황에서 기간 조건 검증 실패 - 지급 조건 불만족")
        void testCouponHistoryFailDueToCondition() {
            // given
            Long userId = 1L;
            Coupon coupon = createMonthlyCoupon(1L, StandardType.LIMIT, 30000L, 3L);
            PaymentEvent paymentEvent = createPaymentEvent(1L);
            LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
            LocalDate endDate = LocalDate.now();

            List<PaymentEventPeriodWithOrdersDTO> paymentEventDTOs = createTestPaymentEvents();

            Set<Long> alreadyUsedPaymentEventIds = new HashSet<>();
            paymentEventDTOs.stream().filter(item -> item.getTotalAmount() >= 30000L).limit(3).forEach(item -> alreadyUsedPaymentEventIds.add(item.getPaymentEventId()));

            when(paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId())).thenReturn(paymentEventDTOs);
            when(paymentEventCouponRepository.findIdByPaymentEventIdIn(any(List.class))).thenReturn(alreadyUsedPaymentEventIds);

            // when
            CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

            // then
            assertThat(result.isValid()).isFalse();
            assertThat(result.isConditionSet()).isTrue();
        }

        @Test
        @DisplayName("쿠폰 발급 이력이 있는 상황에서 기간 조건 검증 실패 - 트리거 이벤트 수 부족")
        void testCouponHistoryFailDueToInsufficientEvents() {
            // given
            Long userId = 1L;
            Coupon coupon = createMonthlyCoupon(1L, StandardType.LIMIT, 20000L, 5L);
            PaymentEvent paymentEvent = createPaymentEvent(1L);
            LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
            LocalDate endDate = LocalDate.now();

            List<PaymentEventPeriodWithOrdersDTO> paymentEventDTOs = createTestPaymentEvents();

            Set<Long> alreadyUsedPaymentEventIds = new HashSet<>();
            paymentEventDTOs.stream().filter(item -> item.getTotalAmount() >= 50000L).limit(3).forEach(item -> alreadyUsedPaymentEventIds.add(item.getPaymentEventId()));

            when(paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId())).thenReturn(paymentEventDTOs);
            when(paymentEventCouponRepository.findIdByPaymentEventIdIn(any(List.class))).thenReturn(alreadyUsedPaymentEventIds);

            // when
            CouponConditionCheckResultDTO result = couponPeriodConditionChecker.checkPeriodCondition(userId, coupon, paymentEvent);

            // then
            assertThat(result.isValid()).isFalse();
            assertThat(result.isConditionSet()).isTrue();
        }
    }

    /**
     * 테스트용 PaymentEventPeriodWithOrdersDTO 목록을 생성한다.
     * <p>총 6개의 PaymentEventPeriodWithOrdersDTO가 만들어지며,</p>
     * <ul>
     *   <li>3개는 총 결제 금액이 20,000원 (2만 원)</li>
     *   <li>3개는 총 결제 금액이 50,000원 (5만 원)</li>
     * </ul>
     * 각각의 PaymentEvent는 2개의 PaymentOrderDTO로 구성됨.
     *
     * @return 6개의 PaymentEventPeriodWithOrdersDTO 목록
     */
    private List<PaymentEventPeriodWithOrdersDTO> createTestPaymentEvents() {
        List<PaymentEventPeriodWithOrdersDTO> results = new ArrayList<>();

        // 1) 3개 PaymentEvent → 총 결제금액 2만원
        // 예: 첫번째 이벤트 (eventId=101)은 (orderId=1011: 1*5000=5000, orderId=1012: 1*15000=15000) => 합 20000
        results.add(createPaymentEventDTO(101L, createOrder(1011L, 1L, 5000L), createOrder(1012L, 1L, 15000L))); // 총 20000원

        results.add(createPaymentEventDTO(102L, createOrder(1021L, 2L, 3000L), createOrder(1022L, 1L, 14000L))); // 총 20000원

        results.add(createPaymentEventDTO(103L, createOrder(1031L, 1L, 10000L), createOrder(1032L, 1L, 10000L))); // 총 20000원

        // 2) 3개 PaymentEvent → 총 결제금액 5만원
        // 예: 첫번째 이벤트 (eventId=201)은 (orderId=2011: 2*10000=20000, orderId=2012: 1*30000=30000) => 합 50000
        results.add(createPaymentEventDTO(201L, createOrder(2011L, 2L, 10000L), createOrder(2012L, 1L, 30000L))); // 총 50000원

        results.add(createPaymentEventDTO(202L, createOrder(2021L, 1L, 25000L), createOrder(2022L, 1L, 25000L))); // 총 50000원

        results.add(createPaymentEventDTO(203L, createOrder(2031L, 5L, 5000L), createOrder(2032L, 5L, 5000L)));  // 총 50000원

        return results;
    }

    /**
     * privateEvent를 생성하는 헬퍼 메서드
     */
    private PaymentEvent createPaymentEvent(Long eventId) {
        PaymentOrder paymentOrder1 = PaymentOrder.builder().id(1L).paymentEventId(eventId).quantity(1L).amount(10000L).status(PaymentOrderStatus.SUCCESS).build();

        PaymentOrder paymentOrder2 = PaymentOrder.builder().id(2L).paymentEventId(eventId).quantity(1L).amount(10000L).status(PaymentOrderStatus.SUCCESS).build();

        List<PaymentOrder> paymentOrders = List.of(paymentOrder1, paymentOrder2);


        return PaymentEvent.builder().id(eventId).buyerId(1L).isPaymentDone(true).idempotencyKey("test_idempotency_key").paymentId("test_payment_id").type(PaymentType.NORMAL).orderName("test_order_name").method(PaymentMethod.CARD).pspRawData(null).approvedAt(LocalDateTime.now()).paymentOrders(paymentOrders).build();
    }

    /**
     * PaymentEventPeriodWithOrdersDTO를 생성하는 헬퍼 메서드.
     */
    private PaymentEventPeriodWithOrdersDTO createPaymentEventDTO(Long eventId, PaymentEventPeriodWithOrdersDTO.PaymentOrderDTO order1, PaymentEventPeriodWithOrdersDTO.PaymentOrderDTO order2) {
        return new PaymentEventPeriodWithOrdersDTO(eventId, Arrays.asList(order1, order2));
    }

    /**
     * PaymentOrderDTO를 생성하는 헬퍼 메서드.
     *
     * @param paymentOrderId 주문 ID
     * @param quantity       수량
     * @param amount         단가(개당 금액)
     */
    private PaymentEventPeriodWithOrdersDTO.PaymentOrderDTO createOrder(Long paymentOrderId, Long quantity, Long amount) {
        // 기본적으로 성공 상태라고 가정
        return new PaymentEventPeriodWithOrdersDTO.PaymentOrderDTO(paymentOrderId, quantity, amount, PaymentOrderStatus.SUCCESS);
    }

    private Coupon createMonthlyCoupon(Long id, StandardType issueConditionType, Long issueConditionValue, Long numberOfPeriod) {
        return Coupon.builder().id(id).code("test_code_" + id).name("test_name").description("").benefitType(BenefitType.AMOUNT).benefitValue(10000L).productType(CouponProductType.ALL).paymentTarget(PaymentTarget.ALL).firstCount(null).quantityIssued(0L).issuedTimeType(IssuedTimeType.IMMEDIATE).couponType(CouponType.EVENT).couponIssueType(CouponIssueType.ONETIME).expireTimeType(ExpireTimeType.LIMIT).expireTimeDays(7).issueCoverageType(CoverageType.ALL).issueCoverageSubCategoryName(null).useCoverageType(CoverageType.ALL).useCoverageSubCategoryName(null).useStandardType(StandardType.UNLIMITED).useStandardValue(null).issueConditionType(issueConditionType).issueConditionValue(issueConditionValue).periodType(PeriodType.MONTHLY).periodStartAt(null).periodEndAt(null).numberOfPeriod(numberOfPeriod).autoManualType(AutoManualType.AUTO).loginAlarm(false).smsAlarm(false).emailAlarm(false).kakaoAlarm(false).statusType(CouponStatusType.ISSUED).isDeleted(false).build();
    }
}