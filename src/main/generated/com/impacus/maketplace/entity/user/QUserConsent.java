package com.impacus.maketplace.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserConsent is a Querydsl query type for UserConsent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserConsent extends EntityPathBase<UserConsent> {

    private static final long serialVersionUID = 1171150841L;

    public static final QUserConsent userConsent = new QUserConsent("userConsent");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final BooleanPath doesAgreePersonalPolicy = createBoolean("doesAgreePersonalPolicy");

    public final BooleanPath doesAgreeServicePolicy = createBoolean("doesAgreeServicePolicy");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final DateTimePath<java.time.LocalDateTime> personalPolicyConsentAt = createDateTime("personalPolicyConsentAt", java.time.LocalDateTime.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final DateTimePath<java.time.LocalDateTime> servicePolicyConsentAt = createDateTime("servicePolicyConsentAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserConsent(String variable) {
        super(UserConsent.class, forVariable(variable));
    }

    public QUserConsent(Path<? extends UserConsent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserConsent(PathMetadata metadata) {
        super(UserConsent.class, metadata);
    }

}

