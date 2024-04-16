package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.dto.admin.AdminUserListDto;
import com.impacus.maketplace.dto.admin.QAdminUserListDto;
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


    @Override
    public List<AdminUserListDto> findAdminInfoList() {
        JPAQuery<AdminUserListDto> query = queryFactory.select(
                new QAdminUserListDto(
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
}
