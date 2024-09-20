package com.impacus.maketplace.common.enumType;

import com.impacus.maketplace.common.enumType.error.AlarmErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.service.alarm.user.enums.AlarmEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MailType {
    AUTH("01", "auth_mail", "인증번호 메일 입니다."),
    PASSWORD("02", "password_mail", "패스워드 메일 입니다."),
    POINT_REDUCTION("10", "points_reduction_mail", "5개월 휴면 메일입니다."),
    DORMANCY_INFO("11", "dormancy_info_mail", "11개월 휴면 메일 입니다."),
    USER_DELETE("12", "user_delete_mail", "14개월 휴면 메일 입니다."),
    EMAIL_VERIFICATION("13", "auth_mail", "이메일 인증"),
    SELLER_APPROVE("14", "seller_approve_mail", "입점 승인 메일 입니다."),
    SELLER_REJECT("15", "seller_reject_mail", "입접 거절 메일 입니다."),
    SELLER_EMAIL_VERIFICATION("16", "auth_mail", "[판매자] 이메일 인증"),
    BRAND_SHOP_EMAIL("17", "alarm/brand_shop_mail", "브랜드 샵 알림 메일 전송 안내입니다."),
    ORDER_DELIVERY_EMAIL("18", "alarm/order_delivery_mail", "주문/배송 알림 메일 전송 안내입니다."),
    RESTOCK_EMAIL("19", "alarm/restock_mail", "재입고 알림 메일 전송 안내입니다."),
    REVIEW_EMAIL("20", "alarm/review_mail", "리뷰 알림 메일 전송 안내입니다."),
    SERVICE_CENTER_EMAIL("21", "alarm/service_center_mail", "고객센터 알림 메일 전송 안내입니다."),
    SHOPPING_BENEFITS_EMAIL("22", "alarm/shopping_benefits_mail", "쇼핑혜택 알림 메일 전송 안내입니다."),
    UNKNOWN("99", "", "");


    private final String code;
    private final String template;
    private final String subject;

    public static MailType fromCode(String code) {
        return Arrays.stream(MailType.values()).filter(t -> t.getCode().equals(code)).findFirst()
                .orElse(UNKNOWN);
    }

    public static MailType selectAlarm(AlarmEnum alarmEnum) {
        if (alarmEnum.name().equals("ORDER_DELIVERY")) return MailType.ORDER_DELIVERY_EMAIL;
        else if (alarmEnum.name().equals("RESTOCK")) return MailType.RESTOCK_EMAIL;
        else if (alarmEnum.name().equals("REVIEW")) return MailType.REVIEW_EMAIL;
        else if (alarmEnum.name().equals("SERVICE_CENTER")) return MailType.SERVICE_CENTER_EMAIL;
        else if (alarmEnum.name().equals("BRAND_SHOP")) return MailType.BRAND_SHOP_EMAIL;
        else if (alarmEnum.name().equals("SHOPPING_BENEFITS")) return MailType.SHOPPING_BENEFITS_EMAIL;
        else throw new CustomException(AlarmErrorType.INVALID_ENUM);
    }

}
