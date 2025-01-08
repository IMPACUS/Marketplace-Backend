package com.impacus.maketplace.service.payment.utils;

import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PaymentOrderConfirmationService {

    public final int CONFIRMATION_DAYS = 7;

    private final PaymentOrderRepository paymentOrderRepository;

    /**
     * 주문 확정 조회
     * <p>
     * 필수 동작: 주문 확정 조회할 때 확정되지 않은 상태일 경우 처리 조건 확인
     */
    public boolean isPaymentOrderConfirmed(Long paymentOrderId) {
        return paymentOrderRepository.findById(paymentOrderId)
                .map(paymentOrder -> {
                    // 이미 확정된 상태면 true
                    if (Boolean.TRUE.equals(paymentOrder.getIsConfirmed())) {
                        return true;
                    }

                    // 아직 확정되지 않았지만, '확정 가능' 상태라면 confirm() 후 true
                    if (isReadyConfirmation(paymentOrder)) {
                        paymentOrder.confirm();
                        return true;
                    }

                    // 위 조건 불충족 시 false
                    return false;
                })
                .orElse(false); // PaymentOrder 미존재 시 false
    }

    /**
     * 주문 확정 처리 조건 확인
     * <p>
     * 1. 결제 완료
     * <p>
     * 2. 주문 확정 예상 날짜가 현재 날짜보다 이전인 경우
     */
    private boolean isReadyConfirmation(PaymentOrder paymentOrder) {
        return paymentOrder.getIsPaymentDone() && paymentOrder.getConfirmationDueAt().isBefore(LocalDateTime.now());
    }

    /**
     * 주문 확정 예상 날짜 업데이트
     * <p>
     * YY/MM/DD 00:00:00 기준으로 처리
     * <p>
     * 현재 날짜에서 7일 뒤 주문 확정
     */
    @Transactional
    public void updateConfirmationDueAt(PaymentOrder paymentOrder) {
        // 1. 현재 시각
        LocalDateTime now = LocalDateTime.now();

        // 2. 현재 날짜에서 7일을 더하여 LocalDate 계산
        LocalDate targetDate = now.toLocalDate().plusDays(CONFIRMATION_DAYS);

        // 3. targetDate에 대해 시간을 00:00:00으로 설정
        LocalDateTime confirmationDueAt = LocalDateTime.of(targetDate, LocalTime.MIDNIGHT);

        // 4. 엔티티에 반영
        paymentOrder.updateConfirmationDueAt(confirmationDueAt);
    }
}
