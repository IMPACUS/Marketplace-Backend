package com.impacus.maketplace.controller.auth;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.auth.response.CertificationRequestDataDTO;
import com.impacus.maketplace.dto.seller.request.CreateSellerDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerFromSellerDTO;
import com.impacus.maketplace.dto.user.request.LoginDTO;
import com.impacus.maketplace.dto.user.request.RefreshTokenDTO;
import com.impacus.maketplace.dto.user.request.SignUpDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import com.impacus.maketplace.service.auth.CertificationService;
import com.impacus.maketplace.service.seller.CreateSellerService;
import com.impacus.maketplace.service.user.SignUpService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final SignUpService signUpService;
    private final UserService userService;
    private final AuthService authService;
    private final CreateSellerService createSellerService;
    private final CertificationService certificationService;

    /**
     * 회원가입 API
     *
     * @param signUpRequest 회원가입 요청 데이터
     * @return 생성된 사용자 정보
     */
    @PostMapping("sign-up")
    public ApiResponseEntity<UserDTO> addUser(@Valid @RequestBody SignUpDTO signUpRequest) {
        UserDTO userDTO = signUpService.addUser(signUpRequest);
        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }

    /**
     * 로그인 API
     *
     * @param loginRequest 로그인 요청 데이터
     * @return 로그인된 사용자 정보
     */
    @PostMapping("login")
    public ApiResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginRequest) {
        UserDTO userDTO = userService.login(loginRequest, UserType.ROLE_CERTIFIED_USER);

        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }

    /**
     * 토큰 재발급 API
     *
     * @param accessToken  기존 액세스 토큰
     * @param tokenRequest 리프레시 토큰 요청 데이터
     * @return 재발급된 사용자 정보
     */
    @PostMapping("reissue")
    public ApiResponseEntity<UserDTO> reissueToken(
            @RequestHeader(value = AUTHORIZATION_HEADER) String accessToken,
            @RequestBody RefreshTokenDTO tokenRequest) {
        UserDTO userDTO = authService.reissueToken(accessToken, tokenRequest.getRefreshToken());

        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }

    /**
     * 판매자 입점 신청 API
     *
     * @param sellerRequest 판매자 신청 데이터
     * @param logoImage 로고 이미지
     * @param businessRegistrationImage 사업자 등록증 이미지
     * @param mailOrderBusinessReportImage 통신판매업 신고증 이미지
     * @param bankBookImage 통장 사본 이미지
     * @return 생성된 판매자 정보
     */
    @PostMapping("seller-entry")
    public ApiResponseEntity<SimpleSellerFromSellerDTO> addSeller(
            @RequestPart(value = "seller") @Valid CreateSellerDTO sellerRequest,
            @RequestPart(value = "logo-image", required = false) MultipartFile logoImage,
            @RequestPart(value = "business-registration-image", required = false) MultipartFile businessRegistrationImage,
            @RequestPart(value = "mail-order-business-report-image", required = false) MultipartFile mailOrderBusinessReportImage,
            @RequestPart(value = "bank-book-image", required = false) MultipartFile bankBookImage

    ) {
        SimpleSellerFromSellerDTO sellerDTO = createSellerService.addSeller(sellerRequest, logoImage, businessRegistrationImage, mailOrderBusinessReportImage, bankBookImage);
        return ApiResponseEntity.<SimpleSellerFromSellerDTO>builder()
                .data(sellerDTO)
                .build();
    }

    /**
     * 로그아웃 API
     *
     * @param accessToken 액세스 토큰
     * @return 로그아웃 결과
     */
    @PostMapping("/logout")
    public ApiResponseEntity<Boolean> logout(
            @RequestHeader(value = HeaderConstants.AUTHORIZATION_HEADER) String accessToken
    ) {
        authService.logout(accessToken);
        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * 본인인증에 필요한 데이터를 요청하는 API
     *
     * @param user 인증된 사용자 정보
     * @return 본인 인증 요청 데이터
     */
    @PreAuthorize("hasRole('ROLE_UNCERTIFIED_USER') or hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/certification/request")
    public ApiResponseEntity<CertificationRequestDataDTO> getCertificationRequestData(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        CertificationRequestDataDTO result = certificationService.getCertificationRequestData(user.getId());
        return ApiResponseEntity.<CertificationRequestDataDTO>builder()
                .data(result)
                .message("본인 인증 암호화 데이터 조회 성공")
                .build();
    }


    /**
     * 본인인증 결과 전달 받아 APP 으로 리다이렉트 시키는 URL
     *
     * @param userId 사용자 ID
     * @param encodeData 암호화된 데이터
     * @param request HTTP 요청
     */
    @RequestMapping(value = "/certification", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<HttpHeaders> getCertificationResult(
            @RequestParam(value = "user-id",required = false) Long userId,
            @RequestParam(value = "EncodeData") String encodeData,
            HttpServletRequest request
    ) {
        LogUtils.writeInfoLog("getCertificationResult", request.getQueryString());
        HttpHeaders httpHeaders = certificationService.saveUserCertification(userId, encodeData, request.getSession());

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
