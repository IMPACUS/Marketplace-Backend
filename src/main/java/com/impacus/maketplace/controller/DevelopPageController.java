package com.impacus.maketplace.controller;

import com.impacus.maketplace.dto.auth.response.CertificationRequestDataDTO;
import com.impacus.maketplace.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/develop")
public class DevelopPageController {
    private final AuthService authService;

    @GetMapping("/cert")
    public String getCertification(
            HttpServletRequest request,
            ModelMap modelMap
    ) {
        CertificationRequestDataDTO dto = authService.getCertificationRequestData(1L, true);

        request.getSession().setAttribute("REQ_SEQ", dto.getReqNumber());
        request.getSession().setAttribute("USER_ID", 1);

        modelMap.addAttribute("sEncData", dto.getEncData());

        return "user-certification";
    }

}
