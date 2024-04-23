package com.impacus.maketplace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.seller.response.SellerEntryStatusDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/seller")
public class SellerController {
    @GetMapping("entry-status")
    public ApiResponseEntity<SellerEntryStatusDTO> getMethodName() {
        return ApiResponseEntity.<SellerEntryStatusDTO>builder()
                .data(null)
                .build();
    }
    
}
