package com.impacus.maketplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.user.request.LoginRequest;
import com.impacus.maketplace.dto.user.request.SignUpRequest;
import com.impacus.maketplace.dto.user.request.TokenRequest;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.PointService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserService userService;
    private final AuthService authService;
    private final PointService pointService;

    @PostMapping("sign-up")
    public ApiResponseEntity<UserDTO> addUser(@RequestBody SignUpRequest signUpRequest) {
        UserDTO userDTO = this.userService.addUser(signUpRequest);
        boolean existPointMaster = pointService.initPointMaster(userDTO);
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

    @PostMapping("/emails/verification-requests")
    public ApiResponseEntity<Object> sendMessage(@RequestParam("email") @Valid @Email String email) {
        userService.sendCodeToEmail(email);
        return ApiResponseEntity
                .builder()
                .build();
    }
}
