package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.BankCode;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    @QueryProjection
    public DetailedSellerEntryDTO(
            Long id,
            String marketName,
            String contactName,
            String email,
            String contactNumber,
            String businessRegistrationNumber,
            String mailOrderBusinessReportNumber,
            String businessAddress,
            BankCode bankCode,
            String accountName,
            String accountNumber
    ) {
        this.id = id;
        this.marketName = marketName;
        this.contactName = contactName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.mailOrderBusinessReportNumber = mailOrderBusinessReportNumber;
        this.businessAddress = businessAddress;
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
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
