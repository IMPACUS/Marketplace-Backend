package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.redis.entity.CertificationRequestNumber;
import com.impacus.maketplace.redis.repository.CertificationRequestNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationRequestNumberService {
    private final CertificationRequestNumberRepository certReqNumberRepository;

    /**
     * 보안 인증 번호 저장
     *
     * @param reqNumber
     */
    @Transactional
    public void saveCertificationRequestNumber(String reqNumber) {
        CertificationRequestNumber certificationReqNumber = new CertificationRequestNumber(reqNumber);
        certReqNumberRepository.save(certificationReqNumber);

        boolean a = existsCertificationRequestNumber(reqNumber);
        LogUtils.writeInfoLog("saveCertificationRequestNumber", "");
    }

    public boolean existsCertificationRequestNumber(String reqNumber) {
        return certReqNumberRepository.existsByReqNumber(reqNumber);
    }

    /**
     * 보안 인증 번호 삭제
     *
     * @param reqNumber
     */
    @Transactional
    public void deleteCertificationRequestNumber(String reqNumber) {
        Optional<CertificationRequestNumber> numbers = certReqNumberRepository.findByReqNumber(reqNumber);
        if (numbers.isPresent()) {
            certReqNumberRepository.delete(numbers.get());
        }
    }
}
