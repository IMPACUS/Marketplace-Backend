package com.impacus.maketplace.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdminLoginLog is a Querydsl query type for AdminLoginLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminLoginLog extends EntityPathBase<AdminLoginLog> {

    private static final long serialVersionUID = 55922686L;

    public static final QAdminLoginLog adminLoginLog = new QAdminLoginLog("adminLoginLog");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

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

    public final StringPath status = createString("status");

    public QAdminLoginLog(String variable) {
        super(AdminLoginLog.class, forVariable(variable));
    }

    public QAdminLoginLog(Path<? extends AdminLoginLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminLoginLog(PathMetadata metadata) {
        super(AdminLoginLog.class, metadata);
    }

}

