package com.impacus.maketplace.service.payment.api;

import com.impacus.maketplace.service.api.PaymentApiService;
import com.impacus.maketplace.service.payment.utils.PaymentOrderConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentApiServiceImpl implements PaymentApiService {

    private final PaymentOrderConfirmationService paymentOrderConfirmationService;

    @Override
    @Transactional
    public boolean isPaymentOrderConfirmed(Long paymentOrderId) {
        return paymentOrderConfirmationService.isPaymentOrderConfirmed(paymentOrderId);
    }

}
