package com.impacus.maketplace.service.admin;


import com.impacus.maketplace.dto.admin.*;
import com.impacus.maketplace.entity.admin.AdminActivityLog;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.admin.AdminLoginLog;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.repository.admin.AdminActivityLogRepository;
import com.impacus.maketplace.repository.admin.AdminInfoRepository;
import com.impacus.maketplace.repository.admin.AdminLoginLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class AdminService {
    private final AdminInfoRepository adminInfoRepository;
    private final AdminLoginLogRepository adminLoginLogRepository;
    private final AdminActivityLogRepository adminActivityLogRepository;


    /**
     * (1) [리스트] 관리자 회원 조회 - 쿼리문 결과 값 추출
     *
     * @return : 쿼리문 결과 값 조회 (리스트 - 관리자)
     */
    @Transactional(readOnly = true)
    public Slice<AdminUserDTO> displayAdmins(Pageable pageable, String search) {
        log.info("service.displayAdmins()");
        return adminInfoRepository.findAdminAll(pageable, search);
    }

    /**
     * (2) 관리자 로그인 히스토리(로그) 남기기
     *
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

    /**
     * (3) 로그인 히스토리
     *
     * @param userId : 해당 유저 번호로만 로그인 / 로그아웃 내역 출력
     * @return : 로그인, 로그아웃 등 히스토리 내역 리스트 형태로 출력
     */
    @Transactional(readOnly = true)
    public List<AdminLoginHistoryDTO> displayAdminsHistory(Long userId) {
        return adminLoginLogRepository.findAdminLoginHistoryAll(userId);
    }


    /**
     * (4) 로그인 후 활동 내역 추가
     *
     * @param adminLoginActivityDTO : 로그인 활동 로그 필요로 하는 파라미터 등록
     * @return : 활동 로그 등록
     */
    @Transactional(readOnly = true)
    public AdminActivityLog registerActivityHistory(AdminLoginActivityDTO adminLoginActivityDTO) {
        log.info(adminLoginActivityDTO);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        AdminActivityLog adminActivityLog
                = AdminActivityLog
                .builder()
                .userId(adminLoginActivityDTO.getUserId())
                .activityDetail(adminLoginActivityDTO.getActivityDetail())
                .crtDate(zonedDateTime)
                .build();
        return adminActivityLogRepository.save(adminActivityLog);
    }


    /**
     * (5) 권한(accountType) 을 지정한다.
     *
     * @param adminInfoDTO : entity 클래스 adminInfo 의 값을 그래도 받고 queryDSL에서 실행 하기 위한 용도
     * @return : adminInfo 형으로 기존 DB를 불려와 권한만 변경하여 Update 한다.
     */
    @Transactional(readOnly = false)
    public AdminInfo reWriteAdminType(AdminInfoDTO adminInfoDTO) {
        AdminInfo adminInfo = adminInfoRepository.findAdminInfoWhereUserId(adminInfoDTO.getUserId());
        adminInfo.setAccountType(adminInfoDTO.getAccountType());
        return adminInfoRepository.save(adminInfo);

    }

    /**
     * (6) 로그 출력, 활동 내역 출력
     * @return : 관리자 활동 내역 리스트 형태로 출력
     */
    @Transactional(readOnly = true)
    public List<AdminLoginActivityDTO> displayViewActivityHistory(Long userId) {
        List<AdminLoginActivityDTO> adminLoginActivityDTOS = adminActivityLogRepository.findAdminActivityLogAll(userId);
        return adminLoginActivityDTOS;
    }

    /**
     * (7) 관리자 등록에 필요한 필요한 폼의 대한 사용자 정보 출력
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public AdminFormDTO displayViewUserInfo(Long userId) {
        log.info("service.displayViewUserInfo");
        return adminInfoRepository.findAllWhereId(userId);
    }

    /**
     * (8) 유저 등록 관련 API - 각각 필요한 쿼리별 수행
     * @param adminFormDTO
     * @return
     */
    @Transactional(readOnly = false)
    public AdminFormDTO registerAdminForm(AdminFormDTO adminFormDTO) {
        Long result = adminInfoRepository.changeUserEntityAdminForm(adminFormDTO);
        log.info("유저 등록 결과 " + result);
        result = adminInfoRepository.changeAdminInfoAdminForm(adminFormDTO);
        log.info("관리자 등록 결과" + result);
        return adminFormDTO;
    }

}
