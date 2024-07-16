package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.BankCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SellerAdjustmentInfoDTO {
    private BankCode bankCode;
    private String accountName;
    private String accountNumber;
    private String bankBookUrl;
}
