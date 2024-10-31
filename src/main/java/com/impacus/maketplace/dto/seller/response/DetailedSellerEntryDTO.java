package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.BankCode;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.utils.StringUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailedSellerEntryDTO {
    private Long id;
    private String marketName;
    private String contactName;
    private String email;
    private String contactNumber;
    private String businessRegistrationNumber;
    private String mailOrderBusinessReportNumber;
    private String businessAddress;
    private BankCode bankCode;
    private String accountName;
    private String accountNumber;
    private String businessRegistrationUrl;
    private String mailOrderBusinessReportUrl;
    private String bankBookUrl;
    private String logoImageUrl;
    private Integer chargePercent;
    private EntryStatus entryStatus;

    @QueryProjection
    public DetailedSellerEntryDTO(
            Long id,
            String marketName,
            String contactName,
            String email,
            String phoneNumberSuffix,
            String phoneNumberPrefix,
            String businessRegistrationNumber,
            String mailOrderBusinessReportNumber,
            String businessAddress,
            BankCode bankCode,
            String accountName,
            String accountNumber,
            String logoImageUrl,
            Integer chargePercent,
            EntryStatus entryStatus
    ) {
        this.id = id;
        this.marketName = marketName;
        this.contactName = contactName;
        this.email = email;
        this.contactNumber = StringUtils.getPhoneNumber(phoneNumberPrefix, phoneNumberSuffix);
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.mailOrderBusinessReportNumber = mailOrderBusinessReportNumber;
        this.businessAddress = businessAddress;
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.logoImageUrl = logoImageUrl;
        this.chargePercent = chargePercent;
        this.entryStatus = entryStatus;
    }

    public void setBusinessRegistrationUrl(String businessRegistrationUrl) {
        this.businessRegistrationUrl = businessRegistrationUrl;
    }

    public void setMailOrderBusinessReportUrl(String mailOrderBusinessReportUrl) {
        this.mailOrderBusinessReportUrl = mailOrderBusinessReportUrl;
    }

    public void setBankBookUrl(String bankBookUrl) {
        this.bankBookUrl = bankBookUrl;
    }

}
