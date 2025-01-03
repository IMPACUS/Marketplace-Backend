package com.impacus.maketplace.dto.oauth.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverDeleteResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;

    private String result;
}
