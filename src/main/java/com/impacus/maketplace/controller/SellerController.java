package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.seller.response.SellerEntryListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/seller")
public class SellerController {

    @GetMapping("/entry/sellers")
    private ApiResponseEntity<SellerEntryListDTO> getSellerEntryList(
            @RequestParam(value = "start-at") LocalDate startAt,
            @RequestParam(value = "end-at") LocalDate endAt,
            @RequestParam(value = "entry-status") String[] entryStatus,
            @PageableDefault(size = 6, sort = "requestAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ApiResponseEntity.<SellerEntryListDTO>builder()
                .data(null)
                .build();
    }

}
