package com.impacus.maketplace.service.auth;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.TokenErrorType;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.auth.response.CheckMatchedPasswordDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.redis.service.BlacklistService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.admin.AdminService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final BlacklistService blacklistService;
    private final PasswordEncoder passwordEncoder;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;
    private final AdminService adminService;

    private final static String AUTHENTICATION_HEADER_TYPE = "Bearer";

    /**
     * JWT 토큰을 재발급하는 함수
     *
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public UserDTO reissueToken(String accessToken, String refreshToken) {
        accessToken = StringUtils.parseGrantTypeInToken(AUTHENTICATION_HEADER_TYPE, accessToken);
        UserType userType = SecurityUtils.getCurrentUserType();

        try {
            // 1. refresh token 유효성 확인
            TokenErrorType validateResult = tokenProvider.validateToken(refreshToken);
            if (validateResult != TokenErrorType.NONE) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, validateResult.getErrorType());
            }

            // 2. Access token 사용자 확인
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();

            // 3. 토큰 재발급
            switch (userType) {
                case ROLE_CERTIFIED_USER:
                case ROLE_APPROVED_SELLER:
                    return reissueConsumerAndSellerToken(customUserDetail.getEmail(), authentication, userType);
                case ROLE_ADMIN:
                case ROLE_PRINCIPAL_ADMIN:
                case ROLE_OWNER:
                    return reissueAdminToken(customUserDetail.getEmail());
                default:
                    throw new CustomException(HttpStatus.UNAUTHORIZED, CommonErrorType.INVALID_TOKEN,
                            "토큰 재발급이 불가능한 권한입니다. " + userType.name());
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }


    public UserDTO reissueConsumerAndSellerToken(String email, Authentication authentication, UserType userType) {
        User user = userService.validateAndFindUser(email, userType);

        // 1. JWT 토큰 재발급
        TokenInfoVO tokenInfoVO = tokenProvider.createToken(authentication);

        // 2. 소비자인 경우 출석 포인트 지급
        if (user.getType() == UserType.ROLE_CERTIFIED_USER) {
            greenLabelPointAllocationService.payGreenLabelPoint(
                    user.getId(),
                    PointType.CHECK,
                    RewardPointType.CHECK.getAllocatedPoints());
        }

        return new UserDTO(user, tokenInfoVO);
    }

    public UserDTO reissueAdminToken(String adminIdName) {
        AdminInfo admin = adminService.findAdminInfoBYAdminIdName(adminIdName);

        // 1. JWT 토큰 재발급
        TokenInfoVO tokenInfoVO = userService.getJwtTokenInfo(admin.getAdminIdName(), admin.getPassword());
        return new UserDTO(admin, tokenInfoVO);
    }

    /**
     * 로그아웃 함수
     *
     * @param accessToken
     */
    @Transactional
    public void logout(String accessToken) {
        accessToken = StringUtils.parseGrantTypeInToken(AUTHENTICATION_HEADER_TYPE, accessToken);
        Long expiration = tokenProvider.getExpiration(accessToken);
        if (expiration == null || expiration == 0L) {
            return;
        }

        blacklistService.saveBlacklist(accessToken, expiration);
    }

    /**
     * 사용자에게 저장된 비밀번호와 동일한지 확인하는 함수
     *
     * @param userType
     * @param password
     * @return
     */
    public CheckMatchedPasswordDTO checkIsPasswordMatch(Long userId, UserType userType, String password) {
        try {
            // 1. 비밀번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(CommonErrorType.INVALID_PASSWORD);
            }

            // 2. 일치하는지 확인
            User user = userService.findUserById(userId);
            switch (userType) {
                case ROLE_APPROVED_SELLER -> {
                    boolean isPasswordMatch = passwordEncoder.matches(password, passwordEncoder.encode(user.getPassword()));
                    return CheckMatchedPasswordDTO.toDTO(isPasswordMatch);
                }
            }

            return CheckMatchedPasswordDTO.toDTO(false);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
