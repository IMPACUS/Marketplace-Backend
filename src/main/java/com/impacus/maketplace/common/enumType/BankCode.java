package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankCode {
    KB("01", "KB국민은행"),
    IBK("02", "IBK 기업은행"),
    SHINHAN("03", "신한은행"),
    SC("04", "SC제일은행");

    private final String code; // TODO 입출금 API code에 따라 변경
    private final String value;
}
