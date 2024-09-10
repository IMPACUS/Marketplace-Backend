package com.impacus.maketplace.dto.seller.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SellerDTO {
    private Long sellerId;

    private String brandName;

    private String contactName;

    private String email;

    private String phoneNumber;

    private LocalDateTime entryApprovedAt;

    private LocalDateTime recentLoginAt;
}
