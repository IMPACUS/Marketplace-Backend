package com.impacus.maketplace.controller.search;


import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.SearchDTO;
import com.impacus.maketplace.redis.service.ProductSearchService;
import com.impacus.maketplace.redis.service.RecentSearchService;
import com.impacus.maketplace.service.search.PopularSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    private final ProductSearchService productSearchService;
    private final RecentSearchService recentSearchService;
    private final PopularSearchService popularSearchService;

    @GetMapping("auto-complete")
    public ApiResponseEntity<?> getAutoComplete(@RequestParam("keyword") String keyword) {
        List<SearchDTO> searchData = productSearchService.getAutoCompleteData(keyword);
        return ApiResponseEntity.builder()
                .message("자동완성이 조회됐습니다.")
                .data(searchData)
                .build();
    }

    @PostMapping
    public ApiResponseEntity<?> getSearchResult(@RequestParam("search") String search,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        popularSearchService.addKeyword(search);
        if (user != null) recentSearchService.addSearch(search, user.getId());
        return ApiResponseEntity.builder()
                .message("검색이 조회됐습니다.")
                .build();
    }

    @GetMapping
    public ApiResponseEntity<?> getSearchData(@AuthenticationPrincipal CustomUserDetails user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("recentSearch", null);
        if (user != null) {
            LinkedList<String> recentSearch = recentSearchService.getSearch(user.getId());
            map.put("recentSearch", recentSearch);
        }
        List<Object> popularSearch = popularSearchService.getPopularKeywords();
        map.put("popularSearch", popularSearch);

        return ApiResponseEntity.builder()
                .message("검색창 데이터가 조회됐습니다.")
                .data(map)
                .build();
    }


    // 1) 상품 등록 시 자동완성 인덱스 생성
    @PostMapping("1")
    public ApiResponseEntity<?> addProduct(@RequestParam("searchType") SearchType searchType,
                                           @RequestParam("searchId") Long searchId,
                                           @RequestParam("productName") String searchName) {
        productSearchService.addSearchData(searchType, searchId, searchName);

        return ApiResponseEntity.builder()
                .message("검색창 데이터가 등록됐습니다.")
                .build();
    }

    @PostMapping("3")
    public ApiResponseEntity<?> dProduct(@RequestParam("searchType") SearchType searchType,
                                           @RequestParam("searchId") Long searchId,
                                           @RequestParam("productName") String searchName) {
        productSearchService.deleteSearchData(searchType, searchId, searchName);

        return ApiResponseEntity.builder()
                .message("검색창 데이터가 삭제.")
                .build();
    }
}
