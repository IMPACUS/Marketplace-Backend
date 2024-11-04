package com.impacus.maketplace.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdminInfo is a Querydsl query type for AdminInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminInfo extends EntityPathBase<AdminInfo> {

    private static final long serialVersionUID = -1549531375L;

    public static final QAdminInfo adminInfo = new QAdminInfo("adminInfo");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath accountType = createString("accountType");

    public final StringPath addr = createString("addr");

    public final StringPath adminIdName = createString("adminIdName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgSrc = createString("imgSrc");

    public final StringPath juminNo = createString("juminNo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final DateTimePath<java.time.ZonedDateTime> recentActivityDate = createDateTime("recentActivityDate", java.time.ZonedDateTime.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public QAdminInfo(String variable) {
        super(AdminInfo.class, forVariable(variable));
    }

    public QAdminInfo(Path<? extends AdminInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminInfo(PathMetadata metadata) {
        super(AdminInfo.class, metadata);
    }

}

