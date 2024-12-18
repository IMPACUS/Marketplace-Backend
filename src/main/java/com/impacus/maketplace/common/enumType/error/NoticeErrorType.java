package com.impacus.maketplace.common.enumType.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NoticeErrorType implements ErrorType {
    DATE_TIME_ERROR("001_DATE_TIME_ERROR", "시작 날짜는 끝 날짜보다 크거나 같을 수 없습니다."),
    NO_NOTICE_ID("001_NO_NOTICE_ID", "해당 notice id는 존재하지 않습니다.");

    private String code;
    private String msg;
}
