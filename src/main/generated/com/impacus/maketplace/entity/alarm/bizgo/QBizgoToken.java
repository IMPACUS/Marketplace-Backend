package com.impacus.maketplace.entity.alarm.bizgo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBizgoToken is a Querydsl query type for BizgoToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBizgoToken extends EntityPathBase<BizgoToken> {

    private static final long serialVersionUID = -817348517L;

    public static final QBizgoToken bizgoToken = new QBizgoToken("bizgoToken");

    public final DateTimePath<java.time.LocalDateTime> createAt = createDateTime("createAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> expiredDate = createDateTime("expiredDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath registerId = createString("registerId");

    public final StringPath token = createString("token");

    public QBizgoToken(String variable) {
        super(BizgoToken.class, forVariable(variable));
    }

    public QBizgoToken(Path<? extends BizgoToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBizgoToken(PathMetadata metadata) {
        super(BizgoToken.class, metadata);
    }

}

