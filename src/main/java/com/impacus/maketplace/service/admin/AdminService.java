package com.impacus.maketplace.service.admin;


import com.impacus.maketplace.dto.admin.AdminUserListDto;
import com.impacus.maketplace.repository.admin.AdminInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final AdminInfoRepository adminInfoRepository;

    /**
     * (1) [리스트] 관리자 회원 조회 - 쿼리문 결과 값 추출
     * @return : 쿼리문 결과 값 조회 (리스트 - 관리자)
     */
    public List<AdminUserListDto> displayAdmins() {
        return adminInfoRepository.findAdminAll();
    }


}
