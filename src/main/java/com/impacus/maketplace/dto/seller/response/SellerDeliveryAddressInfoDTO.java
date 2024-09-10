package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.BankCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDeliveryAddressInfoDTO {
    private Long deliveryAddressId;
    private String generalAddress;
    private String generalDetailAddress;
    private String generalBusinessName;
    private String refundAddress;
    private String refundDetailAddress;
    private String refundBusinessName;
    private String refundAccountNumber;
    private String refundAccountName;
    private BankCode refundBankCode;
}
