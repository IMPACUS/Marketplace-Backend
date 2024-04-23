package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.seller.request.SellerChargePercentageRequest;
import com.impacus.maketplace.dto.seller.response.SellerEntryStatusDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerEntryDTO;
import com.impacus.maketplace.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.time.LocalDate;


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

    /**
     * 전체 판매자 입점 상태 조회 API
     *
     * @param startAt
     * @param endAt
     * @param entryStatus
     * @param pageable
     * @return
     */
    @GetMapping("/entry/sellers")
    private ApiResponseEntity<SimpleSellerEntryDTO> getSellerEntryList(
            @RequestParam(value = "start-at") LocalDate startAt,
            @RequestParam(value = "end-at") LocalDate endAt,
            @RequestParam(value = "entry-status") String[] entryStatus,
            @PageableDefault(size = 6, sort = "requestAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ApiResponseEntity.<SimpleSellerEntryDTO>builder()
                .data(null)
                .build();
    }


    /**
     * 판매자 입점 요청 상태 변경 API
     *
     * @param sellerId
     * @param request
     * @return
     */
    @PatchMapping("/{sellerId}/entry-status")
    public ApiResponseEntity<SimpleSellerDTO> changeEntryStatus(@PathVariable(value = "sellerId") Long sellerId,
                                                                @RequestBody SellerChargePercentageRequest request) {
        SimpleSellerDTO sellerEntryStatusDTO = sellerService.changeEntryStatus(sellerId, request);
        return ApiResponseEntity.<SimpleSellerDTO>builder()
                .data(null)
                .build();
    }

}