package com.impacus.maketplace.controller.seller;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.auth.request.PasswordDTO;
import com.impacus.maketplace.dto.auth.response.CheckMatchedPasswordDTO;
import com.impacus.maketplace.dto.seller.response.DetailedSellerDTO;
import com.impacus.maketplace.dto.seller.response.DetailedSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.SellerEntryStatusDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerEntryDTO;
import com.impacus.maketplace.dto.user.response.CheckExistedEmailDTO;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import com.impacus.maketplace.service.seller.ReadSellerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/seller")
public class ReadSellerController {
    private final ReadSellerService readSellerService;
    private final UserService userService;
    private final AuthService authService;

    /**
     * 판매자 입점 현황 데이터를 조회하는 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @GetMapping("entry-status")
    public ApiResponseEntity<SellerEntryStatusDTO> getEntryStatusStatistics() {
        SellerEntryStatusDTO sellerEntryStatusDTO = readSellerService.getEntryStatusStatistics();
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @GetMapping("/entry/sellers")
    public ApiResponseEntity<Object> getSellerEntryList(
            @RequestParam(value = "start-at") LocalDate startAt,
            @RequestParam(value = "end-at") LocalDate endAt,
            @Valid @ValidEnum(enumClass = EntryStatus.class) @RequestParam(value = "entry-status", required = false) EntryStatus[] entryStatus,
            @PageableDefault(size = 6, sort = "requestAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SimpleSellerEntryDTO> entryDTOList = readSellerService.getSellerEntryList(startAt, endAt, entryStatus, pageable);
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @GetMapping("/entry")
    public ApiResponseEntity<DetailedSellerEntryDTO> getDetailedSellerEntry(@RequestParam(value = "user-id") Long userId) {
        DetailedSellerEntryDTO detailedSellerEntry = readSellerService.getDetailedSellerEntry(userId);
        return ApiResponseEntity.<DetailedSellerEntryDTO>builder()
                .data(detailedSellerEntry)
                .build();
    }

    /**
     * 판매자 등록 이메일 중 요청한 이메일이 중복인지 확인
     *
     * @param email
     * @return
    */
    @GetMapping("/email")
    public ApiResponseEntity<CheckExistedEmailDTO> checkDuplicatedEmail(
            @Valid @Email @RequestParam(value = "email") String email
    ) {
        CheckExistedEmailDTO dto = userService.checkExistedEmailForSeller(email);
        return ApiResponseEntity.<CheckExistedEmailDTO>builder()
                .data(dto)
                .build();
    }

    /**
     * 비밀번호 일치여부를 확인하는 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PostMapping("/password")
    public ApiResponseEntity<CheckMatchedPasswordDTO> checkIsMatchPassword(
                    @AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody PasswordDTO dto) {
            CheckMatchedPasswordDTO result = authService.checkIsPasswordMatch(user.getId(),
                            UserType.ROLE_APPROVED_SELLER, dto.getPassword());
            return ApiResponseEntity
                            .<CheckMatchedPasswordDTO>builder()
                            .message("기존 비밀번호 일치 여부 확인 성공")
                            .data(result)
                            .build();
    }

    /**
     * 판매자 정보 관리 데이터 조회
     *
     * @param user
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @GetMapping("details")
    public ApiResponseEntity<DetailedSellerDTO> findSellerDetailInformation(@AuthenticationPrincipal CustomUserDetails user) {
        DetailedSellerDTO dto = readSellerService.findSellerDetailInformation(user.getId());
        return ApiResponseEntity
                .<DetailedSellerDTO>builder()
                .message("판매자 배송지 정보 수정 성공")
                .data(dto)
                .build();
    }
}
