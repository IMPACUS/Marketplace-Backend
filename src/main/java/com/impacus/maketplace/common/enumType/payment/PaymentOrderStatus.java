package com.impacus.maketplace.common.enumType.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentOrderStatus {

    NOT_STARTED("NOT_STARTED", "사용자가 결제하는 단계"),
    EXECUTING("EXECUTING", "결제 승인 처리 단계"),
    SUCCESS("SUCCESS", "결제 승인 성공"),
    FAILURE("FAILURE", "결제 승인 실패"),
    UNKNOWN("UNKNOWN", "알 수 없는 오류");

    private final String code;
    private final String value;
}
