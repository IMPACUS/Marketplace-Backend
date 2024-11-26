package com.impacus.maketplace.controller.auth;

import com.impacus.maketplace.common.enumType.certification.CertificationResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Enumeration;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/certification")
public class AuthCertificationPageController {

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String getCertificationServer(
            @RequestParam(value = "EncodeData") String encodeData,
            HttpServletRequest request,
            ModelMap modelMap
    ) {
        log.info("IN getCertificationServer ================");
        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            log.info("Session Attribute: " + attributeName + " = " + attributeValue);
        }

        modelMap.addAttribute("sEncodeData",
                encodeData
        );

        return "certification-server";
    }

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
