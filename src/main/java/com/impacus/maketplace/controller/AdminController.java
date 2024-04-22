package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.admin.AdminUserListDto;
import com.impacus.maketplace.dto.coupon.response.CouponListDto;
import com.impacus.maketplace.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 2) /api/v1/admin/create : Post
     *  - 사용 목적 : 어드민 등록
     *
     *
     * 3) /api/v1/admin/login/log/list : Get 할지 Post로 통일하여 할지 고민 중
     *  - 사용 목적 : 로그인 내역
     *
     *
     * 4) /api/v1/admin/activity/log : Get 할지 Post 할지 고민중
     *  - 사용 목적 : 활동 내역
     *
     *
     * 5) /api/v1/admin/update/type : Post
     *  - 사용 목적 : 계정 유형 변경
     *
     *
     * 6) /api/v1/admin/create/activity/log : Post
     *   - 활동 내역 로그 추가
     *
     *
     * 7) /api/v1/admin/create/login/log : Post
     *   - 로그인 로그 추가
     *
     */

    /**
     * 1) 사용 목적 : 어드민 계정 목록 표시
     * @return : 관리자 회원 리스트 출력
     */
    @GetMapping("/")
    public ApiResponseEntity<?> displayAdminList() {
        // 하드코딩으로 연동 먼저 테스트 진행
        List<AdminUserListDto> adminUserListDto = adminService.displayAdmins();

        return ApiResponseEntity
                .builder()
                .data(adminUserListDto)
                .build();
    }


    // 여기는 api 통신하는지만 테스트용
    @GetMapping("/test")
    public ApiResponseEntity<?> test() {
        String result = "test";

        return ApiResponseEntity
                .builder()
                .data(result)
                .build();
    }
}
