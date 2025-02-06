package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.impacus.maketplace.dto.coupon.model.CouponConditionCheckResultDTO.*;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponPeriodConditionChecker {

    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final CouponRepository couponRepository;

    public CouponConditionCheckResultDTO checkPeriodCondition(Long couponId, Long paymentEventId) {
        return couponRepository.findById(couponId)
                .map(coupon -> {
                    PaymentEvent paymentEvent = paymentEventRepository.findById(paymentEventId)
                            .orElse(null);

                    if (paymentEvent == null) return fail();

                    return checkPeriodCondition(coupon, paymentEvent);
                }).orElse(fail());
    }

    public CouponConditionCheckResultDTO checkPeriodCondition(Coupon coupon, Long paymentEventId) {
        return paymentEventRepository.findById(paymentEventId)
                .map(paymentEvent -> checkPeriodCondition(coupon, paymentEvent))
                .orElse(fail());
    }

    public CouponConditionCheckResultDTO checkPeriodCondition(Coupon coupon, PaymentEvent paymentEvent) {

        // 1. 업데이트 Payment Order
        if (paymentEvent.isNotUpdatedOrders()) {
            List<PaymentOrder> paymentOrders = paymentOrderRepository.findByPaymentEventId(paymentEvent.getId())
                    .orElseThrow(() -> {
                        CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, PaymentErrorType.NOT_FOUND_MATCHED_PRODUCT_OPTION_HISTORY);
                        LogUtils.error("CouponPeriodConditionChecker.checkPeriodCondition", String.format("Not found payment orders by payment event id %d", paymentEvent.getId()), exception);
                        return exception;
                    });

            paymentEvent.setPaymentOrders(paymentOrders);
        }

        // 2. 어떤 조건인지 확인
        return switch (coupon.getPeriodType()) {
            case SET -> checkSetPeriodCondition(coupon, paymentEvent);
            case WEEKLY -> checkWeeklyCondition(coupon, paymentEvent);
            case MONTHLY -> checkMonthlyCondition(coupon, paymentEvent);
            default -> fail();
        };
    }

    /**
     * 기간 설정이 월간 N회 이상 주문으로 설정되어 있을 경우 조건을 만족하는지 확인한다.
     *
     * <p>
     *     <b>정책</b>
     *     <li>현재 날짜 기준으로 1일부터 현재 시점까지 발생한 주문을 사용한다.</li>
     *     <li>쿠폰 지급 조건이 설정되어 있는 경우 해당 조건을 충족해야 해당 주문을 사용할 수 있다.</li>
     *     <li>다른 주문 이벤트 쿠폰의 지급 조건으로 사용된 주문은 적용되지 않는다.</li>
     *     <li>상품 이벤트 쿠폰과는 독립적인 발급 관계로 해당 쿠폰의 지급 조건으로 사용된 주문(상품)도 적용 가능하다.</li>
     * </p>
     *
     * @param coupon 기간 설정 조건을 확인하기 위한 쿠폰
     * @param paymentEvent 발생한 결제 이벤트
     * @return 해당 쿠폰 발급 여부
     */
    private CouponConditionCheckResultDTO checkMonthlyCondition(Coupon coupon, PaymentEvent paymentEvent) {



        return fail();
    }

    /**
     * 기간 설정이 주간 N회 이상 주문으로 설정되어 있을 경우 조건을 만족하는지 확인한다.
     *
     * <p>
     *     <b>정책</b>
     *     <li>현재 날짜 기준으로 6일 전 00:00부터 발생한 주문을 기준으로 </li>
     * </p>
     *
     * @param coupon
     * @param paymentEvent
     * @return
     */
    private CouponConditionCheckResultDTO checkWeeklyCondition(Coupon coupon, PaymentEvent paymentEvent) {
        return fail();
    }

    private CouponConditionCheckResultDTO checkSetPeriodCondition(Coupon coupon, PaymentEvent paymentEvent) {

        return fail();
    }

}
