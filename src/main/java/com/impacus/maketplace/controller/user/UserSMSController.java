package com.impacus.maketplace.controller.user;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.auth.request.SMSVerificationForEmailDTO;
import com.impacus.maketplace.dto.auth.request.SMSVerificationForPasswordDTO;
import com.impacus.maketplace.dto.auth.request.SMSVerificationRequestDTO;
import com.impacus.maketplace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/sms")
public class UserSMSController {
    private final UserService userService;

    /**
     * 이메일 인증 코드 요청 API
     *
     * @param dto
     * @return
     */
    @PostMapping("/verification-request")
    public ApiResponseEntity<Object> sendVerificationCodeToSMS(
            @Valid @RequestBody SMSVerificationRequestDTO dto
    ) {
        userService.sendVerificationCodeToSMS(dto);
        return ApiResponseEntity
                .builder()
                .message("휴대폰 번호 인증번호 전송에 성공했습니다.")
                .build();
    }

    /**
     * 이메일 찾기를 위한 휴대폰 인증 확인 API
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/sms/verify-for-email")
    public ApiResponseEntity<Object> verifySMSCodeForEmail(
            @Valid @RequestBody SMSVerificationForEmailDTO dto
    ) {
        boolean result = userService.verifySMSCodeForEmail(dto);
        return ApiResponseEntity
                .builder()
                .data(result)
                .message("이메일 찾기를 위한 휴대폰 인증 확인에 성공하였습니다.")
                .build();
    }

    /**
     * 비밀번호 찾기를 위한 휴대폰 인증 확인 API
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/sms/verify-for-password")
    public ApiResponseEntity<Object> verifySMSCodeForPassword(
            @Valid @RequestBody SMSVerificationForPasswordDTO dto
    ) {
        boolean result = userService.verifySMSCodeForPassword(dto);
        return ApiResponseEntity
                .builder()
                .data(result)
                .message("비밀번호 찾기를 위한 휴대폰 인증 확인에 성공하였습니다.")
                .build();
    }
}
