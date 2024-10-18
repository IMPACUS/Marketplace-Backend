package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.common.utils.StringUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IssueCouponHistoryDTO {
    private Long id;

    @ExcelColumn(headerName = "쿠폰번호")
    private String code;

    @ExcelColumn(headerName = "공급자")
    private String provider;

    @ExcelColumn(headerName = "쿠폰 설명")
    private String description;

    @ExcelColumn(headerName = "쿠폰명")
    private String name;

    @ExcelColumn(headerName = "아이디")
    private String userEmail;

    @ExcelColumn(headerName = "성함")
    private String userName;

    @ExcelColumn(headerName = "지급 상태")
    private UserCouponStatus userCouponStatus;

    @ExcelColumn(headerName = "지급 일자")
    private LocalDate issueDate;

    private BenefitType benefitType;  // 혜택 구분 [ 원, % ]
    private Long benefitValue;   // 혜택 금액 및 퍼센트

    @QueryProjection
    public IssueCouponHistoryDTO(Long id, String code, String description, String name, String userEmail, String userName, UserCouponStatus userCouponStatus, LocalDateTime createAt, BenefitType benefitType, Long benefitValue) {
        this.id = id;
        this.code = code;
        this.provider = "IMPACUS";
        this.description = description;
        this.name = name;
        this.userEmail = StringUtils.getEmailInfo(userEmail).getEmail();
        this.userName = userName;
        this.userCouponStatus = userCouponStatus;
        this.issueDate = createAt.toLocalDate();
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
    }
}