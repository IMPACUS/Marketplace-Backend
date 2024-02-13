package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    // 400
    UNKNOWN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),
    ENCRYPTION_FAILED("001_ENCRYPTION_FAILED", "암호화에 실패하셨습니다."),
    DECRYPTION_FAILED("002_DECRYPTION_FAILED", "복호화에 실패하셨습니다."),
    DUPLICATED_EMAIL("003_DUPLICATED_EMAIL", "이미 등록되어 있는 이메일입니다."),
    REGISTERED_EMAIL_FOR_THE_OTHER("004_REGISTERED_EMAIL_FOR_THE_OTHER", "다른 서비스로 등록되어 있는 이메일입니다."),
    INVALID_PASSWORD("005_INVALID_PASSWORD", "유효하지 않은 비밀번호 입니다."),
    NOT_EXISTED_EMAIL("006_NOT_EXISTED_EMAIL", "존재하지 않는 회원입니다."),
    BLOCKED_EMAIL("007_BLOCKED_EMAIL", "차단된 사용자 입니다."),
    WRONG_PASSWORD("008_WRONG_PASSWORD", "틀린 비밀번호 입니다."),
    NOT_ALLOW_EMAIL("009_NOT_ALLOW_EMAIL", "이메일 사용이 허용이 되지 않은 사용자입니다."),
    FAIL_TO_CONVERT_FILE("010_FAIL_TO_CONVERT_FILE", "첨부된 파일을 File 타입으로 변환하는데 실패했습니다."),
    INVALID_PRODUCT("011_INVALID_PRODUCT", "유효하지 않은 상품 데이터입니다."),
    NOT_EXISTED_BRAND("012_NOT_EXISTED_BRAND", "존재하지 않는 브랜드입니다"),
    FAIL_TO_UPLOAD_FILE("013_FAIL_TO_UPLOAD_FILE", "S3로 파일을 첨부하는데 실패했습니다."),
    NOT_EXISTED_PRODUCT("014_NOT_EXISTED_PRODUCT", "존재하지 않는 상품입니다."),
    REGISTERED_WISHLIST("015_REGISTERED_WISHLIST", "이미 존재하는 찜 데이터 추가 요청입니다."),
    NOT_EXISTED_WISHLIST("016_NOT_EXISTED_WISHLIST", "존재하지 않는 찜 데이터입니다."),
    NOT_EXISTED_ATTACH_FILE("017_NOT_EXISTED_ATTACH_FILE", "존재하지 않는 첨부 파일입니다."),
    INVALID_REQUEST_DATA("018_INVALID_REQUEST_DATA", "유효하지 않은 데이터에 대한 요청입니다."),
    NOT_EXISTED_TEMPORARY_PRODUCT("019_NOT_EXISTED_TEMPORARY_PRODUCT", "임시 저장 데이터가 존재하지 않습니다."),
    NOT_EXISTED_SHOPPING_CART("019_NOT_EXISTED_SHOPPING_CART", "장바구니 데이터가 존재하지 않습니다."),

    //TODO: 3X번 Point 관련 된것 작성하겠습니다!
    NOT_EXISTED_POINT_MASTER("030_NOT_EXISTED_POINT_MASTER", "존재하지 않는 POINT 데이터 입니다."),

    //TODO: 4X번 부터 쿠폰 관련
    INVALID_COUPON_FORMAT("040_INVALID_COUPON_FORMAT", "유효하지 않은 쿠폰 데이터 입니다."),

    // 401
    INVALID_TOKEN("101_INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("102_EXPIRED_TOKEN", "만료된 토큰입니다."),

    // 403
    ACCESS_DENIED_EMAIL("301_ACCESS_DENIED_EMAIL", "접근 권한이 없는 사용자 요청입니다.");

    private final String code;
    private final String msg;
}
