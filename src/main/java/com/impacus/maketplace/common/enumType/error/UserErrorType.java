package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorType implements ErrorType {
    DEACTIVATED_USER("001_DEACTIVATED_USER", "탈퇴한 회원입니다."),
    SUSPENDED_USER("002_SUSPENDED_USER", "정지된 회원입니다."),
    NOT_EXISTED_USER("003_NOT_EXISTED_USER", "존재하지 않는 회원입니다."),
    FAIL_TO_ENCRYPT_CERTIFICATION("004_FAIL_TO_ENCRYPT_CERTIFICATION", "본인인증에 필요한 데이터를 암호화하는데 실패했습니다."),
    FAIL_TO_CERTIFICATION("005_FAIL_TO_CERTIFICATION", "본인인증에 실패했습니다."),
    DUPLICATED_EMAIL("003_DUPLICATED_EMAIL", "이미 등록되어 있는 이메일입니다."),
    REGISTERED_EMAIL_FOR_THE_OTHER("004_REGISTERED_EMAIL_FOR_THE_OTHER", "다른 서비스로 등록되어 있는 이메일입니다."),
    INVALID_PASSWORD("005_INVALID_PASSWORD", "유효하지 않은 비밀번호 입니다."),
    NOT_EXISTED_EMAIL("006_NOT_EXISTED_EMAIL", "존재하지 않는 회원입니다."),
    BLOCKED_EMAIL("007_BLOCKED_EMAIL", "차단된 사용자 입니다."),
    WRONG_PASSWORD("008_WRONG_PASSWORD", "틀린 비밀번호 입니다."),
    NOT_ALLOW_EMAIL("009_NOT_ALLOW_EMAIL", "이메일 사용이 허용이 되지 않은 사용자입니다."),
    DUPLICATED_PHONE_NUMBER("010_DUPLICATED_PHONE_NUMBER", "이미 존재하는 휴대폰 번호입니다."),
    NOT_EXISTED_OAUTH_TOKEN("011_NOT_EXISTED_OAUTH_TOKEN", "OAuth 토큰이 존재하지 않습니다"),
    NOT_EXISTED_CONSUMER("012_NOT_EXISTED_CONSUMER", "Consumer 가 존재하지 않습니다."),
    FAIL_TO_REJOIN_14("013_FAIL_TO_REJOIN_14", "탈퇴 후 14일 이내에는 재가입이 불가합니다."),
    DUPLICATED_CI("014_DUPLICATED_CI", "이미 등록된 CI 입니다.");

    private final String code;
    private final String msg;
}
