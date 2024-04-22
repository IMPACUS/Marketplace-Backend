package com.impacus.maketplace.redis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.redis.entity.EmailVerificationCode;
import com.impacus.maketplace.redis.repository.EmailVerificationCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailVerificationCodeService {

    private final EmailVerificationCodeRepository emailAuthenticationNumberRepository;

    /**
     * 이메일 인증 데이터를 저장하는 함수
     * @param email
     * @param code
     */
    @Transactional
    public void saveEmailVerificationCode(String email, String code) {
        EmailVerificationCode emailVerificationCode = EmailVerificationCode.toEntity(email, code);

        emailAuthenticationNumberRepository.save(emailVerificationCode);
    }

    /**
     * 전달받은 email, code에 대해서 EmailVerificationCode를 조회하는 함수
     * @param email
     * @param code
     * @return
     */
    public EmailVerificationCode findEmailVerificationCodeByEmailAndCode(String email, String code) {
        return emailAuthenticationNumberRepository.findByEmailAndCode(email, code).orElse(null);
    }

    /**
     * emailVerificationCode 삭제하는 함수
     * @param emailVerificationCode
     */
    public void deleteEmailVerificationCode(EmailVerificationCode emailVerificationCode) {
        emailAuthenticationNumberRepository.delete(emailVerificationCode);
    }
    
}
