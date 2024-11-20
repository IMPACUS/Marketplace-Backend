package com.impacus.maketplace.repository.user.querydsl;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.auth.CertificationResult;
import com.impacus.maketplace.dto.common.request.CouponIdsDTO;
import com.impacus.maketplace.dto.user.CommonUserDTO;
import com.impacus.maketplace.dto.user.PhoneNumberDTO;
import com.impacus.maketplace.dto.user.request.UpdateUserDTO;
import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;
import com.impacus.maketplace.dto.user.response.WebUserDTO;
import com.impacus.maketplace.dto.user.response.WebUserDetailDTO;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.impacus.maketplace.entity.consumer.QConsumer;
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
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QUser user = QUser.user;
    private final QLevelPointMaster levelPointMaster = QLevelPointMaster.levelPointMaster;
    private final QGreenLabelPoint greenLabelPoint = QGreenLabelPoint.greenLabelPoint1;
    private final QAttachFile attachFile = QAttachFile.attachFile;
    private final QUserConsent userConsent = QUserConsent.userConsent;
    private final QUserRole userRole = QUserRole.userRole;
    private final QUserStatusInfo userStatusInfo = QUserStatusInfo.userStatusInfo;
    private final QGreenLabelPointAllocation labelPointAllocation = QGreenLabelPointAllocation.greenLabelPointAllocation;
    private final QLevelAchievement levelAchievement = QLevelAchievement.levelAchievement;
    private final QConsumer consumer = QConsumer.consumer;

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
                                user.phoneNumberPrefix,
                                user.phoneNumberSuffix,
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
                .and(userStatusInfo.status.eq(UserStatus.ACTIVE))
                .and(user.isDeleted.eq(false));

        return queryFactory
                .select(user.id)
                .from(user)
                .where(userBuilder)
                .innerJoin(levelPointMaster).on(levelCondition(userLevel))
                .innerJoin(userStatusInfo).on(userStatusInfo.userId.eq(user.id))
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

    @Override
    public Page<WebUserDTO> getUsers(
            Pageable pageable,
            String userName,
            String phoneNumber,
            LocalDate startAt,
            LocalDate endAt,
            OauthProviderType oauthProviderType,
            UserStatus status
    ) {
        BooleanBuilder builder = getBooleanBuilderForWebUserDTO(
                userName,
                phoneNumber,
                startAt,
                endAt,
                oauthProviderType,
                status
        );

        // 데이터 검색
        List<WebUserDTO> dtos = getWebUserDTOs(
                pageable,
                builder
        );

        long count = queryFactory
                .select(
                        user.id.count()
                )
                .from(user)
                .innerJoin(levelPointMaster).on(user.id.eq(levelPointMaster.userId))
                .innerJoin(userStatusInfo).on(user.id.eq(userStatusInfo.userId))
                .where(builder)
                .fetchOne();

        return PaginationUtils.toPage(dtos, pageable, count);
    }

    private BooleanBuilder getBooleanBuilderForWebUserDTO(
            String userName,
            String phoneNumber,
            LocalDate startAt,
            LocalDate endAt,
            OauthProviderType oauthProviderType,
            UserStatus status
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        // 필터링
        builder.and(user.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
                .and(user.type.in(new UserType[]{UserType.ROLE_CERTIFIED_USER, UserType.ROLE_DEACTIVATED_USER}));
        if (userName != null && !userName.isBlank()) {
            builder.and(user.name.containsIgnoreCase(userName));
        }
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            builder.and(user.phoneNumberSuffix.containsIgnoreCase(phoneNumber));
        }
        if (oauthProviderType != null) {
            builder.and(user.email.contains(oauthProviderType.name()));
        }
        if (status != null) {
            builder.and(userStatusInfo.status.eq(status));
        }

        return builder;
    }

    private List<WebUserDTO> getWebUserDTOs(
            Pageable pageable,
            BooleanBuilder builder
    ) {
        JPAQuery<WebUserDTO> query = queryFactory
                .select(
                        Projections.constructor(
                                WebUserDTO.class,
                                user.id,
                                user.name,
                                user.email,
                                user.phoneNumberPrefix,
                                user.phoneNumberSuffix,
                                levelPointMaster.userLevel,
                                user.createAt,
                                user.recentLoginAt
                        )
                )
                .from(user)
                .innerJoin(levelPointMaster).on(user.id.eq(levelPointMaster.userId))
                .innerJoin(userStatusInfo).on(user.id.eq(userStatusInfo.userId))
                .where(builder)
                .orderBy(user.createAt.desc());

        if (pageable != null) {
            return query
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetch();
        } else {
            return query.fetch();
        }
    }

    @Override
    public WebUserDetailDTO getUser(Long userId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                WebUserDetailDTO.class,
                                user.id.as("userId"),
                                attachFile.attachFileName.as("profileImageUrl"),
                                user.email,
                                user.password,
                                user.name,
                                user.phoneNumberPrefix,
                                user.phoneNumberSuffix,
                                user.createAt.as("registerAt"),
                                levelPointMaster.userLevel,
                                levelPointMaster.levelPoint,
                                greenLabelPoint.greenLabelPoint,
                                userStatusInfo.status.as("userStatus")
                        )
                )
                .from(user)
                .leftJoin(attachFile).on(attachFile.id.eq(user.profileImageId))
                .innerJoin(levelPointMaster).on(user.id.eq(levelPointMaster.userId))
                .innerJoin(userStatusInfo).on(user.id.eq(userStatusInfo.userId))
                .innerJoin(greenLabelPoint).on(greenLabelPoint.userId.eq(userId))
                .where(user.id.eq(userId))
                .fetchFirst();
    }

    @Override
    public long updateUser(Long userId, UpdateUserDTO dto, Long profileImageId) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        long result = queryFactory.update(user)
                .set(user.profileImageId, profileImageId)
                .set(user.modifyAt, LocalDateTime.now())
                .set(user.modifyId, currentAuditor)
                .where(user.id.eq(userId))
                .execute();

        queryFactory.update(userStatusInfo)
                .set(userStatusInfo.status, dto.getUserStatus())
                .set(userStatusInfo.statusReason, "관리자에 의한 회원 상태 변경")

                .set(userStatusInfo.modifyAt, LocalDateTime.now())
                .set(userStatusInfo.modifyId, currentAuditor)
                .where(userStatusInfo.userId.eq(userId))
                .execute();

        return result;
    }

    @Override
    public CommonUserDTO findCommonUserByEmail(String email) {
        return queryFactory
                .select(
                        Projections.fields(
                                CommonUserDTO.class,
                                user.id.as("userId"),
                                user.email,
                                user.password,
                                user.name,
                                user.type,
                                userStatusInfo.status
                        )
                )
                .from(user)
                .innerJoin(userStatusInfo).on(userStatusInfo.userId.eq(user.id))
                .where(user.email.eq(email))
                .fetchFirst();
    }

    @Override
    public List<WebUserDTO> findUsersByIds(
            CouponIdsDTO dto
    ) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(user.id.in(dto.getIds()));

        // 데이터 검색
        return getWebUserDTOs(
                null,
                builder
        );
    }

    @Override
    public void saveOrUpdateCertification(Long userId, CertificationResult certificationResult) {
        PhoneNumberDTO dto = new PhoneNumberDTO(certificationResult.getMobileNo());

        // 1. User 저장 업데이트
        queryFactory.update(user)
                .set(user.type, UserType.ROLE_CERTIFIED_USER)
                .set(user.jumin1, certificationResult.getBirthdate())
                .set(user.phoneNumberPrefix, dto.getPhoneNumberPrefix())
                .set(user.phoneNumberSuffix, dto.getPhoneNumberSuffix())
                .set(user.isCertPhone, true)
                .set(user.certPhoneAt, LocalDateTime.now())
                .set(user.modifyAt, LocalDateTime.now())
                .where(user.id.eq(userId))
                .execute();
    }
}
