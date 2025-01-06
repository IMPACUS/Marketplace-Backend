package com.impacus.maketplace.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.dto.SearchDTO;
import com.impacus.maketplace.entity.search.PopularSearch;
import com.impacus.maketplace.repository.search.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PopularSearchService {
    private final RedisTemplate<String, String> redisTemplate;
    private final PopularSearchRepository popularSearchRepository;

    private static final String POPULAR_KEYWORDS_KEY = "popular_search";

    /**
     * 검색어 호출시 인기검색어 우선순위 상승
     *
     * @param searchName 검색어
     * @param searchType 검색타입
     * @param searchId   검색 id
     */
    public void incrementKeyword(String searchName, SearchType searchType, Long searchId) {
        SearchDTO searchDTO = new SearchDTO(searchName, searchType, searchId);
        String str = this.objectToString(searchDTO);
        boolean isExist = redisTemplate.opsForZSet().score(POPULAR_KEYWORDS_KEY, str) != null;
        if (isExist)
            redisTemplate.opsForZSet().incrementScore(POPULAR_KEYWORDS_KEY, str, -1);
    }

    private <T> String objectToString(T value) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>((Class<T>) value.getClass());
        byte[] jsonBytes = serializer.serialize(value);
        return new String(jsonBytes, StandardCharsets.UTF_8);
    }

    /**
     * 인기 검색어 top 10 호출
     *
     * @return
     */
    public List<SearchDTO> getTopKeywords() {
        List<SearchDTO> top10 = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().rangeWithScores(POPULAR_KEYWORDS_KEY, 0, 9);
        if (tuples != null) {
            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                if (tuple.getScore() == Integer.MAX_VALUE) continue;
                top10.add(this.stringToObject(tuple.getValue(), SearchDTO.class));
            }
        }
        return top10;
    }

    private <T> T stringToObject(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule()); // 생성자 기반 역직렬화 지원

        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, clazz);
        return serializer.deserialize(json.getBytes());
    }

    /**
     * 매월 1일마다 top10 인기검색어 백업
     */
    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Seoul")
    @Transactional
    public void backup() {
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().rangeWithScores(POPULAR_KEYWORDS_KEY, 0, -1);
        if (tuples == null || tuples.size() == 0) return;

        List<PopularSearch> list = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> json : tuples) {
            SearchDTO searchDTO = this.stringToObject(json.getValue(), SearchDTO.class);
            list.add(new PopularSearch(searchDTO, (int) Math.round(json.getScore())));
        }
        popularSearchRepository.truncateTable();
        popularSearchRepository.saveAll(list);
    }
}
