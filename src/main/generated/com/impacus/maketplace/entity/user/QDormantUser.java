package com.impacus.maketplace.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDormantUser is a Querydsl query type for DormantUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDormantUser extends EntityPathBase<DormantUser> {

    private static final long serialVersionUID = 1125756342L;

    public static final QDormantUser dormantUser = new QDormantUser("dormantUser");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath authCi = createString("authCi");

    public final StringPath authDi = createString("authDi");

    public final StringPath bankAccountNumber = createString("bankAccountNumber");

    public final EnumPath<com.impacus.maketplace.common.enumType.BankCode> bankCode = createEnum("bankCode", com.impacus.maketplace.common.enumType.BankCode.class);

    public final DateTimePath<java.time.LocalDateTime> certBankDateTime = createDateTime("certBankDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> certEmailDateTime = createDateTime("certEmailDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> certPhoneDateTime = createDateTime("certPhoneDateTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final BooleanPath doesAgreePersonalPolicy = createBoolean("doesAgreePersonalPolicy");

    public final BooleanPath doesAgreeService = createBoolean("doesAgreeService");

    public final BooleanPath doesAgreeServicePolicy = createBoolean("doesAgreeServicePolicy");

    public final DateTimePath<java.time.LocalDateTime> dormancyDateTime = createDateTime("dormancyDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> dormancyUpdateDateTime = createDateTime("dormancyUpdateDateTime", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> greenLabelPoint = createNumber("greenLabelPoint", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAdmin = createBoolean("isAdmin");

    public final BooleanPath isCertBank = createBoolean("isCertBank");

    public final BooleanPath isCertEmail = createBoolean("isCertEmail");

    public final BooleanPath isCertPhone = createBoolean("isCertPhone");

    public final BooleanPath isDormancy = createBoolean("isDormancy");

    public final BooleanPath isWithdrawn = createBoolean("isWithdrawn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath pccc = createString("pccc");

    public final NumberPath<Long> profileImageId = createNumber("profileImageId", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.PaymentMethod> selectedPaymentMethod = createEnum("selectedPaymentMethod", com.impacus.maketplace.common.enumType.PaymentMethod.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.user.UserStatus> status = createEnum("status", com.impacus.maketplace.common.enumType.user.UserStatus.class);

    public final StringPath statusReason = createString("statusReason");

    public final EnumPath<com.impacus.maketplace.common.enumType.user.UserType> type = createEnum("type", com.impacus.maketplace.common.enumType.user.UserType.class);

    public final StringPath userJumin1 = createString("userJumin1");

    public final StringPath userJumin2 = createString("userJumin2");

    public final DateTimePath<java.time.LocalDateTime> withdrawnDateTime = createDateTime("withdrawnDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> wrongPasswordCnt = createNumber("wrongPasswordCnt", Integer.class);

    public QDormantUser(String variable) {
        super(DormantUser.class, forVariable(variable));
    }

    public QDormantUser(Path<? extends DormantUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDormantUser(PathMetadata metadata) {
        super(DormantUser.class, metadata);
    }

}

