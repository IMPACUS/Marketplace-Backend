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
