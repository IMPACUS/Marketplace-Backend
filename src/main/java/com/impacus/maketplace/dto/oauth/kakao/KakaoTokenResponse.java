package com.impacus.maketplace.dto.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoTokenResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "expiresIn")
    private Integer expiresIn;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "refreshTokenExpiresIn")
    private Integer refreshTokenExpiresIn;
}
