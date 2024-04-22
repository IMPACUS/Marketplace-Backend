package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.dto.admin.AdminUserListDto;

import java.util.List;

public interface AdminCustomRepository {
    // 관리자 리스트 출력
    List<AdminUserListDto> findAdminAll();
}
