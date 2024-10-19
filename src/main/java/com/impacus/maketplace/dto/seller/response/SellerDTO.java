package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SellerDTO {
    private Long sellerId;

    @ExcelColumn(headerName = "브랜드명")
    private String brandName;

    @ExcelColumn(headerName = "판매자명")
    private String contactName;

    @ExcelColumn(headerName = "이메일")
    private String email;

    @ExcelColumn(headerName = "휴대폰 번호")
    private String phoneNumber;

    @ExcelColumn(headerName = "입점일시")
    private LocalDateTime entryApprovedAt;

    @ExcelColumn(headerName = "최근 활동")
    private LocalDateTime recentLoginAt;
}
