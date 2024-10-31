package com.impacus.maketplace.dto.coupon.api;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.utils.StringUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AlarmCouponDTO {
    private Long userId;            // 사용자 아이디
    private String userName;        // 사용자 이름
    private String phoneNumber;     // 사용자 핸드폰 번호
    private String email;           // 사용자 이메일 주소
    private String couponName;      // 사용자 쿠폰명
    private BenefitType benefitType;    // 쿠폰 적용 방식
    private Long benefitValue;      // 적용 값 [%, 원]
    private LocalDate expiredAt;    // 쿠폰 만료 날짜

    @QueryProjection
    public AlarmCouponDTO(Long userId, String userName, String phoneNumberPrefix, String phoneNumberSuffix, String email, String couponName, BenefitType benefitType, Long benefitValue, LocalDate expiredAt) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = StringUtils.getPhoneNumber(phoneNumberPrefix, phoneNumberSuffix);
        this.email = email;
        this.couponName = couponName;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
        this.expiredAt = expiredAt;
    }
}
