package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.dto.oauth.KakaoTokenInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KakaoOAuthAPIServiceFactory implements FallbackFactory<KakaoOAuthAPIService> {
    @Override
    public KakaoOAuthAPIService create(Throwable cause) {
        return new KakaoOAuthAPIService() {
            @Override
            public Object getTokenInfo(KakaoTokenInfoRequest request) {
                log.debug("★ Fallback reason was: " + cause.getMessage());
                return null;
            }
        };
    }
}
