package com.impacus.maketplace.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchRecentService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String RECENT_KEYWORD_KEY = "recent_search:userId:";

    public void addSearch(String searchName, SearchType searchType, Long searchId, Long userId) {
        SearchDTO searchDTO = new SearchDTO(searchName, searchType, searchId);
        String str = this.objectToString(searchDTO);

        List<String> range = redisTemplate.opsForList().range(RECENT_KEYWORD_KEY + userId, 0, 9);
        for (String json : range) {
            SearchDTO savedSearch = this.stringToObject(json, SearchDTO.class);
            if (savedSearch.equals(searchDTO)) return;
        }

        redisTemplate.opsForList().leftPush(RECENT_KEYWORD_KEY + userId, str);
        redisTemplate.expire(RECENT_KEYWORD_KEY + userId, 21, TimeUnit.DAYS);
    }

    private <T> String objectToString(T value) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>((Class<T>) value.getClass());
        byte[] jsonBytes = serializer.serialize(value);
        return new String(jsonBytes, StandardCharsets.UTF_8);
    }

    private <T> T stringToObject(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule()); // 생성자 기반 역직렬화 지원

        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, clazz);
        return serializer.deserialize(json.getBytes());
    }

    public List<SearchDTO> getSearch(Long userId) {
        List<String> range = redisTemplate.opsForList().range(RECENT_KEYWORD_KEY + userId, 0, 9);
        List<SearchDTO> result = new ArrayList<>();
        for (String json : range) {
            SearchDTO searchDTO = this.stringToObject(json, SearchDTO.class);
            result.add(searchDTO);
        }
        return result;
    }
}
