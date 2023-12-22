package com.impacus.maketplace.external.payment;

import com.impacus.maketplace.dto.ResultDto;
import com.impacus.maketplace.dto.payment.request.PaymentRequest;

import java.util.Map;
/**
 * @author JHK
 * date : 2023. 12. 22.
 * description : 다양한 결제 유형 Interface
 *
 *
 * 1. 토큰 조회
 * 2. 빌링키등록
 * 3. 최초 결제
 * 4. 재결제
 * 5. 결제 취소
 */
public interface IfPayment {
    String retrieveToken() throws Exception;

    ResultDto registerBillingKey(PaymentRequest paymentRequest) throws Exception;

    ResultDto onetime(PaymentRequest paymentRequest) throws Exception;

    ResultDto again(PaymentRequest paymentRequest) throws Exception;

    ResultDto cancel(PaymentRequest paymentRequest) throws Exception;
}
