package com.impacus.maketplace.common.enumType;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    NONE(1, "NONE"),
    KAKAO_PAY(2, "카카오 페이"),
    NAVER_PAY(3, "네이버 페이"),
    TOSS_PAY(4, "토스 페이"),
    ACCOUNT(5, "계좌 간편 결제"),
    CARD(6, "일반 결제");

    private final int code;
    private final String value;

    public static PaymentMethod fromCode(int code) {
        return Arrays.stream(PaymentMethod.values()).filter(t -> t.getCode() == code).findFirst()
                .orElseThrow(() -> new CustomException(ErrorType.UNKNOWN));
    }
}
