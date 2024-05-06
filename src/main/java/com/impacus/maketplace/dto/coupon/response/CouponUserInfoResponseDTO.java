package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CouponUserInfoResponseDTO {

    private String userId;
    private String userName;
    private UserStatus status;
    private int userLevel;
    private Integer availablePoint;
    private Integer userScore;
    private String phoneNumber;
    private String registerAt;
    private String profilePath;
    private String userEmail;

    @QueryProjection

    public CouponUserInfoResponseDTO(String userId, String userName, UserStatus status, int userLevel, Integer availablePoint, Integer userScore, String phoneNumber, String registerAt, String profilePath, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.userLevel = userLevel;
        this.availablePoint = availablePoint;
        this.userScore = userScore;
        this.phoneNumber = phoneNumber;
        this.registerAt = registerAt;
        this.profilePath = profilePath;
        this.userEmail = userEmail;
    }
}
