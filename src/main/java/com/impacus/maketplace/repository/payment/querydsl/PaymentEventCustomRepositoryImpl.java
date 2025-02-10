package com.impacus.maketplace.repository.payment.querydsl;

import com.impacus.maketplace.entity.payment.QPaymentEvent;
import com.impacus.maketplace.entity.payment.QPaymentOrder;
import com.impacus.maketplace.repository.payment.querydsl.dto.PaymentEventPeriodWithOrdersDTO;
import com.impacus.maketplace.repository.payment.querydsl.dto.QPaymentEventPeriodWithOrdersDTO;
import com.impacus.maketplace.repository.payment.querydsl.dto.QPaymentEventPeriodWithOrdersDTO_PaymentOrderDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class PaymentEventCustomRepositoryImpl implements PaymentEventCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QPaymentEvent paymentEvent = QPaymentEvent.paymentEvent;
    private final QPaymentOrder paymentOrder = QPaymentOrder.paymentOrder;

    @Override
    public List<PaymentEventPeriodWithOrdersDTO> findPaymentEventsWithOrdersInPeriod(Long userId, LocalDate startDate, LocalDate endDate, Long excludePaymentEventId) {
        return queryFactory
                .from(paymentEvent)
                .innerJoin(paymentOrder).on(paymentOrder.paymentEventId.eq(paymentEvent.id))
                .where(
                        paymentEvent.buyerId.eq(userId),
                        excludePaymentEvent(excludePaymentEventId),
                        paymentEvent.approvedAt.between(
                                startDate.atStartOfDay(),
                                endDate.plusDays(1).atStartOfDay()
                        ),
                        paymentEvent.isPaymentDone.isTrue()
                )
                .transform(
                        groupBy(paymentEvent.id).list(
                                new QPaymentEventPeriodWithOrdersDTO(
                                        paymentEvent.id,
                                        list(new QPaymentEventPeriodWithOrdersDTO_PaymentOrderDTO(
                                                        paymentOrder.id,
                                                        paymentOrder.quantity,
                                                        paymentOrder.amount,
                                                        paymentOrder.status
                                                )
                                        )
                                )
                        )
                );
    }
    private BooleanExpression excludePaymentEvent(Long excludePaymentEventId) {
        return excludePaymentEventId != null ? paymentEvent.id.ne(excludePaymentEventId) : null;
    }
}
