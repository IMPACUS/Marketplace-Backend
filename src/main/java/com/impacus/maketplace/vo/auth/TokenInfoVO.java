package com.impacus.maketplace.vo.auth;

import lombok.Builder;

@Builder
public record TokenInfoVO(String grantType, String accessToken, String refreshToken) {

    public static TokenInfoVO toVO(String grantType, String accessToken, String refreshToken) {
        return TokenInfoVO.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
