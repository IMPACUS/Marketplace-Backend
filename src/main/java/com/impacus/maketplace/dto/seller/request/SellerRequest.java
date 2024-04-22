package com.impacus.maketplace.dto.seller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerRequest {
    private String email;
    private String password;
    private String contactName;
    private String contactNumber;
    private String marketName;
    private String customerServiceName;
}
