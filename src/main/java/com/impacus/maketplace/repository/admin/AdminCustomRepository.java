package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.dto.admin.AdminUserListDto;

import java.util.List;

public interface AdminCustomRepository {
    List<AdminUserListDto> findAdminInfoList();
}
