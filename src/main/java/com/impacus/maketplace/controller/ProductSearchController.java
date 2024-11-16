package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.SearchDTO;
import com.impacus.maketplace.redis.entity.ProductSearch;
import com.impacus.maketplace.redis.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
public class ProductSearchController {
    private final ProductSearchService productSearchService;

    @GetMapping("")
    public ApiResponseEntity<?> getAutoComplete(@RequestParam("keyword") String keyword) {
        List<SearchDTO> searchData = productSearchService.getSearchData(keyword);
        return ApiResponseEntity.builder()
                .message("자동완성이 조회됐습니다.")
                .data(searchData)
                .build();
    }
}
