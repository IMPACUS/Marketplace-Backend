package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.seller.request.CreateSellerDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerDTO;
import com.impacus.maketplace.dto.user.request.LoginDTO;
import com.impacus.maketplace.dto.user.request.RefreshTokenDTO;
import com.impacus.maketplace.dto.user.request.SignUpDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.PointService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import com.impacus.maketplace.service.seller.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserService userService;
    private final AuthService authService;
    private final PointService pointService;
    private final SellerService sellerService;


    @PostMapping("sign-up")
    public ApiResponseEntity<UserDTO> addUser(@Valid @RequestBody SignUpDTO signUpRequest) {
        UserDTO userDTO = this.userService.addUser(signUpRequest);
        boolean existPointMaster = pointService.initPointMaster(userDTO);
        // 회원 가입 축하 이벤트
//        couponAdminService.joinCouponForOpenEvent(userDTO.id());

        if (existPointMaster) {
            return ApiResponseEntity.<UserDTO>builder()
                    .code(HttpStatus.CONFLICT)
                    .data(userDTO)
                    .build();
        }
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
    public ApiResponseEntity<SimpleSellerDTO> addSeller(
            @RequestPart(value = "seller") @Valid CreateSellerDTO sellerRequest,
            @RequestPart(value = "logo-image", required = false) MultipartFile logoImage,
            @RequestPart(value = "business-registration-image", required = false) MultipartFile businessRegistrationImage,
            @RequestPart(value = "mail-order-business-report-image", required = false) MultipartFile mailOrderBusinessReportImage,
            @RequestPart(value = "bank-book-image", required = false) MultipartFile bankBookImage

    ) {
        SimpleSellerDTO sellerDTO = sellerService.addSeller(sellerRequest, logoImage, businessRegistrationImage, mailOrderBusinessReportImage, bankBookImage);
        return ApiResponseEntity.<SimpleSellerDTO>builder()
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
}
