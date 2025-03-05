package com.impacus.maketplace.repository.payment.querydsl;

import com.impacus.maketplace.repository.payment.querydsl.dto.PaymentEventPeriodWithOrdersDTO;

import java.time.LocalDate;
import java.util.List;

public interface PaymentEventCustomRepository {

    /**
     * 주어진 기간 내 기본 조건을 충족한 주문 이벤트를 모두 조회한다.
     *
     * <p>
     *     기본 조건:
     *     <ol>
     *         <li>사용자와 일치하는 결제 이벤트만 조회한다.</li>
     *         <li>`excludePaymentEventId`가 `null`이 아닐 경우 해당 ID를 PK로 갖는 결제 이벤트는 제외한다.</li>
     *         <li>`startDate`가 `null`일 경우 `InvalidParameterException` 예외가 발생한다.</li>
     *         <li>`endDate`가 `null`일 경우 현재 날짜로 대체된다.</li>
     *         <li>결제 이벤트의 상태가 완료인 레코드만 조회한다.</li>
     *     </ol>
     * </p>
     *
     * @param userId 사용자 ID
     * @param startDate 기간 설정의 시작 날짜
     * @param endDate 기간 설정의 끝 날짜
     * @param excludePaymentEventId 제외하고 싶은 결제 이벤트의 ID
     * @return 조건을 만족하는 모든 결제 이벤트
     * @throws InvalidParameterException `startDate`가 `null`일 경우 발생
     */
    List<PaymentEventPeriodWithOrdersDTO> findPaymentEventsWithOrdersInPeriod(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            Long excludePaymentEventId
    );
}
