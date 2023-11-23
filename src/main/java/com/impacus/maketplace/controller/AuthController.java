package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.dto.user.UserDTO;
import com.impacus.maketplace.entity.dto.user.request.LoginRequest;
import com.impacus.maketplace.entity.dto.user.request.SignUpRequest;
import com.impacus.maketplace.entity.dto.user.request.TokenRequest;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> addUser(@RequestBody SignUpRequest signUpRequest) {
        UserDTO userDTO = null;
        try {
            userDTO = this.userService.addUser(signUpRequest);
        } catch (CustomException ex) {
            throw new CustomException(ex);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = null;
        try {
            userDTO = userService.login(loginRequest);
        } catch (CustomException ex) {
            throw new CustomException(ex);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/reissue")
    public ResponseEntity<Object> reissueToken(
        @RequestHeader(value = AUTHORIZATION_HEADER) String accessToken,
        @RequestBody TokenRequest tokenRequest) {
        UserDTO userDTO = null;
        try {
            userDTO = authService.reissueToken(accessToken, tokenRequest.getRefreshToken());
        } catch (CustomException ex) {
            throw new CustomException(ex);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('UNCERTIFIED_USER')")
    public ResponseEntity<Object> test(
        @RequestHeader(value = AUTHORIZATION_HEADER) String accessToken) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities(accessToken));
    }
}
