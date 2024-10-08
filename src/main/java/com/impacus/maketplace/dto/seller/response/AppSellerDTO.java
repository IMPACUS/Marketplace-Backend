package com.impacus.maketplace.dto.seller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AppSellerDTO {
    private Long sellerId;
    private String marketName;
    private String contactName;
    private String customerServiceNumber;
    private String businessEmail;
    private String businessDay;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String breakingTime;
}
