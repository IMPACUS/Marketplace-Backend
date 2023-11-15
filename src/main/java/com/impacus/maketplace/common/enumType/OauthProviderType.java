package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthProviderType {
    NONE(1, "자체 로그인"),
    GOOGLE(2, "구글 로그인"),
    KAKAO(3, "카카오 로그인"),
    NAVER(4, "네이버 로그인"),
    APPLE(5, "애플 로그인");

    private final int code;
    private final String value;
}
