package com.impacus.maketplace.controller.auth;

import com.impacus.maketplace.common.enumType.certification.CertificationResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/certification")
public class AuthCertificationPageController {

    @GetMapping("/response")
    public String getCertificationResponse(
            @RequestParam(value = "result") CertificationResultCode result,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "detail", required = false) String detail,
            ModelMap modelMap
    ) {
        modelMap.addAttribute("result", result.getMessage());
        if (result.equals(CertificationResultCode.FAIL)) {
            modelMap.addAttribute("message",
                    String.format("%s: %s", code, detail)
            );
        } else {
            modelMap.addAttribute("message",
                    "본인인증에 성공했습니다."
            );
        }

        return "certification-response";
    }
}
