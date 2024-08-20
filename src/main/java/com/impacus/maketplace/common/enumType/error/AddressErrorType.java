package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AddressErrorType implements ErrorType {

    NOT_FOUND_MY_ADDRESS_BY_NAME("050_NOT_FOUND_MY_ADDRESS_BY_NAME", "해당 이름을 가진 사용자의 주소를 찾을 수 없습니다."),
    MAX_REGIST_ADDRESS("050_MAX_REGIST_ADDRESS", "최대 3개까지 주소지를 등록할 수 있습니다."),
    NOT_FOUND_MY_ADDRESS_BY_ID("050_NOT_FOUND_MY_ADDRESS_BY_ID", "해당 사용자로부터 해당 id를 가진 주소지를 찾을 수 없습니다."),
    DUPLICATE_NAME_ADDRESS("050_DUPLICATE_NAME_ADDRESS", "주소지 명칭이 중복되어 들어갈 수 없습니다.");

    private final String code;
    private final String msg;
}
