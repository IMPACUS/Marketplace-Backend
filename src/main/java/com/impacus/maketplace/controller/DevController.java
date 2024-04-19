package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.dev.request.TestValidEnumRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DevController {
    @RequestMapping("/oauth")
    public String oauth(Model model) {
        model.addAttribute("appleUrl", "https://back-dev.implace.kr/oauth2/authorization/apple");
        return "apple-login";
    }

    @RequestMapping("/test-valid-enum")
    public ApiResponseEntity<Object> testValidEnum(@Valid TestValidEnumRequest validEnumDTO) {
        return ApiResponseEntity
                .builder()
                .data(validEnumDTO)
                .build();
    }
}
