package com.impacus.maketplace.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoTokenInfoRequest {

    @JsonProperty(value = "grant_type")
    private String grantType;

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "redirect_uri")
    private String redirectUri;
    
    private String code;

    @JsonProperty(value = "client_secret")
    private String clientSecret;
}
