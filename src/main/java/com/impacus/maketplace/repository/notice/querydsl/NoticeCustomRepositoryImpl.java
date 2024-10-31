package com.impacus.maketplace.repository.notice.querydsl;

import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.common.enumType.notice.NoticeType;
import com.impacus.maketplace.dto.notice.*;
import com.impacus.maketplace.entity.notice.Notice;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.impacus.maketplace.entity.common.QAttachFile.*;
import static com.impacus.maketplace.entity.notice.QNotice.*;

@RequiredArgsConstructor
public class NoticeCustomRepositoryImpl implements NoticeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NoticeManageDataDto> findManageList(NoticeManageSearchDto n, Pageable pageable) {
        return jpaQueryFactory.select(Projections.constructor(
                        NoticeManageDataDto.class,
                        notice.id,
                        notice.type,
                        notice.title,
                        notice.content,
                        notice.startDateTime,
                        notice.endDateTime,
                        notice.status,
                        notice.impression))
                .from(notice)
                .where(searchEq(n.getSearch()), typeEq(n.getType()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private BooleanExpression searchEq(String title) {
        return StringUtils.hasText(title) ? notice.title.contains(title) : null;
    }

    private BooleanExpression typeEq(NoticeType type) {
        return type == null ? null : notice.type.eq(type);
    }

    @Override
    public NoticeManageGetDto findManage(Long noticeId) {
        return jpaQueryFactory.select(Projections.constructor(
                        NoticeManageGetDto.class,
                        notice.id.as("noticeId"),
                        attachFile.attachFileName.as("imagePath"),
                        notice.title,
                        notice.content,
                        notice.startDateTime,
                        notice.endDateTime,
                        notice.status))
                .from(notice)
                .leftJoin(attachFile).on(attachFile.id.eq(notice.attachFileId))
                .where(notice.id.eq(noticeId))
                .fetchOne();

    }

    @Override
    public List<NoticeShowDto> showNotice() {
        LocalDateTime now = LocalDateTime.now();
        return jpaQueryFactory.select(Projections.constructor(
                        NoticeShowDto.class,
                        notice.id,
                        attachFile.attachFileName.as("imagePath"),
                        notice.startDateTime,
                        notice.type))
                .from(notice)
                .leftJoin(attachFile).on(attachFile.id.eq(notice.attachFileId))
                .where(notice.status.eq(NoticeStatus.RUN)
                        .and(notice.startDateTime.loe(now))
                        .and(notice.endDateTime.goe(now)))
                .fetch();
    }

    @Override
    public List<Notice> findForStop() {
        LocalDateTime now = LocalDateTime.now();
        return jpaQueryFactory.selectFrom(notice)
                .where(notice.status.eq(NoticeStatus.RUN)
                        .and(notice.endDateTime.loe(now)))
                .fetch();
    }

    @Override
    public List<NoticeListDto> findList() {
        return jpaQueryFactory.select(Projections.constructor(
                        NoticeListDto.class,
                        notice.id,
                        notice.title,
                        notice.content,
                        notice.startDateTime))
                .from(notice)
                .where(notice.status.eq(NoticeStatus.RUN))
                .orderBy(notice.createAt.desc())
                .fetch();
    }

    @Override
    public NoticeGetDto find(Long noticeId) {
        return jpaQueryFactory.select(Projections.constructor(
                        NoticeGetDto.class,
                        notice.title,
                        notice.content,
                        attachFile.attachFileName.as("imagePath")))
                .from(notice)
                .leftJoin(attachFile).on(attachFile.id.eq(notice.attachFileId))
                .where(notice.status.eq(NoticeStatus.RUN).and(notice.id.eq(noticeId)))
                .fetchOne();
    }
}
