package com.impacus.maketplace.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserStatusInfo is a Querydsl query type for UserStatusInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserStatusInfo extends EntityPathBase<UserStatusInfo> {

    private static final long serialVersionUID = -1688877183L;

    public static final QUserStatusInfo userStatusInfo = new QUserStatusInfo("userStatusInfo");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.user.UserStatus> status = createEnum("status", com.impacus.maketplace.common.enumType.user.UserStatus.class);

    public final StringPath statusReason = createString("statusReason");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserStatusInfo(String variable) {
        super(UserStatusInfo.class, forVariable(variable));
    }

    public QUserStatusInfo(Path<? extends UserStatusInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserStatusInfo(PathMetadata metadata) {
        super(UserStatusInfo.class, metadata);
    }

}

