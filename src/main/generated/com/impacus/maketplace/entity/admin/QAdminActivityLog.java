package com.impacus.maketplace.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdminActivityLog is a Querydsl query type for AdminActivityLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminActivityLog extends EntityPathBase<AdminActivityLog> {

    private static final long serialVersionUID = 1813684722L;

    public static final QAdminActivityLog adminActivityLog = new QAdminActivityLog("adminActivityLog");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath activityDetail = createString("activityDetail");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final DateTimePath<java.time.ZonedDateTime> crtDate = createDateTime("crtDate", java.time.ZonedDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public QAdminActivityLog(String variable) {
        super(AdminActivityLog.class, forVariable(variable));
    }

    public QAdminActivityLog(Path<? extends AdminActivityLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminActivityLog(PathMetadata metadata) {
        super(AdminActivityLog.class, metadata);
    }

}

