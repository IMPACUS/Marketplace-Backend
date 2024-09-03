package com.impacus.maketplace.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -867471135L;

    public static final QUser user = new QUser("user");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> certEmailAt = createDateTime("certEmailAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> certPhoneAt = createDateTime("certPhoneAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCertEmail = createBoolean("isCertEmail");

    public final BooleanPath isCertPhone = createBoolean("isCertPhone");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath jumin1 = createString("jumin1");

    public final StringPath jumin2 = createString("jumin2");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Long> profileImageId = createNumber("profileImageId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> recentLoginAt = createDateTime("recentLoginAt", java.time.LocalDateTime.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.user.UserType> type = createEnum("type", com.impacus.maketplace.common.enumType.user.UserType.class);

    public final StringPath userIdName = createString("userIdName");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

