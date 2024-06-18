package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.redis.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlacklistService {
    private final BlacklistRepository blacklistRepository;
}
