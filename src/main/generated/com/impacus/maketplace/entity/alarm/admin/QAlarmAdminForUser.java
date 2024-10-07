package com.impacus.maketplace.entity.alarm.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmAdminForUser is a Querydsl query type for AlarmAdminForUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmAdminForUser extends EntityPathBase<AlarmAdminForUser> {

    private static final long serialVersionUID = 2089804397L;

    public static final QAlarmAdminForUser alarmAdminForUser = new QAlarmAdminForUser("alarmAdminForUser");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final EnumPath<com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum> category = createEnum("category", com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum.class);

    public final StringPath comment1 = createString("comment1");

    public final StringPath comment2 = createString("comment2");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum> subcategory = createEnum("subcategory", com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum.class);

    public final StringPath template = createString("template");

    public QAlarmAdminForUser(String variable) {
        super(AlarmAdminForUser.class, forVariable(variable));
    }

    public QAlarmAdminForUser(Path<? extends AlarmAdminForUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmAdminForUser(PathMetadata metadata) {
        super(AlarmAdminForUser.class, metadata);
    }

}

