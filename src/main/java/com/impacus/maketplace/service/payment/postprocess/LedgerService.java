package com.impacus.maketplace.service.payment.postprocess;

import com.impacus.maketplace.entity.payment.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LedgerService {

    @Transactional
    public void ledgerUpdate(PaymentOrder paymentOrder) {

        
    }

}
