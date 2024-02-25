package com.impacus.maketplace.controller;

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
}
