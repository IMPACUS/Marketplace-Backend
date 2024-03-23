package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CouponUserInfoResponse {

    private String userName;
    private UserStatus status;
    private String userLevel;
    private Integer availablePoint;
    private Integer userScore;
    private String phoneNumber;
    private String registerAt;
    private String profilePath;
    private String userEmail;

    @QueryProjection
    public CouponUserInfoResponse(String userName, UserStatus status, Integer availablePoint, Integer userScore, String phoneNumber, String registerAt, String profilePath, String userEmail) {
        this.userName = userName;
        this.status = status;
        this.availablePoint = availablePoint;
        this.userScore = userScore;
        this.phoneNumber = phoneNumber;
        this.registerAt = registerAt;
        this.profilePath = profilePath;
        this.userEmail = userEmail;
    }
}
