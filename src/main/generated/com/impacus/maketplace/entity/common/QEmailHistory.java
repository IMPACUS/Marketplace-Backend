package com.impacus.maketplace.entity.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailHistory is a Querydsl query type for EmailHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailHistory extends EntityPathBase<EmailHistory> {

    private static final long serialVersionUID = 800214414L;

    public static final QEmailHistory emailHistory = new QEmailHistory("emailHistory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath authNo = createString("authNo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.MailType> mailType = createEnum("mailType", com.impacus.maketplace.common.enumType.MailType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath receiveEmail = createString("receiveEmail");

    public final StringPath receiverId = createString("receiverId");

    //inherited
    public final StringPath registerId = _super.registerId;

    public final DateTimePath<java.time.LocalDateTime> sendAt = createDateTime("sendAt", java.time.LocalDateTime.class);

    public QEmailHistory(String variable) {
        super(EmailHistory.class, forVariable(variable));
    }

    public QEmailHistory(Path<? extends EmailHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailHistory(PathMetadata metadata) {
        super(EmailHistory.class, metadata);
    }

}

