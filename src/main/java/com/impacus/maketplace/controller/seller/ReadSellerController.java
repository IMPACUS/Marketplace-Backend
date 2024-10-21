package com.impacus.maketplace.controller.seller;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.auth.request.PasswordDTO;
import com.impacus.maketplace.dto.auth.response.CheckMatchedPasswordDTO;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusIdDTO;
import com.impacus.maketplace.dto.seller.response.*;
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
import java.util.List;

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
            @RequestParam(value = "entry-status", required = false) EntryStatus[] entryStatus,
            @RequestParam(value = "brand-name", required = false) String brandName,
            @PageableDefault(size = 6, sort = "requestAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SimpleSellerEntryDTO> entryDTOList = readSellerService.getSellerEntryList(
                startAt, endAt, entryStatus, brandName, pageable
        );
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
     * [판매자 판매자 정보 관리 데이터 조회
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

    /**
     * 모든 판매자의 마켓명을 반환하는 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @GetMapping("market")
    public ApiResponseEntity<List<SellerMarketNamesDTO>> findAllMarketName() {
        List<SellerMarketNamesDTO> dtos = readSellerService.findSellerNames();

        return ApiResponseEntity
                .<List<SellerMarketNamesDTO>>builder()
                .message("판매자 마켓명 반환 성공")
                .data(dtos)
                .build();
    }

    /**
     * [판매자] 판매자 목록 조회 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @GetMapping
    public ApiResponseEntity<Page<SellerDTO>> getSellers(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "start-at") LocalDate startAt,
            @RequestParam(value = "end-at") LocalDate endAt,
            @RequestParam(value = "brand-name", required = false) String brandName,
            @RequestParam(value = "contact-name", required = false) String contactName,
            @RequestParam(value = "status", required = false) UserStatus status
    ) {
        Page<SellerDTO> dtos = readSellerService.getSellers(
                pageable,
                brandName,
                contactName,
                status,
                startAt,
                endAt
        );
        return ApiResponseEntity
                .<Page<SellerDTO>>builder()
                .message("판매자 목록 조회 성공")
                .data(dtos)
                .build();
    }

    /**
     * 판매자 목록 엑셀 생성 요청 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @GetMapping("/excel")
    public ApiResponseEntity<FileGenerationStatusIdDTO> exportSellers(
            @RequestParam(value = "brand-name", required = false) String brandName,
            @RequestParam(value = "contact-name", required = false) String contactName,
            @RequestParam(value = "status", required = false) UserStatus status
    ) {
        FileGenerationStatusIdDTO result = readSellerService.exportSellers(
                brandName,
                contactName,
                status
        );
        return ApiResponseEntity
                .<FileGenerationStatusIdDTO>builder()
                .message("판매자 목록 엑셀 생성 성공")
                .data(result)
                .build();
    }

    /**
     * [관리자] 판매자 조회 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @GetMapping("{sellerId}")
    public ApiResponseEntity<SimpleSellerFromAdminDTO> getSellerInformationForWeb(@PathVariable Long sellerId) {
        SimpleSellerFromAdminDTO dto = readSellerService.getSellerInformationFroWeb(sellerId);

        return ApiResponseEntity
                .<SimpleSellerFromAdminDTO>builder()
                .message("판매자 정보 조회 성공")
                .data(dto)
                .build();
    }

    /**
     * [앱] 상품의 판매자 정보 조회 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/brand/{sellerId}")
    public ApiResponseEntity<AppSellerDTO> getSellerInformationForApp(@PathVariable Long sellerId) {
        AppSellerDTO dto = readSellerService.getSellerInformationForApp(sellerId);

        return ApiResponseEntity
                .<AppSellerDTO>builder()
                .message("판매자 정보 조회 성공")
                .data(dto)
                .build();
    }
}
