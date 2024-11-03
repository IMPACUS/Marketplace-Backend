package com.impacus.maketplace.controller.oauth;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.oauth.request.OauthCodeDTO;
import com.impacus.maketplace.dto.oauth.request.OauthTokenDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.service.oauth.OAuthService;
import com.impacus.maketplace.service.oauth.OAuthServiceFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {
    private final OAuthServiceFactory oAuthServiceFactory;

    /**
     * 소셜 로그인 API
     *
     * @return
     */
    @PostMapping("/code")
    public ApiResponseEntity<OauthLoginDTO> executeSocialLoginForCode(@Valid @RequestBody OauthCodeDTO dto) {
        OAuthService oAuthService = oAuthServiceFactory.getService(dto.getOauthProviderType());
        OauthLoginDTO response = oAuthService.login(dto);
        return ApiResponseEntity
                .<OauthLoginDTO>builder()
                .data(response)
                .message("소셜 로그인 성공")
                .build();
    }

    /**
     * 소셜 로그인 API
     *
     * @return
     */
    @PostMapping("/token")
    public ApiResponseEntity<OauthLoginDTO> executeSocialLoginForToken(@Valid @RequestBody OauthTokenDTO dto) {
        OAuthService oAuthService = oAuthServiceFactory.getService(dto.getOauthProviderType());
        OauthLoginDTO response = oAuthService.login(dto);
        return ApiResponseEntity
                .<OauthLoginDTO>builder()
                .data(response)
                .message("소셜 로그인 성공")
                .build();
    }
}
