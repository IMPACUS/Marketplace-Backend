package com.impacus.maketplace.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.user.request.LoginRequest;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/seller")
public class SellerController {
    private final UserService userService;

    @PostMapping("auth/login")
    public ApiResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = userService.loginSeller(loginRequest);

        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }
}
