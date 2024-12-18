package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminErrorType implements ErrorType {
    NOT_EXISTED_ADMIN_ID_NAME("001_NOT_EXISTED_ADMIN_ID_NAME", "존재하지 않는 id입니다."),
    DUPLICATED_ADMIN_ID_NAME("002_DUPLICATED_ADMIN_ID_NAME", "이미 등록된 adminIdName 입니다.");

    private final String code;
    private final String msg;
}
