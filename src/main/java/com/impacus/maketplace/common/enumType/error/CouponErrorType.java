package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponErrorType implements ErrorType{

    // INVALID_INPUT_VALUE_{}
    INVALID_INPUT_VALUE("040_INVALID_INPUT_VALUE", "유효하지 않는 입력값입니다."),
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

    // 쿠폰 코드
    INVALID_COUPON_FORMAT("041_INVALID_COUPON_FORMAT", "유효하지 않은 쿠폰입니다. 쿠폰코드를 다시 한번 확인해주세요"),
    DUPLICATED_COUPON_CODE("041_1_DUPLICATED_COUPON_CODE", "중복되는 쿠폰 코드입니다."),

    // 쿠폰 수정
    INVALID_COUPON_UPDATE("042_INVALID_COUPON_UPDATE", "수정 불가능한 쿠폰입니다."),

    // 쿠폰 발급 이력
    NOT_EXISTED_ISSUANCE("043_NOT_EXISTED_ISSUANCE", "존재하지 않는 발급 데이터입니다."),

    // 쿠폰 조회
    NOT_EXISTED_COUPON("044_NOT_EXISTED_COUPON", "존재하지 않는 쿠폰 데이터입니다."),
    NOT_EXISTED_USER_COUPON("044_1_NOT_EXISTED_USER_COUPON", "해당 사용자에게 발급된 쿠폰이 아닙니다."),

    // 쿠폰 상태
    IS_DELETED_COUPON("045_IS_DELETED_COUPON", "삭제된 쿠폰입니다."),
    IS_STOP_COUPON("045_1_IS_STOP_COUPON", "발급 중지된 쿠폰입니다."),
    END_FIRST_COUNT_COUPON("045_2_END_FIRST_COUNT_COUPON", "선착순 만료된 쿠폰입니다."),
    NOT_AVAILABLE_DOWNLOAD_USER_COUPON("045_3_NOT_AVAILABLE_DOWNLOAD_USER_COUPON", "아직 다운로드 받을 수 없습니다."),
    ALREADY_DOWNLOAD_USER_COUPON("045_4_ALREADY_DOWNLOAD_USER_COUPON", "이미 다운로드 받은 쿠폰입니다."),
    ALREADY_USED_USER_COUPON("045_5_ALREADY_USED_USER_COUPON", "이미 사용한 쿠폰입니다."),
    EXPIRED_USER_COUPON("045_6_EXPIRED_USER_COUPON", "만료 기간이 지난 쿠폰입니다."),

    // 쿠폰 등록 조건
    INVALID_REGISTER_EVENT_COUPON("046_INVALID_REGISTER_EVENT_COUPON", "이벤트형 쿠폰은 등록하실 수 없습니다."),
    INVALID_REGISTER_PERSISTENCE_COUPON("046_1_INVALID_REGISTER_PERSISTENCE_COUPON", "지속형 쿠폰은 등록하실 수 없습니다."),
    INVALID_REGISTER_ALREADY_ISSUE("046_2_INVALID_REGISTER_ALREADY_ISSUE", "해당 쿠폰을 이미 발급 받은 이력이 존재합니다."),

    // 쿠폰 다운로드 조건
    INVALID_DOWNLOAD_EVENT_COUPON("047_INVALID_DOWNLOAD_EVENT_COUPON", "이벤트형 쿠폰은 다운로드 받을 수 없습니다."),
    INVALID_DOWNLOAD_PERSISTENCE_COUPON("047_1_INVALID_DOWNLOAD_PERSISTENCE_COUPON", "지속형 쿠폰은 다운로드 받을 수 없습니다."),
    ALREADY_ISSUED_COUPON("047_2_ALREADY_ISSUED_COUPON", "이미 발급 받은 쿠폰입니다.");

    private final String code;
    private final String msg;
}
