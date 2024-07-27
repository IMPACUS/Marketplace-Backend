package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponErrorType implements ErrorType{

    // INVALID_INPUT_VALUE_{}
    INVALID_INPUT_BENEFIT_VALUE("040_1_BENEFIT_VALUE", "혜택 구분 항목에 잘못된 입력이 들어왔습니다."),
    INVALID_INPUT_FIRST_COUNT("040_2_FIRST_COUNT", "선착순 항목에 잘못된 입력이 들어왔습니다."),
    INVALID_INPUT_EXPIRE_TIME_DAYS("040_3_EXPIRE_TIME_DAYS", "사용 기간 항목에 잘못된 입력이 들어왔습니다."),
    INVALID_INPUT_ISSUE_COVERAGE_SUB_CATEGORY_NAME("040_4_ISSUE_COVERAGE_SUB_CATEGORY_NAME", "발급 적용 범 항목에 잘못된 입력이 들어왔습니다."),
    INVALID_INPUT_USE_COVERAGE_SUB_CATEGORY_NAME("040_5_USE_COVERAGE_SUB_CATEGORY_NAME", "쿠폰 사용 범위 항목에 잘못된 입력이 들어왔습니다."),
    INVALID_INPUT_USE_STANDARD_VALUE("040_6_USE_STANDARD_VALUE", "쿠폰 사용가능 기준금액 항목에 잘못된 입력값이 들어왔습니다."),
    INVALID_INPUT_ISSUE_STANDARD_VALUE("040_7_ISSUE_STANDARD_VALUE", "쿠폰 발급 금액 기준 항목에 잘못된 입력값이 들어왔습니다."),
    INVALID_INPUT_PERIOD_START_AT("040_8_PERIOD_START_AT", "지정 기간 설정 항목에 잘못된 시작 날짜가 들어왔습니다."),
    INVALID_INPUT_PERIOD_END_AT("040_9_PERIOD_END_AT", "지정 기간 설정 항목에 잘못된 종료 날짜가 들어왔습니다."),
    INVALID_INPUT_NUMBER_OF_PERIOD("040_10_NUMBER_OF_PERIOD", "기간 설정 항목의 기간 내 N회 이상 주문 시 항목에 잘못된 입력 값이 들어왔습니다."),
    INVALID_INPUT_CODE("040_11_CODE", "잘못된 코드 입력이 들어왔습니다."),

    INVALID_INPUT_VALUE("040_INVALID_INPUT_VALUE", "유효하지 않는 입력값입니다."),
    INVALID_COUPON_FORMAT("041_INVALID_COUPON_FORMAT", "유효하지 않은 쿠폰입니다. 쿠폰코드를 다시 한번 확인해주세요"),
    INVALID_COUPON_UPDATE("042_INVALID_COUPON_UPDATE", "수정 불가능한 쿠폰입니다."),
    NOT_EXISTED_ISSUANCE("043_NOT_EXISTED_ISSUANCE", "존재하지 않는 발급 데이터입니다."),
    NOT_EXISTED_COUPON("044_NOT_EXISTED_COUPON", "존재하지 않는 쿠폰 데이터입니다."),
    DUPLICATED_COUPON("045_DUPLICATE_COUPON", "중복되는 쿠폰이 존재합니다."),
    INVALID_COUPON_REQUEST("046_INVALID_COUPON_REQUEST", "유효하지 않는 쿠폰 요청입니다."),
    DUPLICATED_COUPON_CODE("047_DUPLICATED_COUPON_CODE", "중복되는 쿠폰 코드입니다."),
    IS_DELETED_COUPON("048_IS_DELETED_COUPON", "삭제된 쿠폰입니다."),

    INVALID_COUPON_LIST("INVALID_COUPON_LIST","유효하지 않은 쿠폰 리스트 입니다."),
    INVALID_UPDATE_COUPON_LIST("INVALID_UPDATE_COUPON_LIST", "수정 불가능한 쿠폰이 선택 되었습니다.");

    private final String code;
    private final String msg;
}
