package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.exception.Custom400Exception;
import com.impacus.maketplace.entity.dto.user.UserDTO;
import com.impacus.maketplace.entity.dto.user.request.LoginRequest;
import com.impacus.maketplace.entity.dto.user.request.SignUpRequest;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> addUser(@RequestBody SignUpRequest signUpRequest) {
        UserDTO userDTO = null;
        try {
            userDTO = userService.addUser(signUpRequest);
        } catch (Custom400Exception ex) {
            throw new Custom400Exception(ex);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = null;
        try {
            userDTO = userService.login(loginRequest);
        } catch (Custom400Exception ex) {
            throw new Custom400Exception(ex);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
