package com.impacus.maketplace.controller.user;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.auth.request.EmailRequest;
import com.impacus.maketplace.dto.auth.request.EmailVerificationRequest;
import com.impacus.maketplace.dto.excel.ExcelSheetData;
import com.impacus.maketplace.dto.excel.SXSSFExcelFile;
import com.impacus.maketplace.dto.user.request.UpdateUserDTO;
import com.impacus.maketplace.dto.user.request.UserRewardDTO;
import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;
import com.impacus.maketplace.dto.user.response.WebUserDTO;
import com.impacus.maketplace.dto.user.response.WebUserDetailDTO;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.user.WebUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final WebUserService readUserService;

    /**
     * 이메일 인증 코드 요청 API
     *
     * @param request
     * @return
     */
    @PostMapping("/email/verification-request")
    public ApiResponseEntity<Object> sendVerificationCodeToEmail(@Valid @RequestBody EmailRequest request) {
        userService.sendVerificationCodeToEmail(request.getEmail(), UserType.ROLE_CERTIFIED_USER);
        return ApiResponseEntity
                .builder()
                .build();
    }

    /**
     * 이메일 인증 API
     *
     * @param request
     * @return
     */
    @PostMapping("/email/confirm")
    public ApiResponseEntity<Object> confirmEmail(@Valid @RequestBody EmailVerificationRequest request) {
        boolean result = userService.confirmEmail(request);
        return ApiResponseEntity
                .builder()
                .data(result)
                .build();
    }

    /**
     * [관리자] 이메일로 사용자 정보 조회
     *
     * @param email
     * @return
     */
    @GetMapping("/summary")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<ReadUserSummaryDTO> getUserSummary(
            @Valid @Email @RequestParam String email
    ) {
        ReadUserSummaryDTO dto = readUserService.getUserSummary(email);
        return ApiResponseEntity.<ReadUserSummaryDTO>builder()
                .message("사용자 프로필 검색 성공")
                .data(dto)
                .build();
    }

    /**
     * [관리자] 소비자 회원 검색 목록 조회 API
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Page<WebUserDTO>> getUsers(
            @PageableDefault(size = 6, sort = "registerAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "user-name", required = false) String userName,
            @RequestParam(value = "phone-number", required = false) String phoneNumber,
            @RequestParam(value = "start-at", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(value = "end-at", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
            @RequestParam(value = "oauth-provider-type", required = false) OauthProviderType oauthProviderType,
            @RequestParam(value = "user-status", required = false) UserStatus status
    ) {
        Page<WebUserDTO> result = readUserService.getUsers(pageable, userName, phoneNumber, startAt, endAt, oauthProviderType, status);

        try {
            new SXSSFExcelFile(ExcelSheetData.of(result.getContent(), WebUserDTO.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ApiResponseEntity.<Page<WebUserDTO>>builder()
                .message("회원 검색 목록 조회 성공")
                .data(result)
                .build();
    }

    /**
     * [관리자] 회원님의 정보 관리 API
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<WebUserDetailDTO> getUser(
            @PathVariable(name = "userId") Long userId
    ) {
        WebUserDetailDTO result = readUserService.getUser(userId);
        return ApiResponseEntity.<WebUserDetailDTO>builder()
                .message("회원님의 정보 관리 성공")
                .data(result)
                .build();
    }

    /**
     * [관리자] 소비자 정보 변경 API
     */
    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Void> updateUser(
            @PathVariable(name = "userId") Long userId,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @Valid @RequestPart(value = "user") UpdateUserDTO dto
    ) {
        readUserService.updateUser(userId, profileImage, dto);
        return ApiResponseEntity.<Void>builder()
                .message("소비자 정보 변경 성공")
                .build();
    }

    /**
     * [관리자] 포인트/쿠폰 지급 API
     */
    @PatchMapping("/{userId}/reward")
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Void> issueUserReward(
            @PathVariable(name = "userId") Long userId,
            @Valid @RequestBody UserRewardDTO dto
    ) {
        readUserService.issueUserReward(userId, dto);
        return ApiResponseEntity.<Void>builder()
                .message("포인트/쿠폰 지급 성공")
                .build();
    }
}
