package com.impacus.maketplace.repository.admin;

import com.impacus.maketplace.dto.admin.*;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.admin.QAdminActivityLog;
import com.impacus.maketplace.entity.admin.QAdminInfo;
import com.impacus.maketplace.entity.admin.QAdminLoginLog;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    public Slice<AdminUserDTO> findAdminAll(Pageable pageable, String search) {
        BooleanExpression filterCondition = createFilterCondition(search);
//    public List<AdminUserDTO> findAdminAll(int page, int size) {
//        select
//        aaa.id,
//                aaa.user_id,
//                aaa.email,
//                aaa.account_type,
//                aaa.phone_number,
//                aaa.recent_activity_date,
//                bbb.activity_detail
//        from ( select
//                a.id,
//                u.user_id,
//                u.email,    @Override
//    public List<AdminUserDTO> findAdminAll(int page, int size) {
////        select
////        aaa.id,
////                aaa.user_id,
////                aaa.email,
////                aaa.account_type,
////                aaa.phone_number,
////                aaa.recent_activity_date,
////                bbb.activity_detail
////        from ( select
////                a.id,
////                u.user_id,
////                u.email,
////                a.account_type,
////                u.phone_number,
////                a.recent_activity_date,
////                u.is_admin
////                from
////                user_info u inner join admin_info a
////                on u.user_id = a.user_id ) aaa
////        inner join (select aal.user_id, aal.activity_detail
////        from (select user_id, activity_detail, row_number() over (PARTITION BY user_id order by crt_date desc) as rn from
////                admin_activity_log ) aal
////        where rn = 1
////                ) bbb
////        on bbb.user_id = aaa.user_id
////        where aaa.is_admin = true
////        ;
//        return queryFactory.select(Projections.fields(
//                                AdminUserDTO.class,
//                                adminInfo.id,
//                                adminInfo.userId,
//                                userEntity.email,
//                                adminInfo.accountType,
//                                userEntity.phoneNumber,
//                                adminInfo.recentActivityDate,
//                                adminActivityLog.activityDetail
//                        )
//                ).from(adminInfo)
//                .innerJoin(userEntity).on(userEntity.id.eq(adminInfo.userId))
//                .innerJoin(adminActivityLog).on(adminActivityLog.userId.eq(userEntity.id))
//                .where(userEntity.isAdmin.isTrue())
//                .where(adminActivityLog.crtDate.eq(
//                        queryFactory.select(adminActivityLog.crtDate.max())
//                                .from(adminActivityLog)
//                                .where(adminActivityLog.userId.eq(userEntity.id))
//                ))
//                .offset(page * size).limit(size)
//                .fetch();
//
//                a.account_type,
//                u.phone_number,
//                a.recent_activity_date,
//                u.is_admin
//                from
//                user_info u inner join admin_info a
//                on u.user_id = a.user_id ) aaa
//        inner join (select aal.user_id, aal.activity_detail
//        from (select user_id, activity_detail, row_number() over (PARTITION BY user_id order by crt_date desc) as rn from
//                admin_activity_log ) aal
//        where rn = 1
//                ) bbb
//        on bbb.user_id = aaa.user_id
//        where aaa.is_admin = true
//        ;
        List<AdminUserDTO> results =  queryFactory.select(Projections.fields(
                                AdminUserDTO.class,
                                adminInfo.id,
                                adminInfo.userId,
                                userEntity.email,
                                adminInfo.accountType,
                                userEntity.phoneNumber,
                                adminInfo.recentActivityDate,
                                adminActivityLog.activityDetail
                        )
                ).from(adminInfo)
                .innerJoin(userEntity).on(userEntity.id.eq(adminInfo.userId))
                .innerJoin(adminActivityLog).on(adminActivityLog.userId.eq(userEntity.id))
                .where(userEntity.isAdmin.isTrue())
                .where(adminActivityLog.crtDate.eq(
                        queryFactory.select(adminActivityLog.crtDate.max())
                                .from(adminActivityLog)
                                .where(adminActivityLog.userId.eq(userEntity.id))
                ))
                .where(filterCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


    /**
     * 로그인 내역 조회
     *
     * @param userId : 해당 유저 번호
     * @return : 로그인 히스토리 전체 조회
     */
    @Override
    public Slice<AdminLoginHistoryDTO> findAdminLoginHistoryAll(Long userId, Pageable pageable) {
        List<AdminLoginHistoryDTO> results = queryFactory.select(
                        new QAdminLoginHistoryDTO(
                                adminLoginLog.id,
                                adminLoginLog.crtDate,
                                adminLoginLog.status
                        )
                )
                .from(adminLoginLog)
                .where(adminLoginLog.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


    /**
     * 관리자 타입 수정을 위한 로직
     *
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
                        adminInfo.recentActivityDate
                )
        ).from(adminInfo).where(adminInfo.userId.eq(userId)).fetchOne();

        return AdminInfo
                .builder()
                .id(adminInfoDTO.getId())
                .userId(adminInfoDTO.getUserId())
                .accountType(adminInfoDTO.getAccountType())
                .recentActivityDate(adminInfoDTO.getRecentActivityDate())
                .build();
    }


    /**
     * 관리자 활동 정보를 전체 리스트 출력
     *
     * @param userId : 해당 관리자 번호 검색 조건 출력
     * @return : 활동정보 전체 출력
     */
    @Override
    public Slice<AdminLoginActivityDTO> findAdminActivityLogAll(Long userId, Pageable pageable) {
        List<AdminLoginActivityDTO> results = queryFactory.select(
                        new QAdminLoginActivityDTO(
                                adminActivityLog.crtDate,
                                adminActivityLog.activityDetail
                        )
                )
                .from(adminActivityLog)
                .where(adminActivityLog.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public AdminFormDTO findAllWhereId(Long userId) {
        return queryFactory.select(
                new QAdminFormDTO(
                        userEntity.id,
                        userEntity.name,
                        userEntity.phoneNumber,
                        userEntity.email,
                        userEntity.profileImageId
                )
        ).from(userEntity).where(userEntity.id.eq(userId)).fetchOne();
    }

    /**
     * (8) 관리자 등록 서비스 단
     * - 현재는 isAdmin = True, userJumin1 = 주소
     * - 사실 Form 부분은 출력만 해주고 나머지는 등록해야함
     *
     * @param adminFormDTO
     * @return
     */
    @Override
    public Long changeUserEntityAdminForm(AdminFormDTO adminFormDTO) {
        return queryFactory.update(userEntity)
                .set(userEntity.isAdmin, true)
                .set(userEntity.userJumin1, adminFormDTO.getUserJumin1())
                .where(userEntity.id.eq(adminFormDTO.getUserId())).execute();
    }

    /**
     * (9) 관리자 등록 서비스 단
     * - 현재는 주소 등록, 관리자 타입 설정
     * - 필요 부분만 적용
     *
     * @param adminFormDTO
     * @return
     */
    @Override
    public Long changeAdminInfoAdminForm(AdminFormDTO adminFormDTO) {
        return queryFactory.update(adminInfo)
                .set(adminInfo.accountType, adminFormDTO.getAccountType())
                .set(adminInfo.addr, adminFormDTO.getAddr())
                .where(adminInfo.userId.eq(adminFormDTO.getUserId())).execute();
    }

    @Override
    public List<AdminGroupCountDTO> displayGroupCounter() {
        return queryFactory
                .select(
                        new QAdminGroupCountDTO(
                                adminInfo.accountType,
                                adminInfo.count()
                        )
                )
                .from(adminInfo)
                .groupBy(adminInfo.accountType)
                .fetch();
    }


    /**
     * 공통 함수 : 검색조건 설정하는 함수, 이름, 전화번호 등 하나라도 포함되어 있으면!!
     * @param search 단어 검색
     * @return true or false
     */
    private BooleanExpression createFilterCondition(String search) {
        if (search == null || search.isEmpty()) {
            return null;
        }
        return userEntity.email.containsIgnoreCase(search)
                .or(userEntity.phoneNumber.containsIgnoreCase(search))
                .or(userEntity.name.containsIgnoreCase(search));
    }

}
