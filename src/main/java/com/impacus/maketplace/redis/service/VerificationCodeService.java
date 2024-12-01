package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.redis.entity.VerificationCode;
import com.impacus.maketplace.redis.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    /**
     * 이메일 인증 데이터를 저장하는 함수
     * @param email
     * @param code
     */
    @Transactional
    public void saveEmailVerificationCode(String email, String code) {
        // 1. email에 대해서 인증 데이터가 존재하는지 확인
        VerificationCode existedCode = findEmailVerificationCodeByEmail(email);
        if (existedCode != null) {
            deleteEmailVerificationCode(existedCode);
        }

        VerificationCode emailVerificationCode = VerificationCode.toEntity(email, code);
        verificationCodeRepository.save(emailVerificationCode);
    }

    /**
     * email에 대해서 EmailVerificationCode를 조회하는 함수
     * @param email
     * @return
     */
    public VerificationCode findEmailVerificationCodeByEmail(String email) {
        return verificationCodeRepository.findByEmail(email).orElse(null);
    }

    /**
     * 전달받은 email, code에 대해서 EmailVerificationCode를 조회하는 함수
     * @param email
     * @param code
     * @return
     */
    public VerificationCode findEmailVerificationCodeByEmailAndCode(String email, String code) {
        return verificationCodeRepository.findByEmailAndCode(email, code).orElse(null);
    }

    /**
     * emailVerificationCode 삭제하는 함수
     * @param emailVerificationCode
     */
    @Transactional
    public void deleteEmailVerificationCode(VerificationCode emailVerificationCode) {
        verificationCodeRepository.delete(emailVerificationCode);
    }
    
}
