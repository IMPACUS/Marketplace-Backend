package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.StandardType;
import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.DateUtils;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.PaymentEventCouponRepository;
import com.impacus.maketplace.repository.coupon.PaymentOrderCouponRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.payment.querydsl.PaymentEventCustomRepository;
import com.impacus.maketplace.repository.payment.querydsl.dto.PaymentEventPeriodWithOrdersDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO.fail;
import static com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO.pass;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponPeriodConditionChecker {

    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final CouponRepository couponRepository;
    private final PaymentEventCustomRepository paymentEventCustomRepository;
    private final PaymentEventCouponRepository paymentEventCouponRepository;
    private final PaymentOrderCouponRepository paymentOrderCouponRepository;

    /**
     * Coupon과 Payment Event를 모두 조회한 후 조건 확인 함수를 실행한다.
     *
     * @param userId         사용자 ID
     * @param couponId       기간 설정 조건 확인 대상에 해당하는 쿠폰 ID
     * @param paymentEventId 이벤트 트리거가 된 결제 이벤트의 ID
     * @return 기간 설정 조건 만족 여부와 세부 사항
     */
    public CouponConditionCheckResultDTO checkPeriodCondition(Long userId, Long couponId, Long paymentEventId) {
        return couponRepository.findById(couponId)
                .map(coupon -> {
                    // 1. Payment Event 조회
                    PaymentEvent paymentEvent = paymentEventRepository.findById(paymentEventId)
                            .orElse(null);

                    if (paymentEvent == null) return fail();

                    // 2. 조회 성공 시 조건 확인 작업
                    return checkPeriodCondition(userId, coupon, paymentEvent);
                }).orElse(fail());
    }

    /**
     * Payment Event를 조회한 후 조건 확인 함수를 실행한다.
     *
     * @param userId         사용자 ID
     * @param coupon         기간 설정 조건 확인 대상에 해당하는 쿠폰
     * @param paymentEventId 이벤트 트리거가 된 결제 이벤트의 ID
     * @return 기간 설정 조건 만족 여부와 세부 사항
     */

    public CouponConditionCheckResultDTO checkPeriodCondition(Long userId, Coupon coupon, Long paymentEventId) {
        return paymentEventRepository.findById(paymentEventId)
                .map(paymentEvent -> checkPeriodCondition(userId, coupon, paymentEvent))
                .orElse(fail());
    }

    /**
     * 기간 설정 조건을 확인한 후 조건을 충족 했는지 확인한다.
     *
     * @param userId       사용자 ID
     * @param coupon       기간 설정 조건 확인 대상에 해당하는 쿠폰
     * @param paymentEvent 이벤트 트리거가 된 결제 이벤트
     * @return 기간 설정 조건 만족 여부와 세부 사항
     */
    public CouponConditionCheckResultDTO checkPeriodCondition(Long userId, Coupon coupon, PaymentEvent paymentEvent) {

        // 1. 업데이트 Payment Order
        if (paymentEvent.isNotUpdatedOrders()) {
            List<PaymentOrder> paymentOrders = paymentOrderRepository.findByPaymentEventId(paymentEvent.getId())
                    .orElseThrow(() -> {
                        CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, PaymentErrorType.NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID);
                        LogUtils.error("CouponPeriodConditionChecker.checkPeriodCondition", String.format("Not found payment orders by payment event id %d", paymentEvent.getId()), exception);
                        return exception;
                    });

            paymentEvent.setPaymentOrders(paymentOrders);
        }

        // 2. 어떤 조건인지 확인
        return switch (coupon.getPeriodType()) {
            case SET -> checkSetPeriodCondition(userId, coupon, paymentEvent);
            case WEEKLY -> checkWeeklyCondition(userId, coupon, paymentEvent);
            case MONTHLY -> checkMonthlyCondition(userId, coupon, paymentEvent);
            default -> pass(coupon);
        };
    }

    /**
     * 기간 설정이 월간 N회 이상 주문으로 설정되어 있을 경우 조건을 만족하는지 확인한다.
     *
     * <p>
     * <b>정책</b>
     * <li>현재 날짜 기준으로 1일부터 현재 시점까지 발생한 주문을 사용한다.</li>
     * <li>쿠폰 지급 조건이 설정되어 있는 경우 해당 조건을 충족해야 해당 주문을 사용할 수 있다.</li>
     * <li>다른 주문 이벤트 쿠폰의 지급 조건으로 사용된 주문은 적용되지 않는다.</li>
     * <li>상품 이벤트 쿠폰과는 독립적인 발급 관계로 해당 쿠폰의 지급 조건으로 사용된 주문(상품)도 적용 가능하다.</li>
     * </p>
     *
     * @param coupon       기간 설정 조건을 확인하기 위한 쿠폰
     * @param paymentEvent 발생한 결제 이벤트
     * @return 해당 쿠폰 발급 여부
     */
    private CouponConditionCheckResultDTO checkMonthlyCondition(Long userId, Coupon coupon, PaymentEvent paymentEvent) {

        // 1. 조회 기간 날짜 설정
        LocalDate startDate = DateUtils.getFirstDayOfCurrentMonth();
        LocalDate endDate = LocalDate.now();

        // 2. 기간 내 결제 이벤트 조회
        List<PaymentEventPeriodWithOrdersDTO> paymentEvents = paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, paymentEvent.getId());
        paymentEvents.add(
                new PaymentEventPeriodWithOrdersDTO(paymentEvent.getId(),
                        paymentEvent.getPaymentOrders().stream().map(PaymentEventPeriodWithOrdersDTO.PaymentOrderDTO::new).toList())
        );

        // 3. 쿠폰 지급 조건 금액 확인
        List<PaymentEventPeriodWithOrdersDTO> filteredPaymentEvents = paymentEvents.stream()
                .filter(paymentEventDTO -> {
                    if (coupon.getIssueConditionType() == StandardType.LIMIT) {
                        return coupon.getIssueConditionValue() <= paymentEventDTO.getTotalAmount();
                    }
                    return true;
                })
                .toList();

        // 4. 이전에 쿠폰 발행에 사용된 이벤트인지 확인
        List<Long> paymentEventIds = filteredPaymentEvents.stream()
                .map(PaymentEventPeriodWithOrdersDTO::getPaymentEventId)
                .toList();

        Set<Long> alreadyUsedPaymentEventIds = paymentEventCouponRepository.findIdByPaymentEventIdIn(paymentEventIds);

        List<Long> resultIds = paymentEventIds.stream()
                .filter(id -> !alreadyUsedPaymentEventIds.contains(id))
                .toList();

        // 5. 최종 필터링 이후 조건을 만족하는 이벤트의 수를 통해 N회 조건 검증
        if (resultIds.size() < coupon.getNumberOfPeriod()) return fail();

        // 6. DTO 변환 후 반환
        return CouponConditionCheckResultDTO.getSuccessDTO(coupon, resultIds);
    }

    /**
     * 기간 설정이 주간 N회 이상 주문으로 설정되어 있을 경우 조건을 만족하는지 확인한다.
     *
     * <p>
     * <b>정책</b>
     * <li>현재 날짜 기준으로 6일 전 00:00부터 발생한 주문을 기준으로 </li>
     * </p>
     *
     * @param coupon
     * @param paymentEvent
     * @return
     */
    private CouponConditionCheckResultDTO checkWeeklyCondition(Long userId, Coupon coupon, PaymentEvent paymentEvent) {
        return fail();
    }

    private CouponConditionCheckResultDTO checkSetPeriodCondition(Long userId, Coupon coupon, PaymentEvent paymentEvent) {

        return fail();
    }

}
