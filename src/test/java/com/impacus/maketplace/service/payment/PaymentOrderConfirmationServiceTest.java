package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.service.payment.utils.PaymentOrderConfirmationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentOrder 주문 확정 테스트")
public class PaymentOrderConfirmationServiceTest {

    @InjectMocks
    private PaymentOrderConfirmationService paymentOrderConfirmationService;

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @Test
    @DisplayName("주문 확정 상태에서 True로 조회된다.")
    void isPaymentOrderConfirmed_isTrueWhenConfirmed() {
        // given
        Long paymentOrderId = 1L;
        PaymentOrder paymentOrder = PaymentOrder.builder()
                .id(paymentOrderId)
                .isPaymentDone(true)
                .confirmationDueAt(LocalDateTime.now().minusDays(2))
                .isConfirmed(true)
                .confirmedAt(LocalDateTime.now().minusDays(1))
                .build();

        when(paymentOrderRepository.findById(paymentOrderId)).thenReturn(Optional.of(paymentOrder));

        // when
        boolean result = paymentOrderConfirmationService.isPaymentOrderConfirmed(paymentOrderId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("주문 미확정 상태에서 주문 확정 조건을 만족하고 있는 경우에 주문 확정으로 업데이트 되면서 true로 조회된다.")
    void isPaymentOrderConfirmed_trueWhenIsNotConfirmedAndConditionsAreMet() {
        // given
        Long paymentOrderId = 1L;
        PaymentOrder paymentOrder = PaymentOrder.builder()
                .id(paymentOrderId)
                .isPaymentDone(true)
                .confirmationDueAt(LocalDateTime.now().minusDays(2))
                .isConfirmed(false)
                .build();

        when(paymentOrderRepository.findById(paymentOrderId)).thenReturn(Optional.of(paymentOrder));

        // when
        boolean result = paymentOrderConfirmationService.isPaymentOrderConfirmed(paymentOrderId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("주문 미확정 상태에서 조건도 불만족하고 있을 경우 false로 조회된다.")
    void isPaymentOrderConfirmed_falseWhenIsNotConfirmedAndConditionsAreNotMet() {
        // given
        Long paymentOrderId = 1L;
        PaymentOrder paymentOrder = PaymentOrder.builder()
                .id(paymentOrderId)
                .isPaymentDone(true)
                .confirmationDueAt(LocalDateTime.now().plusDays(1))
                .isConfirmed(false)
                .build();

        when(paymentOrderRepository.findById(paymentOrderId)).thenReturn(Optional.of(paymentOrder));

        // when
        boolean result = paymentOrderConfirmationService.isPaymentOrderConfirmed(paymentOrderId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주문 미확정 상태에서 결제도 완료되지 않은 경우 false로 조회된다.")
    void isPaymentOrderConfirmed_falseWhenPaymentIsNotCompleted() {
        // given
        Long paymentOrderId = 1L;
        PaymentOrder paymentOrder = PaymentOrder.builder()
                .id(paymentOrderId)
                .isPaymentDone(false)
                .build();

        when(paymentOrderRepository.findById(paymentOrderId)).thenReturn(Optional.of(paymentOrder));

        // when
        boolean result = paymentOrderConfirmationService.isPaymentOrderConfirmed(paymentOrderId);

        // then
        assertThat(result).isFalse();
    }


}
