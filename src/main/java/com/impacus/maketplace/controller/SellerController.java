package com.impacus.maketplace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.seller.request.SellerFeePercentageRequest;
import com.impacus.maketplace.dto.seller.response.SellerEntryStatusDTO;
import com.impacus.maketplace.service.seller.SellerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/seller")
public class SellerController {
    private final SellerService sellerService;

    /**
     * 판매자 입점 현황 데이터를 조회하는 API
     * 
     * @return
     */
    @GetMapping("entry-status")
    public ApiResponseEntity<SellerEntryStatusDTO> getEntryStatusStatistics() {
        SellerEntryStatusDTO sellerEntryStatusDTO = sellerService.getEntryStatusStatistics();
        return ApiResponseEntity.<SellerEntryStatusDTO>builder()
                .data(sellerEntryStatusDTO)
                .build();
    }

    @PatchMapping("/{sellerId}/entry-status")
    public ApiResponseEntity<SellerEntryStatusDTO> changeEntryStatus(@PathVariable(value = "sellerId") Long sellerId,
    @RequestBody SellerFeePercentageRequest feePercentageRequest) {
        SellerEntryStatusDTO sellerEntryStatusDTO = sellerService.changeEntryStatus(sellerId, feePercentageRequest);
        return ApiResponseEntity.<SellerEntryStatusDTO>builder()
                .data(null)
                .build();
    }
    
}
