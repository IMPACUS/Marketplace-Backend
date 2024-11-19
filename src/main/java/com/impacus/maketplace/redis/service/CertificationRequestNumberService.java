package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.redis.entity.CertificationRequestNumber;
import com.impacus.maketplace.redis.repository.CertificationRequestNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationRequestNumberService {
    private final CertificationRequestNumberRepository certificationRequestNumberRepository;

    /**
     * 보안 인증 번호 저장
     *
     * @param reqNumber
     */
    @Transactional
    public void saveCertificationRequestNumber(String reqNumber) {
        CertificationRequestNumber certificationReqNumber = new CertificationRequestNumber(reqNumber);
        certificationRequestNumberRepository.save(certificationReqNumber);
    }
}
