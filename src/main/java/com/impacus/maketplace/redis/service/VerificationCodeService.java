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
     * @param identifier
     * @param code
     */
    @Transactional
    public void saveVerificationCode(String identifier, String code) {
        // 1. identifier 에 대해서 인증 데이터가 존재하는지 확인
        VerificationCode existedCode = findVerificationCodeByIdentifier(identifier);
        if (existedCode != null) {
            deleteIdentifierVerificationCode(existedCode);
        }

        VerificationCode verificationCode = VerificationCode.toEntity(identifier, code);
        verificationCodeRepository.save(verificationCode);
    }

    /**
     * identifier 에 대해서 VerificationCode 를 조회하는 함수
     * @param identifier
     * @return
     */
    public VerificationCode findVerificationCodeByIdentifier(String identifier) {
        return verificationCodeRepository.findByIdentifier(identifier).orElse(null);
    }

    /**
     * 전달받은 identifier, code에 대해서 VerificationCode 를 조회하는 함수
     * @param identifier
     * @param code
     * @return
     */
    public VerificationCode findVerificationCode(String identifier, String code) {
        return verificationCodeRepository.findByIdentifierAndCode(identifier, code).orElse(null);
    }

    /**
     * VerificationCode 삭제하는 함수
     * @param verificationCode 삭제할 VerificationCode
     */
    @Transactional
    public void deleteIdentifierVerificationCode(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
    
}
