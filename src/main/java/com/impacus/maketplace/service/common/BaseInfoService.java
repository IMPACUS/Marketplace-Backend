package com.impacus.maketplace.service.common;

import com.impacus.maketplace.repository.common.BaseInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BaseInfoService {
    private final BaseInfoRepository baseInfoRepository;
}
