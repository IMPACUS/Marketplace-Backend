package com.impacus.maketplace.controller.auth;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.enumType.certification.CertificationResultCode;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.auth.response.CertificationRequestDataDTO;
import com.impacus.maketplace.dto.seller.request.CreateSellerDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerFromSellerDTO;
import com.impacus.maketplace.dto.user.request.LoginDTO;
import com.impacus.maketplace.dto.user.request.RefreshTokenDTO;
import com.impacus.maketplace.dto.user.request.SignUpDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import com.impacus.maketplace.service.seller.CreateSellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserService userService;
    private final AuthService authService;
    private final CreateSellerService createSellerService;


    @PostMapping("sign-up")
    public ApiResponseEntity<UserDTO> addUser(@Valid @RequestBody SignUpDTO signUpRequest) {
        UserDTO userDTO = this.userService.addUser(signUpRequest);
        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }

    @PostMapping("login")
    public ApiResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginRequest) {
        UserDTO userDTO = userService.login(loginRequest, UserType.ROLE_CERTIFIED_USER);

        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }

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
     * @param sellerRequest
     * @param logoImage
     * @param businessRegistrationImage
     * @param mailOrderBusinessReportImage
     * @param bankBookImage
     * @return
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
     * @return
     */
    @PreAuthorize("hasRole('ROLE_UNCERTIFIED_USER') or hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/certification/request")
    public ApiResponseEntity<CertificationRequestDataDTO> getCertificationRequestData(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        CertificationRequestDataDTO result = authService.getCertificationRequestData(user.getId());
        return ApiResponseEntity.<CertificationRequestDataDTO>builder()
                .data(result)
                .message("본인 인증 암호화 데이터 조회 성공")
                .build();
    }


    /**
     * 본인인증 결과 전달 받아 APP 으로 리다이렉트 시키는 URL
     *
     * @param result
     * @param encodeData
     * @param request
     * @return
     */
    @RequestMapping(value = "/certification", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<HttpHeaders> getCertificationResult(
            @RequestParam(value = "result") CertificationResultCode result,
            @RequestParam(value = "user-id") Long userId,
            @RequestParam(value = "EncodeData") String encodeData,
            HttpServletRequest request
    ) {
        HttpHeaders httpHeaders = authService.saveUserCertification(result, userId, encodeData, request.getSession());

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
