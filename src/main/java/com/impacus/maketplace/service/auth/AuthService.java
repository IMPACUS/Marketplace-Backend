package com.impacus.maketplace.service.auth;

import NiceID.Check.CPClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.impacus.maketplace.common.enumType.certification.CPClientErrorCode;
import com.impacus.maketplace.common.enumType.certification.CertificationResultCode;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.TokenErrorType;
import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.auth.CertificationResult;
import com.impacus.maketplace.dto.auth.response.CertificationRequestDataDTO;
import com.impacus.maketplace.dto.auth.response.CheckMatchedPasswordDTO;
import com.impacus.maketplace.dto.user.CommonUserDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.redis.service.BlacklistService;
import com.impacus.maketplace.redis.service.CertificationRequestNumberService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.admin.AdminService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import security.CustomUserDetails;

import java.net.URISyntaxException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final BlacklistService blacklistService;
    private final PasswordEncoder passwordEncoder;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;
    private final AdminService adminService;
    private final NiceAPIService niceAPIService;
    private final CertificationRequestNumberService certReqNumberService;

    private static final String AUTHENTICATION_HEADER_TYPE = "Bearer";
    private static final String REQ_NUM_KEY = "REQ_SEQ";
    private static final String USER_ID_KEY = "USER_ID";

    @Value("${url.certification.success}")
    private String certificationSuccessURL;

    @Value("${url.certification.fail}")
    private String certificationFailureURL;


    /**
     * JWT 토큰을 재발급하는 함수
     *
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public UserDTO reissueToken(String accessToken, String refreshToken) {
        accessToken = StringUtils.parseGrantTypeInToken(AUTHENTICATION_HEADER_TYPE, accessToken);

        try {
            // 1. refresh token 유효성 확인
            TokenErrorType validateResult = tokenProvider.validateToken(refreshToken);
            boolean isLogOut = blacklistService.existsBlacklistByAccessToken(accessToken);
            if (validateResult != TokenErrorType.NONE) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, validateResult.getErrorType());
            } else if (isLogOut) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, CommonErrorType.LOGGED_OUT_TOKEN);
            }

            // 2. Access token 사용자 확인
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            UserType userType = SecurityUtils.getCurrentUserFromCustomUserDetails(customUserDetail);

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
        CommonUserDTO user = userService.findCommonUserByEmail(email);
        switch (user.getStatus()) {
            case DEACTIVATED -> throw new CustomException(UserErrorType.DEACTIVATED_USER);
            case SUSPENDED -> throw new CustomException(UserErrorType.SUSPENDED_USER);
        }

        // 1. JWT 토큰 재발급
        TokenInfoVO tokenInfoVO = tokenProvider.createToken(authentication);

        // 2. 소비자인 경우 출석 포인트 지급
        if (user.getType() == UserType.ROLE_CERTIFIED_USER) {
            greenLabelPointAllocationService.payGreenLabelPoint(
                    user.getUserId(),
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
        TokenErrorType tokenErrorType = tokenProvider.validateToken(accessToken);
        if (tokenErrorType != TokenErrorType.NONE) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, tokenErrorType.getErrorType());
        }

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

    /**
     * 사용자 인증 정보를 저장하는 함수
     *
     * @param result
     * @param encodeData
     */
    public HttpHeaders saveUserCertification(
            CertificationResultCode result,
            String encodeData,
            HttpSession session
    ) throws URISyntaxException {
        try {
            CPClient client = niceAPIService.getCPClient(encodeData);

            // 1. 암호화 데이터 분석
            String plainData = client.getPlainData();

            // 2. 세션값 확인
            // - 존재하지 않는 경우: 에러 발생
            // - 존재하는 경우: 삭제
            Long userId = Long.valueOf((Integer) session.getAttribute(USER_ID_KEY));
            String sessionReqNumber = (String) session.getAttribute(REQ_NUM_KEY);
            if (!certReqNumberService.existsCertificationRequestNumber(sessionReqNumber)) {
                throw new CustomException(UserErrorType.FAIL_TO_CERTIFICATION, CPClientErrorCode.NOT_MATCH_SESSION_NUMBER);
            } else {
                certReqNumberService.deleteCertificationRequestNumber(sessionReqNumber);
            }

            // 3. 데이터 추출
            HashMap mapresult = client.fnParse(plainData);
            CertificationResult certificationResult = new ObjectMapper().convertValue(mapresult, CertificationResult.class);
            writeCertificationLog(userId, certificationResult);

            // 4. 사용자 보안인증 정보 저장
            userService.saveCertification(userId, certificationResult);

            // 5. 성공 정보 전달
            HttpHeaders httpHeaders = new HttpHeaders();
            UriComponents redirectURL = UriComponentsBuilder
                    .fromUriString(certificationSuccessURL)
                    .queryParam("result", CertificationResultCode.SUCCESS.toString())
                    .build();

            httpHeaders.setLocation(redirectURL.toUri());
            return httpHeaders;
        } catch (Exception e) {
            HttpHeaders httpHeaders = new HttpHeaders();
            UriComponents redirectURL;

            // 실패 정보 전달
            if (e instanceof CustomException) {
                redirectURL = UriComponentsBuilder
                        .fromUriString(certificationFailureURL)
                        .queryParam("result", CertificationResultCode.FAIL.toString())
                        .queryParam("code", ((CustomException) e).getErrorType().getCode())
                        .queryParam("detail", ((CustomException) e).getDetail())
                        .build();
            } else {
                redirectURL = UriComponentsBuilder
                        .fromUriString(certificationFailureURL)
                        .queryParam("result", CertificationResultCode.FAIL.toString())
                        .queryParam("code", CommonErrorType.UNKNOWN.getCode())
                        .queryParam("detail", e.getMessage())
                        .build();
            }

            httpHeaders.setLocation(redirectURL.toUri());
            return httpHeaders;
        }
    }

    private void writeCertificationLog(Long userId, CertificationResult certificationResult) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("gender", certificationResult.getGender());
        jsonObject.addProperty("nationalInfo", certificationResult.getNationalInfo());
        jsonObject.addProperty("mobileCo", certificationResult.getMobileCo());
        jsonObject.addProperty("name", certificationResult.getName());
        jsonObject.addProperty("authType", certificationResult.getAuthType());

        LogUtils.writeInfoLog("saveUserCertification", jsonObject.toString());
    }

    public CertificationRequestDataDTO getCertificationRequestData() {
        return getCertificationRequestData(false);
    }

    /**
     * 본인인증 암호화 데이터 조회
     *
     * @return
     */
    public CertificationRequestDataDTO getCertificationRequestData(boolean isTest) {
        // CertificationRequestDataDTO 생성
        CertificationRequestDataDTO dto = niceAPIService.getRequestData(isTest);

        // CertificationRequestNumber 저장
        certReqNumberService.saveCertificationRequestNumber(dto.getReqNumber());

        return dto;
    }
}
