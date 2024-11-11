package com.impacus.maketplace.service.oauth.naver;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.oauth.naver.NaverTokenResponse;
import com.impacus.maketplace.dto.oauth.naver.userProfile.NaverUserResponse;
import com.impacus.maketplace.dto.oauth.request.OauthCodeDTO;
import com.impacus.maketplace.dto.oauth.request.OauthTokenDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.service.oauth.CustomOauth2UserService;
import com.impacus.maketplace.service.oauth.OAuthService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverOAuthService implements OAuthService {
    private final NaverOAuthAPIService naverOAuthAPIService;
    private final NaverCommonAPIService naverCommonAPIService;
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtTokenProvider tokenProvider;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OauthCodeDTO dto) {
        if (dto.getState() == null) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "Naver 로그인인 경우, state가 null일 수 없습니다.");
        }

        // 1. Naver 토큰 발급
        NaverTokenResponse tokenResponse = naverOAuthAPIService.getNaverToken(
            "authorization_code", 
                        clientId, 
                        clientSecret,
                        dto.getCode(),
                        dto.getState()
        );

        OauthTokenDTO tokenRequestDTO = OauthTokenDTO.toDTO(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                dto.getOauthProviderType()
        );

        return login(tokenRequestDTO);


    }

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OauthTokenDTO dto) {
        // 1. 사용자 정보 요청
        NaverUserResponse userResponse = naverCommonAPIService.getUser(
                String.format("Bearer %s", dto.getAccessToken())
        );

        // 2. 회원가입/로그인
        OAuthAttributes attribute = OAuthAttributes.builder()
                .name(userResponse.getResponse().getName())
                .email(userResponse.getResponse().getEmail())
                .oAuthProvider(dto.getOauthProviderType())
                .build();
        User user = customOauth2UserService.saveOrUpdate(attribute);
        Authentication auth = tokenProvider.createAuthenticationFromUser(user, UserType.ROLE_CERTIFIED_USER);
        TokenInfoVO token = tokenProvider.createToken(auth);

        return OauthLoginDTO.of(
                user,
                false,
                token
        );
    }

    /**
     * 소셜 로그인 토큰 재발급
     *
     * @param memberId
     */
    @Override
    public void reissue(Long memberId) {

    }

    /**
     * 소셜 로그인 연동해제
     */
    @Override
    public void unlink() {

    }
}
