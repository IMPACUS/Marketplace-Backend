package com.impacus.maketplace.entity.notice;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNoticeManage is a Querydsl query type for NoticeManage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeManage extends EntityPathBase<NoticeManage> {

    private static final long serialVersionUID = 1143635654L;

    public static final QNoticeManage noticeManage = new QNoticeManage("noticeManage");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final NumberPath<Long> attachFileId = createNumber("attachFileId", Long.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final DateTimePath<java.time.LocalDateTime> endDateTime = createDateTime("endDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> impression = createNumber("impression", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final DateTimePath<java.time.LocalDateTime> startDateTime = createDateTime("startDateTime", java.time.LocalDateTime.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.notice.NoticeStatus> status = createEnum("status", com.impacus.maketplace.common.enumType.notice.NoticeStatus.class);

    public final StringPath title = createString("title");

    public final EnumPath<com.impacus.maketplace.common.enumType.notice.NoticeType> type = createEnum("type", com.impacus.maketplace.common.enumType.notice.NoticeType.class);

    public QNoticeManage(String variable) {
        super(NoticeManage.class, forVariable(variable));
    }

    public QNoticeManage(Path<? extends NoticeManage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNoticeManage(PathMetadata metadata) {
        super(NoticeManage.class, metadata);
    }

}

