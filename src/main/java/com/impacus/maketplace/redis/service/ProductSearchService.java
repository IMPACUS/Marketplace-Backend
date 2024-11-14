package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.error.SearchErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.SearchDTO;
import com.impacus.maketplace.redis.entity.ProductSearch;
import com.impacus.maketplace.redis.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ID_KEY = "productSearch:id"; // 자동 증가 ID를 저장할 Redis 키

    public String addSearchData(String name, SearchType type, Long productId) {
        Long increment = redisTemplate.opsForValue().increment(ID_KEY);
        ProductSearch productSearch = new ProductSearch(increment.toString(), name, type, productId);
        ProductSearch save = productSearchRepository.save(productSearch);
        return save.getId();
    }

    public void deleteSearchData(String id) {
        productSearchRepository.deleteById(id);
    }

    public void updateSearchData(String id, String searchName) {
        Optional<ProductSearch> byId = productSearchRepository.findById(id);
        if (byId.isEmpty()) throw new CustomException(HttpStatus.BAD_REQUEST, SearchErrorType.NOT_ID);
        ProductSearch productSearch = byId.get();
        productSearch.setSearchName(searchName);
        productSearchRepository.save(productSearch);
    }

    public List<SearchDTO> getSearchData(String keyword) {
        List<ProductSearch> all = productSearchRepository.findAll();

        List<ProductSearch> result = this.searchProducts(all, keyword);
        return result.stream().map(SearchDTO::new).collect(Collectors.toList());
    }

    // 검색 및 정렬된 결과 반환
    private List<ProductSearch> searchProducts(List<ProductSearch> products, String query) {
        String normalizedQuery = normalize(query); // 검색어를 초성으로 변환

        return products.stream()
                .filter(product -> normalize(product.getSearchName()).contains(normalizedQuery)) // 검색 필터링
                .sorted(Comparator
                        .comparing(ProductSearch::getType) // category, subcategory, product 순서 정렬
                        .thenComparingInt(product -> {
                            String normalizedProduct = normalize(product.getSearchName());
                            // 정렬 우선순위: 완전 일치 0, 부분 일치 1
                            return normalizedProduct.startsWith(normalizedQuery) ? 0 : 1;
                        })
                        .thenComparing(product -> normalize(product.getSearchName()).length()) // 초성 길이 순 정렬
                )
                .collect(Collectors.toList());
    }

    // 띄어쓰기를 제거하고 초성 추출
    private String normalize(String input) {
        String noSpaces = input.replaceAll(" ", ""); // 띄어쓰기 제거
        return this.extractChosung(noSpaces); // 초성 추출
    }

    // 초성을 추출하는 함수
    private String extractChosung(String keyword) {
        StringBuilder sb = new StringBuilder();
        char[] CHO_SUNG = {
                'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ',
                'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        };
        for (char ch : keyword.toCharArray()) {
            if (ch >= 0xAC00 && ch <= 0xD7A3) { // 유니코드 한글 범위
                int base = ch - 0xAC00;
                int cho = base / (21 * 28); // 초성 인덱스 계산
                sb.append(CHO_SUNG[cho]);
            } else {
                sb.append(ch); // 한글이 아닌 경우 그대로 추가
            }
        }

        return sb.toString();
    }
}
