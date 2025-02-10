package com.impacus.maketplace.repository.payment.querydsl;

import com.impacus.maketplace.repository.payment.querydsl.dto.PaymentEventPeriodWithOrdersDTO;

import java.time.LocalDate;
import java.util.List;

public interface PaymentEventCustomRepository {

    // TODO: 실행 계획을 통해 인덱스 레인지 스캔이 재대로 이루어지는지 확인 (excludPaymentEventId)
    List<PaymentEventPeriodWithOrdersDTO> findPaymentEventsWithOrdersInPeriod(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            Long excludePaymentEventId
    );
}
