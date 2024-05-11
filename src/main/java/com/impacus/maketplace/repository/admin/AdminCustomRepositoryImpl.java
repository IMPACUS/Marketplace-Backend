package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.dto.admin.*;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.admin.QAdminActivityLog;
import com.impacus.maketplace.entity.admin.QAdminInfo;
import com.impacus.maketplace.entity.admin.QAdminLoginLog;
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


}
