package com.impacus.maketplace.service;

import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.payment.request.PaymentRequest;
import com.impacus.maketplace.dto.payment.response.PaymentDTO;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.external.payment.IfPayment;
import com.impacus.maketplace.external.payment.PaymentCreditCard;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public PaymentDTO register(PaymentRequest paymentRequest) throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO();
        IfPayment payment = new PaymentCreditCard();
        payment.registerBillingKey(paymentRequest);


        return paymentDTO;
    }

    public PaymentDTO onetime(PaymentRequest paymentRequest) throws Exception {
        IfPayment payment = new PaymentCreditCard();
        payment.onetime(paymentRequest);
        return null;
    }
}
