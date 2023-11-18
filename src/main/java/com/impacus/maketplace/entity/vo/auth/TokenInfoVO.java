package com.impacus.maketplace.entity.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfoVO {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
