package com.impacus.maketplace.service.admin;


import com.impacus.maketplace.dto.admin.AdminLoginHistoryDTO;
import com.impacus.maketplace.dto.admin.AdminUserDTO;
import com.impacus.maketplace.entity.admin.AdminLoginLog;
import com.impacus.maketplace.repository.admin.AdminInfoRepository;
import com.impacus.maketplace.repository.admin.AdminLoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class AdminService {
    private final AdminInfoRepository adminInfoRepository;
    private final AdminLoginLogRepository adminLoginLogRepository;

    /**
     * (1) [리스트] 관리자 회원 조회 - 쿼리문 결과 값 추출
     * @return : 쿼리문 결과 값 조회 (리스트 - 관리자)
     */
    @Transactional(readOnly = true)
    public List<AdminUserDTO> displayAdmins() {
        return adminInfoRepository.findAdminAll();
    }

    /**
     * (2) 관리자 로그인 히스토리(로그) 남기기
     * @param adminLoginHistoryDTO : 관리자 로그
     * @return : 생성된 로그인 히스토리(로그) dto 쿼리 정보
     */
    @Transactional(readOnly = false)
    public AdminLoginLog createAdminLoginHistory(AdminLoginHistoryDTO adminLoginHistoryDTO) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        AdminLoginLog adminLoginLog
                = AdminLoginLog
                    .builder()
                    .userId(adminLoginHistoryDTO.getUserId())
                    .status(adminLoginHistoryDTO.getStatus())
                    .crtDate(zonedDateTime)
                    .build();
        return adminLoginLogRepository.save(adminLoginLog);
    }

}
