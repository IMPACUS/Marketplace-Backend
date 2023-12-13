package com.impacus.maketplace.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
public record TokenInfoVO(String grantType, String accessToken, String refreshToken) {

}
