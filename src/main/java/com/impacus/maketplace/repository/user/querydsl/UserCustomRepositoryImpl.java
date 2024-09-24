package com.impacus.maketplace.repository.user.querydsl;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPoint;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPointAllocation;
import com.impacus.maketplace.entity.point.levelPoint.QLevelAchievement;
import com.impacus.maketplace.entity.point.levelPoint.QLevelPointMaster;
import com.impacus.maketplace.entity.user.QUser;
import com.impacus.maketplace.entity.user.QUserConsent;
import com.impacus.maketplace.entity.user.QUserRole;
import com.impacus.maketplace.entity.user.QUserStatusInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;
    private final QLevelPointMaster levelPointMaster = QLevelPointMaster.levelPointMaster;
    private final QGreenLabelPoint greenLabelPoint = QGreenLabelPoint.greenLabelPoint1;
    private final QAttachFile attachFile = QAttachFile.attachFile;
    private final QUserConsent userConsent = QUserConsent.userConsent;
    private final QUserRole userRole = QUserRole.userRole;
    private final QUserStatusInfo userStatusInfo = QUserStatusInfo.userStatusInfo;
    private final QGreenLabelPointAllocation labelPointAllocation = QGreenLabelPointAllocation.greenLabelPointAllocation;
    private final QLevelAchievement levelAchievement = QLevelAchievement.levelAchievement;

    @Override
    public ReadUserSummaryDTO findUserSummaryByEmail(String email) {
        BooleanBuilder userBuilder = new BooleanBuilder();
        userBuilder.and(user.type.eq(UserType.ROLE_CERTIFIED_USER))
                .and(user.email.contains(email));

        return queryFactory
                .select(
                        Projections.constructor(
                                ReadUserSummaryDTO.class,
                                user.id.as("userId"),
                                user.name,
                                levelPointMaster.userLevel,
                                user.email,
                                greenLabelPoint.greenLabelPoint,
                                levelPointMaster.levelPoint,
                                user.phoneNumber,
                                attachFile.attachFileName.as("profileImageUrl"),
                                user.createAt.as("registerAt")
                        )
                )
                .from(user)
                .innerJoin(levelPointMaster).on(levelPointMaster.userId.eq(user.id))
                .innerJoin(greenLabelPoint).on(greenLabelPoint.userId.eq(user.id))
                .leftJoin(attachFile).on(user.profileImageId.eq(attachFile.id))
                .where(userBuilder)
                .fetchFirst();
    }

    @Override
    public List<Long> findUserIdByUserLevel(UserLevel userLevel) {
        BooleanBuilder userBuilder = new BooleanBuilder();
        userBuilder.and(user.type.eq(UserType.ROLE_CERTIFIED_USER))
                .and(user.id.eq(levelPointMaster.userId))
                .and(user.isDeleted.eq(false));

        return queryFactory
                .select(user.id)
                .from(user)
                .where(userBuilder)
                .innerJoin(levelPointMaster).on(levelCondition(userLevel))
                .fetch();
    }

    private BooleanExpression levelCondition(UserLevel userLevel) {
        return userLevel != null ? levelPointMaster.userLevel.eq(userLevel) : Expressions.TRUE;
    }

    @Override
    public void deleteConsumer(Long userId) {
        queryFactory.delete(user)
                .where(user.id.eq(userId))
                .execute();
        queryFactory.delete(userConsent)
                .where(userConsent.userId.eq(userId))
                .execute();
        queryFactory.delete(userRole)
                .where(userRole.userId.eq(userId))
                .execute();
        queryFactory.delete(userStatusInfo)
                .where(userStatusInfo.userId.eq(userId))
                .execute();
        queryFactory.delete(greenLabelPoint)
                .where(greenLabelPoint.userId.eq(userId))
                .execute();
        queryFactory.delete(labelPointAllocation)
                .where(labelPointAllocation.userId.eq(userId))
                .execute();
        queryFactory.delete(levelPointMaster)
                .where(levelPointMaster.userId.eq(userId))
                .execute();
        queryFactory.delete(levelAchievement)
                .where(levelAchievement.userId.eq(userId))
                .execute();
    }
}
