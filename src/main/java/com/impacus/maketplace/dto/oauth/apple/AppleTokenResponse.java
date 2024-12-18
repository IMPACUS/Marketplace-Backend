package com.impacus.maketplace.dto.oauth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.OSType;
import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import lombok.Getter;

@Getter
public class AppleTokenResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private String expiresIn;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "id_token")
    private String idToken;

    public OAuthTokenDTO toOAuthTokenDTO(OSType osType) {
        return new OAuthTokenDTO(accessToken, refreshToken, osType);
    }
}
