package com.impacus.maketplace.dto.oauth.naver.userProfile;

import lombok.Getter;

@Getter
public class NaverUserResponse {
    private String resultcode;
    private String message;
    private NaverAccountResponse response;
}
