package com.impacus.maketplace.dto.coupon.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {

    private String userEmail;
    private String userLevel;
    private String phoneNumber;
    private String registerAt;
    private String userScore;
    private String availablePoint;
}
