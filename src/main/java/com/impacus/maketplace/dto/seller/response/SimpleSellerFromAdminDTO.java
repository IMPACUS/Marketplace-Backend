package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.BankCode;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SimpleSellerFromAdminDTO {
    private Long sellerId;
    private String marketName;
    private String contactName;
    private String email;
    private String phoneNumber;
    private LocalDateTime entryApprovedAt;
    private String representativeContact;
    private String businessAddress;
    private String businessRegistrationNumber;
    private String mailOrderBusinessReportNumber;
    private BankCode bankCode;
    private String accountName;
    private String accountNumber;
    private int charge;
    private UserStatus userStatus;

    private String businessRegistrationUrl;
    private String mailOrderBusinessReportUrl;
    private String bankBookUrl;

    public void setBusinessRegistrationUrl(String url) {
        this.businessRegistrationUrl = url;
    }

    public void setMailOrderBusinessReportUrl(String url) {
        this.mailOrderBusinessReportUrl = url;
    }

    public void setBankBookUrl(String url) {
        this.bankBookUrl = url;
    }
}
