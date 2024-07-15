package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.dto.admin.*;
import com.impacus.maketplace.entity.admin.AdminInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface AdminCustomRepository {
    // 관리자 리스트 출력
    Slice<AdminUserDTO> findAdminAll(Pageable pageable, String search);

    // 로그인 이력 조회 (커스텀마이징)
    Slice<AdminLoginHistoryDTO> findAdminLoginHistoryAll(Long adminId, Pageable pageable);


    // 활동 내역 출력
    Slice<AdminLoginActivityDTO> findAdminActivityLogAll(Long adminId, Pageable pageable);

    // 그룹별 조회
    List<AdminGroupCountDTO> displayGroupCounter();

    // 아이디 중복 체크
    Boolean existsByAdminIdName(String adminIdName);
}
