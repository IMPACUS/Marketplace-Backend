package com.impacus.maketplace.external.payment;

import com.google.gson.JsonObject;
import com.impacus.maketplace.dto.ResultDto;
import com.impacus.maketplace.dto.payment.request.PaymentRequest;
import com.impacus.maketplace.external.HttpRequestHandler;
import net.minidev.json.JSONObject;

import java.util.Map;

public class PaymentCreditCard implements IfPayment    {
    @Override
    public String retrieveToken() throws Exception {
        /*
        [JHK] to-do 토큰 DB or Redis 조회 후 유효기간 지났을시 신규 토큰 발급
         */
        String token = "";

            String strUrl = PaymentApiInit.getSendApiUrl() + PaymentApiInit.PAYMENT_API_URL_RETRIEVE_TOKEN_01;
            JsonObject message = new JsonObject();
            message.addProperty("imp_key", PaymentApiInit.getImpApiKey());
            message.addProperty("imp_secret", PaymentApiInit.getImpApiSecret());
            HttpRequestHandler hrh = new HttpRequestHandler();
            JSONObject resultJson = hrh.sendHttp(strUrl, message, null, "POST");
            if (!resultJson.get("resultCd").equals("200"))	throw new Exception ("TOKEN GENERATE FAIL");

        return token;
    }

    @Override
    public ResultDto registerBillingKey(PaymentRequest paymentRequest) throws Exception {
        ResultDto resultDto = new ResultDto();
        /*
        [JHK] to-do
        결제 정보등록 (카드 등록 후 빌링키 발급 + 저장)
         */
        resultDto.setResultCd("200");
        return resultDto;
    }

    @Override
    public ResultDto onetime(PaymentRequest paymentRequest) throws Exception {
        ResultDto resultDto = new ResultDto();
        /*
        [JHK] to-do
        즉시 결제
         */
        resultDto.setResultCd("200");
        return resultDto;
    }

    @Override
    public ResultDto again(PaymentRequest paymentRequest) throws Exception {
        ResultDto resultDto = new ResultDto();
        /*
        [JHK] to-do
        기존 결제 이력 있을 시 빌링키로 결제
         */
        resultDto.setResultCd("200");
        return resultDto;
    }

    @Override
    public ResultDto cancel(PaymentRequest paymentRequest) throws Exception {
        ResultDto resultDto = new ResultDto();
        /*
        [JHK] to-do
        결제 취소
         */
        resultDto.setResultCd("200");
        return resultDto;
    }
}
