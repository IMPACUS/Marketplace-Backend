package com.impacus.maketplace.external.payment;

public class PaymentApiInit {
    private static String sendApiUrl;
    private static String sendImpKey;
    private static String sendImpSecret;

    public static String getSendApiUrl() {return sendApiUrl;}
    public static String getImpApiKey() {return sendImpKey;}
    public static String getImpApiSecret() {
        return sendImpSecret;
    }


    public final static String PAYMENT_API_URL_RETRIEVE_TOKEN_01 = "/users/getToken";
    public final static String PAYMENT_API_URL_RETRIEVE_BILLING_KEY_02 = "/subscribe/customers/";
    public final static String PAYMENT_API_URL_PAY_ONETIME_03 = "/subscribe/payments/onetime";
    public final static String PAYMENT_API_URL_PAY_AGAIN_04 = "/subscribe/payments/again";
    public final static String PAYMENT_API_URL_PAY_AGAIN_05 = "/subscribe/payments/cancel";

    public static void saveLog() {
        try {
            /*
            [JHK] TO-DO 로그 저장
             */
        }   catch (Exception e) {

        }

    }

}
