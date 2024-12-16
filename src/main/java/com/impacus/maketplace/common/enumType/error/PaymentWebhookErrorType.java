package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentWebhookErrorType implements ErrorType{

    REQUIRED_PAYMENT_ID("INVALID_INPUT_VALUE", "Payment ID가 누락되어 들어왔습니다."),
    REQUIRED_TRANSACTION_ID("REQUIRED_TRANSACTION_ID", "Transaction ID가 누락되어 들어왔습니다."),
    REQUIRED_TOTAL_AMOUNT("REQUIRED_TOTAL_AMOUNT", "Confirm process를 위한 total amount가 누락되어 들어왔습니다."),

    NOT_FOUND_PAYMENT_EVENT_BY_PAYMENT_ID("NOT_FOUND_PAYMENT_EVENT_BY_PAYMENT_ID", "해당 Payment ID로 등록된 결제 이벤트를 찾을 수 없습니다."),
    NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID("NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID", "해당 Payment event id로 등록된 결제 주문을 찾을 수 없습니다."),

    MISMATCH_TOTAL_AMOUNT("MISMATCH_TOTAL_AMOUNT", "I'm Place에서 처리되는 결제 금액과 실제 결제 금액이 일치하지 않습니다."),

    INVALID_RPODUCT_OPTION("INVALID_RPODUCT_OPTION", "더이상 해당 구매 상품의 옵션이 유효하지 않습니다."),

    OUT_OF_STOCK("OUT_OF_STOCK", "구매하려는 상품의 재고가 부족합니다.");

    private final String code;
    private final String msg;
}
