package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorType implements ErrorType {
    // 400
    UNKNOWN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),
    ENCRYPTION_FAILED("001_ENCRYPTION_FAILED", "암호화에 실패하셨습니다."),
    DECRYPTION_FAILED("002_DECRYPTION_FAILED", "복호화에 실패하셨습니다."),
    FAIL_TO_SEND_SMS("003_FAIL_TO_SEND_SMS", "SMS 전송에 실패했습니다."),
    CONTENTS_CONTAINS_PROFANITY("004_CONTENTS_CONTAINS_PROFANITY", "내용에 비속어가 포함되어 있습니다."),
    FAIL_TO_CONVERT_FILE("010_FAIL_TO_CONVERT_FILE", "첨부된 파일을 File 타입으로 변환하는데 실패했습니다."),
    FAIL_TO_CREATE_EXCEL("011_FAIL_TO_CREATE_EXCEL", "엑셀 생성하는데 실패했습니다."),
    FAIL_TO_UPLOAD_FILE("013_FAIL_TO_UPLOAD_FILE", "S3로 파일을 첨부하는데 실패했습니다."),
    OPEN_API_REQUEST_FAIL("014_OPEN_API_REQUEST_FAIL", "OPEN API 요청에 실패했습니다."),
    NOT_EXISTED_ATTACH_FILE("017_NOT_EXISTED_ATTACH_FILE", "존재하지 않는 첨부 파일입니다."),
    INVALID_REQUEST_DATA("018_INVALID_REQUEST_DATA", "유효하지 않은 데이터에 대한 요청입니다."),
    FAIL_TO_APPLE_LOGIN("020_FAIL_TO_APPLE_LOGIN", "apple 로그인에 실패하셨습니다."),
    FAIL_TO_CONVERT_MAP("021_FAIL_TO_CONVERT_MAP", "map 변환하는데 실패하셨습니다."),
    INVALID_SMS("022_INVALID_SMS", "유효하지 않는 SMS 데이터입니다."),
    INVALID_THUMBNAIL("024_INVALID_THUMBNAIL", "유효하지 않은 데이터에 대한 요청입니다."),
    FAIL_TO_SEND_EMAIL("32_FAIL_TO_SEND_EMAIL", "이메일 전송에 실패하였습니다."),
    INVALID_ALARM("043_INVALID_ALARM", "유효하지 않는 알림 타입입니다."),
    INVALID_ID("044_INVALID_ID", "유효하지 않은 ID 입니다."),
    INVALID_END_DATE("060_INVALID_END_DATE", "종료날짜는 시작날짜보다 이전일 수 없습니다."),

    // 401
    INVALID_TOKEN("101_INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("102_EXPIRED_TOKEN", "만료된 토큰입니다."),
    LOGGED_OUT_TOKEN("103_LOGGED_OUT_TOKEN", "로그아웃된 토큰입니다."),

    // 403
    ACCESS_DENIED_ACCOUNT("301_ACCESS_DENIED_ACCOUNT", "접근 권한이 없는 요청입니다.");

    private final String code;
    private final String msg;
}
