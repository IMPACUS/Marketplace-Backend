package com.impacus.maketplace.service.admin;


import com.impacus.maketplace.common.enumType.error.AdminErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.admin.*;
import com.impacus.maketplace.entity.admin.AdminActivityLog;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.admin.AdminLoginLog;
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
                .adminId(adminLoginHistoryDTO.getAdminId())
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
    public Slice<AdminLoginHistoryDTO> displayAdminsHistory(Long userId, Pageable pageable) {
        return adminLoginLogRepository.findAdminLoginHistoryAll(userId, pageable);
    }


    /**
     * (4) 로그인 후 활동 내역 추가
     *
     * @param adminLoginActivityDTO : 로그인 활동 로그 필요로 하는 파라미터 등록
     * @return : 활동 로그 등록
     */
    @Transactional(readOnly = false)
    public AdminActivityLog registerActivityHistory(AdminLoginActivityDTO adminLoginActivityDTO) {
        log.info(adminLoginActivityDTO);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        AdminActivityLog adminActivityLog
                = AdminActivityLog
                .builder()
                .adminId(adminLoginActivityDTO.getAdminId())
                .activityDetail(adminLoginActivityDTO.getActivityDetail())
                .crtDate(zonedDateTime)
                .build();
        return adminActivityLogRepository.save(adminActivityLog);
    }


    /**
     * (5) 권한(accountType) 을 지정한다.
     *
     * @param adminChangeDTO : entity 클래스 adminInfo 의 값을 그래도 받고 queryDSL에서 실행 하기 위한 용도
     * @return : adminInfo 형으로 기존 DB를 불려와 권한만 변경하여 Update 한다.
     */
    public AdminInfo reWriteAdminInfoChanged(AdminChangeDTO adminChangeDTO) {
        Optional<AdminInfo> optionalAdminInfo = adminInfoRepository.findById(adminChangeDTO.getAdminId());

        if (optionalAdminInfo.isEmpty()) {
            throw new RuntimeException("AdminInfo with ID " + adminChangeDTO.getAdminId() + " not found");
        }

        AdminInfo adminInfo = optionalAdminInfo.get();
        adminInfo.setAccountType(adminChangeDTO.getAccountType());

        return adminInfoRepository.save(adminInfo);
    }

    /**
     * (6) 로그 출력, 활동 내역 출력
     * @return : 관리자 활동 내역 리스트 형태로 출력
     */
    @Transactional(readOnly = true)
    public Slice<AdminLoginActivityDTO> displayViewActivityHistory(Long userId, Pageable pageable) {
        log.info("AdminService.displayViewActivityHistory()");
        return adminActivityLogRepository.findAdminActivityLogAll(userId, pageable);
    }

    /**
     * (7) 유저 등록 관련 API - 각각 필요한 쿼리별 수행
     * @param adminFormDTO
     * @return
     */
    @Transactional(readOnly = false)
    public AdminInfo registerAdminForm(AdminFormDTO adminFormDTO) {
        // 먼저 AdminFormDTO에서 받은 정보를 AdminInfo 에 등록하고 해당 폼 삽입
        AdminInfo adminInfo
                = AdminInfo
                .builder()
                .imgSrc(adminFormDTO.getImgSrc())
                .name(adminFormDTO.getName())
                .phoneNumber(adminFormDTO.getPhoneNumber())
                .email(adminFormDTO.getEmail())
                .addr(adminFormDTO.getAddr())
                .juminNo(adminFormDTO.getJuminNo())
                .accountType(adminFormDTO.getAccountType())
                .adminIdName(adminFormDTO.getAdminIdName())
                .password(adminFormDTO.getPassword())
                .build();
        AdminInfo result = adminInfoRepository.save(adminInfo);

        // 다음으로는 관리자 등록을 했으니 로그를 추가한다.
        AdminActivityLog activityLog = AdminActivityLog
                .builder()
                .adminId(result.getId())
                .crtDate(result.getRecentActivityDate())
                .activityDetail("어드민 계정 등록")
                .build();
        adminActivityLogRepository.save(activityLog);
        return result;
    }

    /**
     * (8) 그룹별 카운트
     * @return
     */
    @Transactional(readOnly = true)
    public List<AdminGroupCountDTO> displayGroupCounter() {
        return adminInfoRepository.displayGroupCounter();
    }

    /**
     * (9) 중복 체크 로직
     * @param adminIdName 아이디 중복 체크 다생 선택
     * @return
     */
    @Transactional(readOnly = true)
    public Boolean doCheckingUserId(String adminIdName) {
//        String s = adminInfoRepository.findByAdminIdName(adminIdName);
//        log.info(s);
        boolean b = adminInfoRepository.existsByAdminIdName(adminIdName);
        log.info(b);
        return b;
    }

    /**
     * adminIdName로 관리자를 조회하는 함수
     *
     * @param adminIdName
     * @return
     */
    @Transactional(readOnly = true)
    public AdminInfo findAdminInfoBYAdminIdName(String adminIdName) {
        return adminInfoRepository.findByAdminIdName(adminIdName)
                .orElseThrow(() -> new CustomException(AdminErrorType.NOT_EXISTED_ADMIN_ID_NAME));
    }
}
