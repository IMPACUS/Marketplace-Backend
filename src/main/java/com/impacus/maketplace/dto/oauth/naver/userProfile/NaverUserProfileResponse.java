package com.impacus.maketplace.dto.oauth.naver.userProfile;

import lombok.Getter;

@Getter
public class NaverUserProfileResponse {
    private String resultcode;
    private String message;
    private NaverUserResponse response;
}
