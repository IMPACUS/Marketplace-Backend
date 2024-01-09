package com.impacus.maketplace.controller;

import com.impacus.maketplace.dto.user.request.LoginRequest;
import com.impacus.maketplace.dto.user.request.SignUpRequest;
import com.impacus.maketplace.dto.user.request.TokenRequest;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.PointService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserService userService;
    private final AuthService authService;
    private final PointService pointService;

    @PostMapping("sign-up")
    public ResponseEntity<Object> addUser(@RequestBody SignUpRequest signUpRequest) {
        UserDTO userDTO = this.userService.addUser(signUpRequest);
        boolean existPointMaster = pointService.initPointMaster(userDTO);
        if (existPointMaster) {
            return new ResponseEntity<>(userDTO, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = userService.login(loginRequest);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("reissue")
    public ResponseEntity<Object> reissueToken(
            @RequestHeader(value = AUTHORIZATION_HEADER) String accessToken,
            @RequestBody TokenRequest tokenRequest) {
        UserDTO userDTO = authService.reissueToken(accessToken, tokenRequest.getRefreshToken());

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
