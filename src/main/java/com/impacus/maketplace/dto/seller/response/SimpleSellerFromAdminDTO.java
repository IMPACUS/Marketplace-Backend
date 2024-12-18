package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.BankCode;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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
    private String password; // 비밀번호
    private String businessEmail; // 대표 이메일
    private String representativeName; // 대표자명
    private String logoImageId;

    public SimpleSellerFromAdminDTO(
            Long sellerId,
            String marketName,
            String contactName,
            String email,
            String phoneNumberPrefix,
            String phoneNumberSuffix,
            LocalDateTime entryApprovedAt,
            String representativeContact,
            String businessAddress,
            String businessRegistrationNumber,
            String mailOrderBusinessReportNumber,
            BankCode bankCode,
            String accountName,
            String accountNumber,
            int charge,
            UserStatus userStatus,
            String businessRegistrationUrl,
            String mailOrderBusinessReportUrl,
            String bankBookUrl,
            String password,
            String businessEmail,
            String representativeName,
            String logoImageId
    ) {
        this.sellerId = sellerId;
        this.marketName = marketName;
        this.contactName = contactName;
        this.email = email;
        this.phoneNumber = StringUtils.getPhoneNumber(phoneNumberPrefix, phoneNumberSuffix);
        this.entryApprovedAt = entryApprovedAt;
        this.representativeContact = representativeContact;
        this.businessAddress = businessAddress;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.mailOrderBusinessReportNumber = mailOrderBusinessReportNumber;
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.charge = charge;
        this.userStatus = userStatus;
        this.businessRegistrationUrl = businessRegistrationUrl;
        this.mailOrderBusinessReportUrl = mailOrderBusinessReportUrl;
        this.bankBookUrl = bankBookUrl;
        this.password = password;
        this.businessEmail = businessEmail;
        this.representativeName = representativeName;
        this.logoImageId = logoImageId;
    }
}
