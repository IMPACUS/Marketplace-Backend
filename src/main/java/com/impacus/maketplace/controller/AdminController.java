package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.admin.AdminInfoDTO;
import com.impacus.maketplace.dto.admin.AdminLoginActivityDTO;
import com.impacus.maketplace.dto.admin.AdminLoginHistoryDTO;
import com.impacus.maketplace.dto.admin.AdminUserDTO;
import com.impacus.maketplace.entity.admin.AdminActivityLog;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.admin.AdminLoginLog;
import com.impacus.maketplace.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    /**
     * 설계도 - 추후 API 개발 하면서 정리 예정
     *
     *  7) /api/v1/admin/te : Post
     *  - 사용 목적 : 어드민 등록
     *
     *

     */

    /**
     * 1) 사용 목적 : 어드민 계정 목록 표시
     *
     * @return : 관리자 회원 리스트 출력
     */
    @GetMapping("/")
    public ApiResponseEntity<?> displayAdminList() {
        // 하드코딩으로 연동 먼저 테스트 진행
        List<AdminUserDTO> adminUserDto = adminService.displayAdmins();

        return ApiResponseEntity
                .builder()
                .data(adminUserDto)
                .build();
    }


    /**
     * 2) /api/v1/admin/register/login/history
     * - 사용 목적 : 로그인 로그 남기기 위한 히스토리 등록
     *
     * @param adminLoginHistoryDTO : 로그인 생성에 필요한 파라미터 등록 (userId : 회원번호, status : 로그인/로그아웃 상태 등록)
     * @return : 쿼리 결과 값 출력
     */
    @PostMapping("register/login/history")
    public ApiResponseEntity<?> registerLoginHistory(@RequestBody AdminLoginHistoryDTO adminLoginHistoryDTO) {
        AdminLoginLog adminLoginLog = adminService.createAdminLoginHistory(adminLoginHistoryDTO);
        return ApiResponseEntity
                .builder()
                .data(adminLoginLog)
                .build();
    }

    /**
     * 3) /api/v1/admin/login/history: Get
     * - 로그인 내역
     *
     * @param userId
     * @return
     */
    @GetMapping("login/history")
    public ApiResponseEntity<?> displayAdminsHistory(@RequestParam("userId") Long userId) {
        List<AdminLoginHistoryDTO> adminLoginHistoryDTO = adminService.displayAdminsHistory(userId);
        return ApiResponseEntity
                .builder()
                .data(adminLoginHistoryDTO)
                .build();
    }


    /**
     * 4) /api/v1/admin/create/activity/log : Post
     *  - 활동 내역 로그 추가
     * @param adminLoginActivityDTO : 필요 데이터 삽입
     * @return : 추가된 데이터 반환
     */
    @PostMapping("register/activity/history")
    public ApiResponseEntity<?> registerActivityHistory(@RequestBody AdminLoginActivityDTO adminLoginActivityDTO) {
        AdminActivityLog adminActivityLog = adminService.registerActivityHistory(adminLoginActivityDTO);
        return ApiResponseEntity
                .builder()
                .data(adminActivityLog)
                .build();
    }


    /**
     *  5) /api/v1/admin/type : Patch
     * @param adminInfoDTO : userId, accountType 만 불려와 담는다.
     * @return : userId 가 업데이트 된 정보를 반환한다.
     */
    @PatchMapping("type")
    public ApiResponseEntity<?> reWriteAdminType(@RequestBody AdminInfoDTO adminInfoDTO) {
        log.info("controller.reWriteAdminType");
        AdminInfo adminInfo = adminService.reWriteAdminType(adminInfoDTO);
        return ApiResponseEntity
                .builder()
                .data(adminInfo)
                .build();
    }

    /**
     * * 6) /api/v1/admin/activity/history : Get 할지 Post 할지 고민중
     *      *  - 사용 목적 : 활동 내역
     *      *
     */
    @GetMapping("activity/history")
    public ApiResponseEntity<?> displayViewActivityHistory(@RequestParam("userId") Long userId) {
        log.info("controller.displayViewActivityHistory");
        List<AdminLoginActivityDTO> adminLoginActivityDTOS = adminService.displayViewActivityHistory(userId);
        return ApiResponseEntity
                .builder()
                .data(adminLoginActivityDTOS)
                .build();
    }


}
