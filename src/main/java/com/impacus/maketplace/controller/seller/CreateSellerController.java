package com.impacus.maketplace.controller.seller;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.auth.request.EmailRequest;
import com.impacus.maketplace.dto.auth.request.EmailVerificationRequest;
import com.impacus.maketplace.dto.user.request.LoginDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import com.impacus.maketplace.service.seller.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/seller")
public class CreateSellerController {
    private final SellerService sellerService;
    private final UserService userService;
    private final AuthService authService;

    /**
     * 이메일 인증 코드 요청 API
     *
     * @param request
     * @return
     */
    @PostMapping("/email/verification-request")
    public ApiResponseEntity<Object> sendVerificationCodeToEmail(@Valid @RequestBody EmailRequest request) {
        userService.sendVerificationCodeToEmail(request.getEmail(), UserType.ROLE_APPROVED_SELLER);
        return ApiResponseEntity
                .builder()
                .build();
    }

    /**
     * 이메일 인증 API
     *
     * @param request
     * @return
     */
    @PostMapping("/email/confirm")
    public ApiResponseEntity<Object> confirmEmail(@Valid @RequestBody EmailVerificationRequest request) {
        boolean result = userService.confirmEmail(request);
        return ApiResponseEntity
                .builder()
                .data(result)
                .build();
    }

    /**
     * 판매자 로그인 API
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("auth/login")
    public ApiResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserDTO userDTO = userService.login(loginDTO, UserType.ROLE_APPROVED_SELLER);
        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }
}
