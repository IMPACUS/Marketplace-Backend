package com.impacus.maketplace.repository.notice.querydsl;

import com.impacus.maketplace.common.enumType.notice.NoticeType;
import com.impacus.maketplace.dto.notice.NoticeManageDataDto;
import com.impacus.maketplace.dto.notice.NoticeManageSearchDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.impacus.maketplace.entity.notice.QNoticeManage.*;

@RequiredArgsConstructor
public class NoticeManageCustomRepositoryImpl implements NoticeManageCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NoticeManageDataDto> findList(NoticeManageSearchDto n, Pageable pageable) {
        return jpaQueryFactory.select(Projections.constructor(
                        NoticeManageDataDto.class,
                        noticeManage.id,
                        noticeManage.type,
                        noticeManage.title,
                        noticeManage.content,
                        noticeManage.startDateTime,
                        noticeManage.endDateTime,
                        noticeManage.status,
                        noticeManage.impression))
                .from(noticeManage)
                .where(searchEq(n.getSearch()), typeEq(n.getType()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private BooleanExpression searchEq(String title) {
        return StringUtils.hasText(title) ? noticeManage.title.contains(title) : null;
    }

    private BooleanExpression typeEq(NoticeType type) {
        return type == null ? null : noticeManage.type.eq(type);
    }
}
