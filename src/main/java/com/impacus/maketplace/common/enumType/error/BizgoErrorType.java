package com.impacus.maketplace.common.enumType.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BizgoErrorType implements ErrorType {
    COMMUNICATION_ERROR("001_COMMUNICATION_ERROR", "비즈고 인증 토큰값 리턴 통신에러입니다."),
    UNAUTHORIZED("002_UNAUTHORIZED", "비즈고 인증 권한이 없습니다(401)"),
    UNKNOWN_ERROR("003_UNKNOWN_ERROR", "비즈고 서버에러입니다.(500)"),
    MSG_ERROR("004_MSG_ERROR", "비즈고 문자 전송 에러입니다.(400)"),
    MSG_LIMIT_EXCEED("005_MSG_LIMIT_EXCEED", "비즈고 문자 전송 길이 초과입니다.(2000바이트)"),
    KAKAO_ERROR("006_KAKAO_ERROR", "카카오톡 전송 에러입니다.(400)"),
    KAKAO_NO_NAME("007_KAKAO_NO_NAME", "이름 전송 필수입니다."),
    KAKAO_NO_ITEM_NAME("008_KAKAO_NO_ITEM_NAME", "상품명 전송 필수입니다."),
    KAKAO_NO_ORDER_NUM("009_ KAKAO_NO_ORDER_NUM", "주문번호 전송 필수입니다."),
    KAKAO_NO_COURIER("010_KAKAO_NO_COURIER", "택배사 전송 필수입니다."),
    KAKAO_NO_INVOICE("011_KAKAO_NO_INVOICE", "송장번호 전송 필수입니다."),
    FCM_ERROR("012_FCM_ERROR", "push 메세지 전송 실패입니다.");

    private final String code;
    private final String msg;
}
