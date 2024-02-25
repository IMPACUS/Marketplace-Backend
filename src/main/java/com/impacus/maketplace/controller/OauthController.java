package com.impacus.maketplace.controller;

import com.impacus.maketplace.dto.apple.request.AppleLoginCodeRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("oauth")
public class OauthController {

    //https://appleid.apple.com/auth/authorize?client_id=implace.kr&redirect_uri=https://back-dev.implace.kr/oauth/apple/login/code&response_type=code%20id_token&scope=name%20email&response_mode=form_post

    @PostMapping("apple/login/callback")
    public ResponseEntity<Object> getAppleLoginResult(@RequestBody AppleLoginCodeRequest appleLoginCodeRequest) throws Exception {
        log.info("IN ----- /oauth/apple/login/callback ");
        log.info(appleLoginCodeRequest.getCode());
        //AppleDTO appleInfo = appleService.getAppleInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body("OK");
    }

    @PostMapping("apple/login/code")
    public ResponseEntity<Object> callback(HttpServletRequest request) throws Exception {
        log.info("IN ----- /oauth/apple/login/code ");
        log.info(request.getParameter("code"));
        //AppleDTO appleInfo = appleService.getAppleInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body("OK");
    }
}
