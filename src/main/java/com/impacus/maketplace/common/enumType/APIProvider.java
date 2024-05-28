package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum APIProvider {
    PORTONE("01", 86400L, 604800L);

    private final String code;
    private final Long accessTokenTTLInSeconds;
    private final Long refreshTokenTTLInSeconds;
}
