package com.impacus.maketplace.dto.oauth.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import lombok.Getter;

@Getter
public class NaverTokenResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    @JsonProperty(value = "error")
    private String error;

    @JsonProperty(value = "error_description")
    private String errorDescription;

    public OAuthTokenDTO toOAuthTokenDTO() {
        return new OAuthTokenDTO(accessToken, refreshToken);
    }
}
