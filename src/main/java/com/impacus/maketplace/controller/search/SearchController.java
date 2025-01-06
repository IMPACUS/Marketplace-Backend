package com.impacus.maketplace.controller.search;


import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.SearchDTO;
import com.impacus.maketplace.dto.product.response.AppProductDTO;
import com.impacus.maketplace.redis.service.PopularSearchService;
import com.impacus.maketplace.redis.service.ProductSearchService;
import com.impacus.maketplace.redis.service.RecentSearchService;
import com.impacus.maketplace.service.product.ReadProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    ;
    private final PopularSearchService popularSearchService;
    private final ReadProductService readProductService;

    @GetMapping("auto-complete")
    public ApiResponseEntity<?> getAutoComplete(@RequestParam("keyword") String keyword) {
        List<SearchDTO> searchData = productSearchService.getAutoCompleteData(keyword);
        return ApiResponseEntity.builder()
                .message("자동완성이 조회됐습니다.")
                .data(searchData)
                .build();
    }

    @PostMapping
    public ApiResponseEntity<?> getSearchResult(@RequestParam("searchName") String searchName,
                                                @RequestParam("searchType") SearchType searchType,
                                                @RequestParam("searchId") Long searchId,
                                                @AuthenticationPrincipal CustomUserDetails user,
                                                Pageable pageable) {
        productSearchService.updateScore(searchName, searchType, searchId);
        popularSearchService.incrementKeyword(searchName, searchType, searchId);

        Slice<AppProductDTO> products = null;
        if (user != null) {
            recentSearchService.addSearch(searchName, user.getId());
            products = readProductService.findProductsByName(user.getId(), searchName, pageable);
        }
        return ApiResponseEntity.builder()
                .message("검색이 조회됐습니다.")
                .data(products)
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
        List<SearchDTO> popularSearch = popularSearchService.getTopKeywords();
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
                                         @RequestParam("oldSearchName") String oldSearchName,
                                         @RequestParam("newSearchName") String newSearchName) {
        productSearchService.updateSearchData(searchType, searchId, oldSearchName, newSearchName);

        return ApiResponseEntity.builder()
                .message("검색창 데이터가 수정")
                .build();
    }
}
