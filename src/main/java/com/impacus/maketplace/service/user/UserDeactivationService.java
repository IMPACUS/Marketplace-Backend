package com.impacus.maketplace.service.user;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.dto.user.CommonUserDTO;
import com.impacus.maketplace.repository.user.UserRepository;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.oauth.OAuthService;
import com.impacus.maketplace.service.oauth.OAuthServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDeactivationService {
    private final UserService userService;
    private final OAuthServiceFactory oauthServiceFactory;
    private final UserRepository userRepository;

    /**
     * 사용자 탈퇴 요청 처리 함수
     *
     * @param email
     */
    @Transactional
    public void deactivateConsumer(String email) {
        CommonUserDTO userDTO = userService.findCommonUserByEmail(email);
        OauthProviderType oauthProviderType = userDTO.getOauthProviderType();

        // 1. 연동 해제
        if (oauthProviderType != OauthProviderType.NONE) {
            OAuthService oAuthService = oauthServiceFactory.getService(oauthProviderType);
            oAuthService.unlink(userDTO.getUserId());
        }

        // 2. 비활성화 처리
        userRepository.deactivateConsumer(userDTO.getUserId());
    }

    /**
     * 탈퇴 30일 이후 삭제 처리 함수
     *
     * @param userId
     */
    public void deleteConsumer(Long userId) {
        // User, UserConsent, UserRole, UserStatusInfo, Consumer, OAuthToken
        // GreenLabelPoint, GreenLabelPointAllocation, LevelAchievement, LevelPointMaster
        userRepository.deleteConsumer(userId);
    }

}
