package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ApiResponseEntity<?> sendEmailCode(@RequestBody EmailDto emailDto) {

        Boolean result = emailService.sendMail(emailDto, MailType.AUTH);

        return ApiResponseEntity.builder()
                .data(result)
                .build();
    }

    @PostMapping("/check")
    public ApiResponseEntity<?> checkAuthNumber(@RequestBody EmailDto emailDto) {
        Boolean result = emailService.checkAuthNumber(emailDto);

        return ApiResponseEntity.builder()
                .result(result)
                .build();
    }



}
