package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.BankCode;
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
    private String logoImageUrl;
    private Integer chargePercent;

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
