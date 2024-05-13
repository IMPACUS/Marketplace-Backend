package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.seller.request.ChangeSellerEntryStatusDTO;
import com.impacus.maketplace.dto.seller.response.DetailedSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.SellerEntryStatusDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerEntryDTO;
import com.impacus.maketplace.dto.user.request.LoginDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.seller.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/seller")
public class SellerController {
    private final SellerService sellerService;
    private final UserService userService;

    /**
     * 판매자 입점 현황 데이터를 조회하는 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/entry/sellers")
    public ApiResponseEntity<Object> getSellerEntryList(
            @RequestParam(value = "start-at") LocalDate startAt,
            @RequestParam(value = "end-at") LocalDate endAt,
            @Valid @ValidEnum(enumClass = EntryStatus.class) @RequestParam(value = "entry-status", required = false) EntryStatus[] entryStatus,
            @PageableDefault(size = 6, sort = "requestAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SimpleSellerEntryDTO> entryDTOList = sellerService.getSellerEntryList(startAt, endAt, entryStatus, pageable);
        return ApiResponseEntity.builder()
                .data(entryDTOList)
                .build();
    }

    /**
     * 판매자 입점 관련 데이터 조회 API
     *
     * @param userId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/entry")
    public ApiResponseEntity<DetailedSellerEntryDTO> getDetailedSellerEntry(@RequestParam(value = "user-id") Long userId) {
        DetailedSellerEntryDTO detailedSellerEntry = sellerService.getDetailedSellerEntry(userId);
        return ApiResponseEntity.<DetailedSellerEntryDTO>builder()
                .data(detailedSellerEntry)
                .build();
    }


    /**
     * 판매자 입점 요청 상태 변경 API
     *
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/entry/sellers/entry-status")
    public ApiResponseEntity<Boolean> changeEntryStatus(
            @Valid @RequestBody ChangeSellerEntryStatusDTO request) {
        Boolean result = sellerService.changeEntryStatus(request);
        return ApiResponseEntity.<Boolean>builder()
                .data(result)
                .build();
    }

    /**
     * 판매자 로그인 API
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("auth/login")
    public ApiResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserDTO userDTO = userService.login(loginDTO, UserType.ROLE_APPROVED_SELLER);
        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
    }
}
