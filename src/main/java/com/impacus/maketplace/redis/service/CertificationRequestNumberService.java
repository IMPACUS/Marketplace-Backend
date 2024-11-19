package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.redis.repository.CertificationRequestNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationRequestNumberService {
    private final CertificationRequestNumberRepository certificationRequestNumberRepository;
}
