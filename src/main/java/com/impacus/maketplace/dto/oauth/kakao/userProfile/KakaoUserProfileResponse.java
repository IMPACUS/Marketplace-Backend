package com.impacus.maketplace.dto.oauth.kakao.userProfile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KakaoUserProfileResponse {
    private Long id;

    @JsonProperty(value = "has_signed_up")
    private Boolean hasSignedUp;

    @JsonProperty(value = "connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty(value = "synched_at")
    private LocalDateTime synchedAt;

    private JsonObject properties;

    @JsonProperty(value = "kakao_account")
    private KakaoAccountResponse kakaoAccount;

    @JsonProperty(value = "for_partner")
    private Object forPartner;
}
