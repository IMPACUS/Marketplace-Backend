package com.impacus.maketplace.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PopularSearchRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String POPULAR_KEYWORDS_KEY = "popular_search";

    public void incrementKeyword(String keyword, int count) {
        redisTemplate.opsForZSet().incrementScore(POPULAR_KEYWORDS_KEY, keyword, count);
    }

    public Set<Object> getTopKeywords() {
        return redisTemplate.opsForZSet().reverseRange(POPULAR_KEYWORDS_KEY, 0, 9);
    }

    public Double getKeywordScore(String keyword) {
        return redisTemplate.opsForZSet().score(POPULAR_KEYWORDS_KEY, keyword);
    }

    public void deleteAll() {
        redisTemplate.delete(POPULAR_KEYWORDS_KEY);
    }
}
