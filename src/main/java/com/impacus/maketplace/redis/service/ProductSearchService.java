package com.impacus.maketplace.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.dto.SearchDTO;
import com.impacus.maketplace.entity.product.Product;
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

    private static final String AUTO_COMPLETE_KEY = "autocomplete:";
    private static final String POPULAR_KEYWORDS_KEY = "popular_search";

    /**
     * redis에 자동완성과 인기검색어 데이터 삽입
     *
     * @param searchType 검색타입
     * @param searchId   검색 id
     * @param searchName 검색어
     */
    public void addSearchData(SearchType searchType, Long searchId, String searchName) {
        SearchDTO searchDTO = new SearchDTO(searchName, searchType, searchId);
        String str = this.objectToString(searchDTO);
        boolean isExist = redisTemplate.opsForZSet().score(AUTO_COMPLETE_KEY + searchName, str) != null;
        if (!isExist) {
            List<String> prefixes = this.makePrefixes(searchName);
            for (String prefix : prefixes) {
                String redisKey = AUTO_COMPLETE_KEY + prefix;
                redisTemplate.opsForZSet().add(redisKey, str, Integer.MAX_VALUE);
            }
            if (searchType.equals(SearchType.PRODUCT))
                redisTemplate.opsForZSet().add(POPULAR_KEYWORDS_KEY, str, Integer.MAX_VALUE);
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

    /**
     * redis에 자동완성과 인기검색어 데이터 삭제
     *
     * @param searchType 검색타입
     * @param searchId   검색 id
     * @param searchName 검색명
     */
    public void deleteSearchData(SearchType searchType, Long searchId, String searchName) {
        SearchDTO searchDTO = new SearchDTO(searchName, searchType, searchId);
        String str = this.objectToString(searchDTO);
        boolean isExist = redisTemplate.opsForZSet().score(AUTO_COMPLETE_KEY + searchName, str) != null;

        if (isExist) {
            List<String> prefixes = this.makePrefixes(searchName);
            for (String prefix : prefixes) {
                String redisKey = AUTO_COMPLETE_KEY + prefix;
                redisTemplate.opsForZSet().remove(redisKey, str);
            }
            redisTemplate.opsForZSet().remove(POPULAR_KEYWORDS_KEY, str);
        }
    }

    /**
     * redis에 자동완성과 인기검색어 데이터 수정
     *
     * @param searchType    검색 타입
     * @param searchId      검색 id
     * @param oldSearchName 기존 검색명
     * @param newSearchName 새 검색명
     */
    public void updateSearchData(SearchType searchType, Long searchId, String oldSearchName, String newSearchName) {
        SearchDTO searchDTO = new SearchDTO(oldSearchName, searchType, searchId);
        String oldStr = this.objectToString(searchDTO);
        Double score = redisTemplate.opsForZSet().score(AUTO_COMPLETE_KEY + oldSearchName, oldStr);
        if (score != null) {
            // 기존 데이터 삭제
            List<String> prefixes = this.makePrefixes(oldSearchName);
            for (String prefix : prefixes) {
                String redisKey = AUTO_COMPLETE_KEY + prefix;
                redisTemplate.opsForZSet().remove(redisKey, oldStr);
            }
            redisTemplate.opsForZSet().remove(POPULAR_KEYWORDS_KEY, oldStr);

            // 새로 추가
            searchDTO = new SearchDTO(newSearchName, searchType, searchId);
            String newStr = this.objectToString(searchDTO);
            prefixes = this.makePrefixes(newSearchName);
            for (String prefix : prefixes) {
                String redisKey = AUTO_COMPLETE_KEY + prefix;
                redisTemplate.opsForZSet().add(redisKey, newStr, score);
            }
            if (searchType.equals(SearchType.PRODUCT))
                redisTemplate.opsForZSet().add(POPULAR_KEYWORDS_KEY, newStr, score);
        }
    }


    /**
     * redis로부터 자동완성 검색어 호출
     *
     * @param keyword 검색어
     * @return
     */
    public List<SearchDTO> getAutoCompleteData(String keyword) {
        String redisKey = AUTO_COMPLETE_KEY + keyword;

        Set<String> resultSet = redisTemplate.opsForZSet().range(redisKey, 0, 9);

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

    /**
     * 자동 완성으로 검색시 우선순위 높이기
     *
     * @param searchName 검색이름
     * @param searchType 검색타입
     * @param searchId   검색 id
     */
    public void updateScore(String searchName, SearchType searchType, Long searchId) {
        SearchDTO searchDTO = new SearchDTO(searchName, searchType, searchId);
        String str = this.objectToString(searchDTO);
        boolean isExist = redisTemplate.opsForZSet().score(AUTO_COMPLETE_KEY + searchName, str) != null;
        if (isExist) {
            List<String> prefixes = this.makePrefixes(searchName);
            for (String prefix : prefixes) {
                String redisKey = AUTO_COMPLETE_KEY + prefix;
                // ZADD 명령
                redisTemplate.opsForZSet().incrementScore(redisKey, str, -1);
            }
        }
    }
}
