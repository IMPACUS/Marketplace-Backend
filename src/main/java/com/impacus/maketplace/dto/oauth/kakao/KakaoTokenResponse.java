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

    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;
}
