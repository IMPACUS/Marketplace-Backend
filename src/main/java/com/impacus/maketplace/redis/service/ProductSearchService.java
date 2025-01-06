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
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductSearchService {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX_KEY = "autocomplete:";

    public List<SearchDTO> getInitializer() {
        return null;
//        List<ProductSearch> all = productSearchRepository.findAll();
//        return all.stream().map(SearchDTO::new).collect(Collectors.toList());
    }

    public void addSearchData(SearchType type, Long searchId, String searchName) {
        List<String> prefixes = this.makePrefixes(searchName);

        for (String prefix : prefixes) {
            SearchDTO searchDTO = new SearchDTO(searchName, type, searchId);
            String redisKey = PREFIX_KEY + prefix;
            // ZADD 명령
            redisTemplate.opsForZSet().add(redisKey, this.objectToString(searchDTO), 0.0);
        }
    }

    private List<String> makePrefixes(String term) {
        List<String> results = new ArrayList<>();
        for (int i = 1; i <= term.length(); i++) {
            if (term.charAt(i - 1) == ' ') continue;
            results.add(term.substring(0, i));
        }
        getChoseong(term, results);
        return results;
    }

    private void getChoseong(String term, List<String> results) {
        term = term.replace(" ", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);
            // 가(AC00) ~ 힣(D7A3) 범위 내에 있는가?
            if (c >= 0xAC00 && c <= 0xD7A3) {
                int base = c - 0xAC00;  // 한글 코드 기준점
                int choseongIndex = base / (21 * 28);

                // 초성 배열
                char[] CHOSEONG_LIST = {
                        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
                };

                sb.append(CHOSEONG_LIST[choseongIndex]);
            } else {
                // 아니면 그대로
                sb.append(c);
            }
            results.add(sb.toString());
        }
    }

    private <T> String objectToString(T value) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>((Class<T>) value.getClass());
        byte[] jsonBytes = serializer.serialize(value);
        return new String(jsonBytes, StandardCharsets.UTF_8);
    }


    public void deleteSearchData(SearchType type, Long searchId, String searchName) {
        List<String> prefixes = this.makePrefixes(searchName);
        SearchDTO searchDTO = new SearchDTO(searchName, type, searchId);
        for (String prefix : prefixes) {
            String redisKey = PREFIX_KEY + prefix;
            redisTemplate.opsForZSet().remove(redisKey, this.objectToString(searchDTO));
        }
    }


    public void updateSearchData(SearchType type, Long searchId, String searchName) {

    }

    public List<SearchDTO> getAutoCompleteData(String keyword) {
        String redisKey = PREFIX_KEY + keyword;

        Set<String> resultSet = redisTemplate
                .opsForZSet()
                .range(redisKey, 0, 9);


        if (resultSet == null) {
            return Collections.emptyList();
        }
        List<SearchDTO> collect = resultSet.stream()
                .map(r -> this.stringToObject(r, SearchDTO.class))
                .collect(Collectors.toList());

        // 결과를 리스트로 변환
        return collect;
    }

    private <T> T stringToObject(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule()); // 생성자 기반 역직렬화 지원

        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, clazz);
        return serializer.deserialize(json.getBytes());
    }
}
