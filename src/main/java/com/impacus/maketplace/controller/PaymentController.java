package com.impacus.maketplace.controller;

import com.impacus.maketplace.dto.payment.request.PaymentRequest;
import com.impacus.maketplace.dto.payment.response.PaymentDTO;
import com.impacus.maketplace.dto.user.request.LoginRequest;
import com.impacus.maketplace.dto.user.request.SignUpRequest;
import com.impacus.maketplace.dto.user.request.TokenRequest;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.PaymentService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author JHK
 * date : 2023. 12. 22.
 * description : 결제 Request mapping
 *
 *
 * 1. 빌링키 등록 (결제 수단 등록)
 * 2. 즉시결제
 * 3. 재결제
 * 4. 결제취소
 * 5. 결제 이력 조회
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment")
public class PaymentController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final PaymentService paymentService;
    /*

     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody PaymentRequest paymentRequest) throws Exception {
        PaymentDTO paymentDTO = this.paymentService.register(paymentRequest);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @PostMapping("/onetime")
    public ResponseEntity<Object> onetime(@RequestBody PaymentRequest paymentRequest) throws Exception {
        PaymentDTO paymentDTO = this.paymentService.onetime(paymentRequest);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }
}
