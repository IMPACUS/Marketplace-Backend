package com.impacus.maketplace.controller.admin;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.admin.*;
import com.impacus.maketplace.dto.user.request.LoginDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.admin.AdminActivityLog;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.admin.AdminLoginLog;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.admin.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;

    /**
     * 1) 사용 목적 : 어드민 계정 목록 표시
     *
     * @return : 관리자 회원 리스트 출력
     */
    @GetMapping()
    public ApiResponseEntity<?> displayAdminList(
            @PageableDefault(size=6) Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Slice<AdminUserDTO> adminUserDto = adminService.displayAdmins(pageable, search);

        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("관리자 계정 전체 조회 성공")
                .data(adminUserDto)
                .build();
    }


    /**
     * 2) /api/v1/admin/register/login-history
     * - 사용 목적 : 로그인 로그 남기기 위한 히스토리 등록
     *
     * @param adminLoginHistoryDTO : 로그인 생성에 필요한 파라미터 등록 (userId : 회원번호, status : 로그인/로그아웃 상태 등록)
     * @return : 쿼리 결과 값 출력
     */
    @PostMapping("register/login-history")
    public ApiResponseEntity<?> registerLoginHistory(@RequestBody AdminLoginHistoryDTO adminLoginHistoryDTO) {
        AdminLoginLog adminLoginLog = adminService.createAdminLoginHistory(adminLoginHistoryDTO);
        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("로그인 히스토리 등록 성공")
                .data(adminLoginLog)
                .build();
    }

    /**
     * 3) /api/v1/admin/login-history: Get
     * - 로그인 내역
     *
     * @param adminId
     * @return
     */
    @GetMapping("login-history")
    public ApiResponseEntity<?> displayAdminsHistory(
            @RequestParam("adminId") Long adminId,
            @PageableDefault(size = 5) Pageable pageable) {
        Slice<AdminLoginHistoryDTO> adminLoginHistorySlice = adminService.displayAdminsHistory(adminId, pageable);

        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("로그인 내역 조회 성공")
                .data(adminLoginHistorySlice)
                .build();
    }


    /**
     * 4) /api/v1/admin/register/activity-history : Post
     *  - 활동 내역 로그 추가
     * @param adminLoginActivityDTO : 필요 데이터 삽입
     * @return : 추가된 데이터 반환
     */
    @PostMapping("register/activity-history")
    public ApiResponseEntity<?> registerActivityHistory(@RequestBody AdminLoginActivityDTO adminLoginActivityDTO) {
        AdminActivityLog adminActivityLog = adminService.registerActivityHistory(adminLoginActivityDTO);
        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("관리자 활동 등록 조회 성공")
                .data(adminActivityLog)
                .build();
    }


    /**
     *  5) /api/v1/admin/info-changed : Patch
     * @param adminChangeDTO : adminId, accountType 만 불려와 담는다.
     * @return : userId 가 업데이트 된 정보를 반환한다.
     */
    @PatchMapping("info-changed")
    public ApiResponseEntity<?> reWriteAdminInfoChanged(@RequestBody AdminChangeDTO adminChangeDTO) {
        log.info("controller.reWriteAdminType");
        AdminInfo adminInfo = adminService.reWriteAdminInfoChanged(adminChangeDTO);
        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("관리자 타입 변경 성공")
                .data(adminInfo)
                .build();
    }

    /**
     * * 6) /api/v1/admin/activity-history : Get 할지 Post 할지 고민중
     *      *  - 사용 목적 : 활동 내역
     *      *
     */
    @GetMapping("/activity-history")
    public ApiResponseEntity<?> displayViewActivityHistory(
            @RequestParam("adminId") Long adminId,
            @PageableDefault(size = 5) Pageable pageable) {
        log.info("controller.displayViewActivityHistory");
        Slice<AdminLoginActivityDTO> adminLoginActivitySlice = adminService.displayViewActivityHistory(adminId, pageable);

        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("관리자 활동 내역 조회 성공")
                .data(adminLoginActivitySlice)
                .build();
    }

    /**
     * (7) /api/v1/admin/register-admin-form 관리자 등록 폼 - 일반 등록이라 POST로 지정, 실제는 isAdmin = true로만 변경
     *       사실상 isAdmin만 true 하면 되지만 확장성을 고려해 그냥 데이터 다 받는 것으로만 지정
     * @param profileImage : 프로필 이미지
     * @param adminFormDTO : 프로필을 제외한 나머지만 adminFormDTO로 지정
     * @return
     */
    @PostMapping("register-admin-form")
    public ApiResponseEntity<?> registerAdminForm(
            @RequestPart(value = "profileImage", required = false) @Valid MultipartFile profileImage,
            @RequestPart(value = "adminFormDTO", required = false) AdminFormDTO adminFormDTO
            ) {
        // 프로필은 그냥 수정만 진행, 현재 의미가 없어서 일단 주석으로만 설명
        // 유효성 체크가 필요하면 향후 적용 예정

        // 실제로는 isAdmin 값만 true 하면 됨
        AdminInfo adminInfo = adminService.registerAdminForm(adminFormDTO);

        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("관리자 등록 성공")
                .data(adminInfo)
                .build();
    }

    /**
     * (8) group by 지정 - ADMIN, OWNER 별로 권한 갯수 지정
     * @return : 권한 부여
     */
    @GetMapping("group-counter")
    public ApiResponseEntity<?> displayGroupCounter() {
        List<AdminGroupCountDTO> result = adminService.displayGroupCounter();
        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("조회 성공")
                .data(result)
                .build();
    }
/*
     * (9) 기존 아이디 들어있는지 ID 검사
     * @param adminIdName 대상 아이디 값 조회
     * @return
     */
    @GetMapping("checked-id")
    public ApiResponseEntity<?> doCheckingUserId(
            @RequestParam("adminIdName") String adminIdName
    ) {
        Boolean b = adminService.doCheckingUserId(adminIdName);

        Map result = new HashMap();
        result.put("isChecked", b);

        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("아이디 중복 검사 성공")
                .data(result)
                .build();
    }

        /**
     * 관리자 로그인 API
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("auth/login")
    public ApiResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserDTO userDTO = userService.login(loginDTO, UserType.ROLE_ADMIN);
        return ApiResponseEntity.<UserDTO>builder()
                .data(userDTO)
                .build();
        }
}
