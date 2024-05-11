package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.dto.admin.*;
import com.impacus.maketplace.entity.admin.*;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminCustomRepositoryImpl implements AdminCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QAdminInfo adminInfo = QAdminInfo.adminInfo;
    private final QAdminLoginLog adminLoginLog = QAdminLoginLog.adminLoginLog;

    private final QAdminActivityLog adminActivityLog = QAdminActivityLog.adminActivityLog;

    private final QUser userEntity = QUser.user;


    /**
     * 쿼리문 : 관리자 전체 조회 (리스트 형태)
     *
     * @return : [리스트] 관리자 회원 조회
     */
    @Override
    public List<AdminUserDTO> findAdminAll() {
        JPAQuery<AdminUserDTO> query = queryFactory.select(
                        new QAdminUserDTO(
                                adminInfo.id,
                                userEntity.id,
                                userEntity.email,
                                userEntity.password,
                                adminInfo.accountType,
                                adminInfo.activityDetail
                        )
                )
                .from(userEntity)
                .innerJoin(adminInfo)
                .on(userEntity.id.eq(adminInfo.userId))
                .where(userEntity.isAdmin)
                .fetchJoin();
        return query.fetch();
    }


    /**
     * 로그인 내역 조회
     * @param userId : 해당 유저 번호
     * @return : 로그인 히스토리 전체 조회
     */
    @Override
    public List<AdminLoginHistoryDTO> findAdminLoginHistoryAll(Long userId) {
        return queryFactory.select(
                        new QAdminLoginHistoryDTO(
                                adminLoginLog.id,
                                adminLoginLog.crtDate,
                                adminLoginLog.status
                        )
                )
                .from(adminLoginLog)
                .where(adminLoginLog.userId.eq(userId))
                .fetch();
    }


    /**
     * 관리자 타입 수정을 위한 로직
     * @param userId : 관리자 타입을 변경하기 위한 유저번호
     * @return : 관리자 타입 변경된 해당 유저 정보 조회
     */
    @Override
    public AdminInfo findAdminInfoWhereUserId(Long userId) {
        AdminInfoDTO adminInfoDTO = queryFactory.select(
                new QAdminInfoDTO(
                        adminInfo.id,
                        adminInfo.userId,
                        adminInfo.accountType,
                        adminInfo.activityDetail
                )
        ).from(adminInfo).where(adminInfo.userId.eq(userId)).fetchOne();

        return AdminInfo
                .builder()
                .id(adminInfoDTO.getId())
                .userId(adminInfoDTO.getUserId())
                .accountType(adminInfoDTO.getAccountType())
                .activityDetail(adminInfoDTO.getActivityDetail())
                .build();
    }


    /**
     * 관리자 활동 정보를 전체 리스트 출력
     * @param userId : 해당 관리자 번호 검색 조건 출력
     * @return : 활동정보 전체 출력
     */
    @Override
    public List<AdminLoginActivityDTO> findAdminActivityLogAll(Long userId) {
        return queryFactory.select(
                new QAdminLoginActivityDTO(
                        adminActivityLog.crtDate,
                        adminActivityLog.activityDetail
                )
        ).from(adminActivityLog).where(adminActivityLog.userId.eq(userId)).fetch();
    }


}
