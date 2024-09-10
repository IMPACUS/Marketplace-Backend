package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IssueCouponHIstoryDTO {
    private Long couponId;
    private String code;
    private String provider;
    private String description;
    private String name;
    private String userEmail;
    private String userName;
    private UserCouponStatus userCouponStatus;
    private LocalDate issueDate;
    private BenefitType benefitType;  // 혜택 구분 [ 원, % ]
    private Long benefitValue;   // 혜택 금액 및 퍼센트

    @QueryProjection
    public IssueCouponHIstoryDTO(Long couponId, String code, String description, String name, String userEmail, String userName, UserCouponStatus userCouponStatus, LocalDateTime createAt, BenefitType benefitType, Long benefitValue) {
        this.couponId = couponId;
        this.code = code;
        this.provider = "IMPACUS";
        this.description = description;
        this.name = name;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userCouponStatus = userCouponStatus;
        this.issueDate = createAt.toLocalDate();
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
    }
}