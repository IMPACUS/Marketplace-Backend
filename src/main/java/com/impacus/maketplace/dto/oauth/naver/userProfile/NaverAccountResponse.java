package com.impacus.maketplace.dto.oauth.naver.userProfile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverAccountResponse {
    private String id;
    private String nickname;
    private String name;
    private String email;
    private String gender;
    private String age;
    private String birthday;

    @JsonProperty(value = "profile_image")
    private String profileImage;
    private String birthyear;
    private String mobile;
}
