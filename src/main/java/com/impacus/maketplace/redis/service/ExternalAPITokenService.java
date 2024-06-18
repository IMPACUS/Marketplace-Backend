package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.redis.repository.ExternalAPITokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalAPITokenService {
//    private final RedisTemplate<String, ExternalAPIToken> redisTemplate;
private final ExternalAPITokenRepository externalAPITokenRepository;
//
//    public void saveToken(ExternalAPIToken token, long ttlInSeconds) {
//        externalAPITokenRepository.save(token);
//        redisTemplate.expire(token.getId(), ttlInSeconds, TimeUnit.SECONDS);
//    }
}
