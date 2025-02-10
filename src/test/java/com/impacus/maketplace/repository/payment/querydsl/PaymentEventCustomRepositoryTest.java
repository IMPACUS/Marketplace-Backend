package com.impacus.maketplace.repository.payment.querydsl;

import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.enumType.payment.PaymentType;
import com.impacus.maketplace.config.EmptyAuditingTestConfig;
import com.impacus.maketplace.config.QuerydslConfig;
import com.impacus.maketplace.config.RedisConfig;
import com.impacus.maketplace.config.RedisTestConfig;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.payment.querydsl.dto.PaymentEventPeriodWithOrdersDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PaymentEventCustomRepositoryImpl.class, QuerydslConfig.class, EmptyAuditingTestConfig.class, RedisConfig.class, RedisTestConfig.class})
class PaymentEventCustomRepositoryTest {

    @Autowired
    private PaymentEventRepository paymentEventRepository;

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private PaymentEventCustomRepository paymentEventCustomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private static final LocalDateTime NOW = LocalDate.of(2024, 12, 15).atStartOfDay();

    @BeforeEach
    @Transactional
    void setup() {
        /**
         * 주단위 주문 3건, 월단위 주문 3건, 2주 사이 주문 3건
         * 각각 2건은 결제 성공한 주문 이벤트, 1건은 결제 완료가 되지 않은 주문 이벤트
         */
        for (int i = 0; i < 9; i++) {
            PaymentEvent paymentEvent = PaymentEvent.builder()
                    .buyerId(1L)
                    .isPaymentDone((i + 1) % 3 != 0)
                    .idempotencyKey("test_idempotency_key")
                    .paymentId("test_payment_id")
                    .type(PaymentType.NORMAL)
                    .orderName("test_order_name-" + i)
                    .method(PaymentMethod.CARD)
                    .pspRawData(null)
                    .approvedAt(generateApprovedAt(i))
                    .build();

            PaymentEvent savedPaymentEvent = paymentEventRepository.save(paymentEvent);
            // 1만원 상품 2개, 2만원 상품 1개
            for (int j = 0; j < 2; j++) {
                PaymentOrder paymentOrder = PaymentOrder.builder()
                        .paymentEventId(savedPaymentEvent.getId())
                        .sellerId(1L)
                        .productId((2 * savedPaymentEvent.getId()) + j)
                        .productOptionHistoryId((2 * savedPaymentEvent.getId()) + j)
                        .quantity((long) (2 - j))
                        .paymentId(savedPaymentEvent.getPaymentId())
                        .amount(10000L * (2 - j))
                        .ecoDiscount(1000L)
                        .greenLabelDiscount(100L)
                        .couponDiscount(1000L)
                        .commissionPercent(10)
                        .status(PaymentOrderStatus.SUCCESS)
                        .ledgerUpdated(false)
                        .walletUpdated(false)
                        .isPaymentDone(savedPaymentEvent.getIsPaymentDone())
                        .failedCount(0)
                        .threshold(5)
                        .confirmationDueAt(LocalDateTime.now())
                        .isConfirmed(savedPaymentEvent.getIsPaymentDone())
                        .confirmedAt(savedPaymentEvent.getApprovedAt())
                        .build();

                paymentOrderRepository.save(paymentOrder);
            }
        }
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    @DisplayName("6일 전 paymentEvent 3개의 paymentOrder가 전부 올바르게 매핑된다.")
    void paymentEventMappingPaymentOrderById() {
        // given
        Long userId = 1L;
        LocalDate startDate = NOW.minusDays(6).toLocalDate();
        LocalDate endDate = NOW.toLocalDate();

        // when
        List<PaymentEventPeriodWithOrdersDTO> result = paymentEventCustomRepository.findPaymentEventsWithOrdersInPeriod(userId, startDate, endDate, null);

        // then
        assertThat(result.size()).isEqualTo(3L);
    }
    private LocalDateTime generateApprovedAt(int i) {
        if (i < 3) {
            // 6일 전
            return NOW.minusDays(6);
        } else if (i < 6) {
            // 이번 달 1일 (자정)
            return NOW.withDayOfMonth(1);
        } else {
            // 12일 전 (2주 이내)
            return NOW.minusDays(12);
        }
    }
}