package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.dto.admin.AdminLoginHistoryDTO;
import com.impacus.maketplace.dto.admin.AdminUserDTO;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.admin.AdminLoginLog;

import java.util.List;

public interface AdminCustomRepository {
    // 관리자 리스트 출력
    List<AdminUserDTO> findAdminAll();

    // 로그인 이력 조회 (커스텀마이징)
    List<AdminLoginHistoryDTO> findAdminLoginHistoryAll(Long userId);

    // 해당 관리자 표시 (admin_info 테이블에서 user_id 조건문)
    AdminInfo findAdminInfoWhereUserId(Long userId);
}
