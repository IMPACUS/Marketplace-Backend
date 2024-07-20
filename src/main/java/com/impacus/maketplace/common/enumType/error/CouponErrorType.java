package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponErrorType implements ErrorType{

    INVALID_COUPON_FORMAT("040_INVALID_COUPON_FORMAT", "유효하지 않은 쿠폰입니다.\n쿠폰코드를 다시 한번 확인해주세요"),
    INVALID_COUPON_UPDATE("040_1_INVALID_COUPON_UPDATE", "수정 불가능한 쿠폰입니다."),
    NOT_EXISTED_ISSUANCE("041_NOT_EXISTED_ISSUANCE", "존재하지 않는 발급 데이터입니다."),
    NOT_EXISTED_COUPON("042_NOT_EXISTED_COUPON", "존재하지 않는 쿠폰 데이터입니다."),
    INVALID_ALARM("043_INVALID_ALARM", "유효하지 않는 알림 타입입니다."),
    INVALID_PERCENT("044_INVALID_PERCENT", "유효하지 않는 퍼센트 입니다."),
    DUPLICATED_COUPON("45_DUPLICATE_COUPON", "중복되는 쿠폰이 존재합니다."),
    INVALID_COUPON_REQUEST("46_INVALID_COUPON_REQUEST", "유효하지 않는 쿠폰 요청입니다."),
    INVALID_FIRST_COUNT("047_INVALID_FIRST_COUNT", "유효하지 않는 선착순 수 입니다."),
    INVALID_VALUE("048_INVALID_VALUE", "유효하지 않는 수 입니다."),
    DUPLICATED_COUPON_CODE("048_DUPLICATED_COUPON_CODE", "중복되는 쿠폰 코드입니다."),
    INVALID_COUPON_LIST("INVALID_COUPON_LIST","유효하지 않은 쿠폰 리스트 입니다."),
    INVALID_UPDATE_COUPON_LIST("INVALID_UPDATE_COUPON_LIST", "수정 불가능한 쿠폰이 선택 되었습니다.");

    private final String code;
    private final String msg;

}
