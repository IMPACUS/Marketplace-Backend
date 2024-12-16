package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.service.api.PaymentEventInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventService implements PaymentEventInterface {

    private final PaymentEventRepository paymentEventRepository;

    @Override
    public Long findIdByPaymentId(String paymentId) {
        return paymentEventRepository.findIdByPaymentId(paymentId)
                .orElseThrow(() -> new CustomException(PaymentErrorType.NOT_FOUND_ORDER_ID));
    }
}
