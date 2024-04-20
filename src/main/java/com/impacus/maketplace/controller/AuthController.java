package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.user.request.LoginRequest;
import com.impacus.maketplace.dto.user.request.SignUpRequest;
import com.impacus.maketplace.dto.user.request.TokenRequest;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.PointService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import com.impacus.maketplace.service.coupon.CouponAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserService userService;
    private final AuthService authService;
    private final PointService pointService;

    private final CouponAdminService couponAdminService;

    @PostMapping("sign-up")
    public ApiResponseEntity<UserDTO> addUser(@RequestBody SignUpRequest signUpRequest) {
        UserDTO userDTO = this.userService.addUser(signUpRequest);
        boolean existPointMaster = pointService.initPointMaster(userDTO);
        // 회원 가입 축하 이벤트
        couponAdminService.joinCouponForOpenEvent(userDTO.id());

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
    public ApiResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = userService.login(loginRequest);

        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }

    @PostMapping("reissue")
    public ApiResponseEntity<UserDTO> reissueToken(
            @RequestHeader(value = AUTHORIZATION_HEADER) String accessToken,
            @RequestBody TokenRequest tokenRequest) {
        UserDTO userDTO = authService.reissueToken(accessToken, tokenRequest.getRefreshToken());

        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }
}
