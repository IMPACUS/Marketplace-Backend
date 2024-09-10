package com.impacus.maketplace.dto.payment.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressInfoDTO {
    private String name;
    private String receiver;
    private String postalCode;
    private String address;
    private String detailAddress;
    private String connectNumber;
    private String memo;
}
