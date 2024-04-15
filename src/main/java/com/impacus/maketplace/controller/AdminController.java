package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {


    /**
     * 설계도 - 추후 API 개발 하면서 정리 예정
     *
     * 1) /api/v1/admin/list : Get 방식 할지 Post 로 통일 할지 고민중
     *  - 사용 목적 : 어드민 계정 목록 표시
     *  - Admin Name / Email / Account type / 전화번호 / 최근 활동 / 활동 내역 / 비밀번호 /
     *
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
