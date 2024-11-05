package com.impacus.maketplace.entity.system;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSystemFooter is a Querydsl query type for SystemFooter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSystemFooter extends EntityPathBase<SystemFooter> {

    private static final long serialVersionUID = -414720228L;

    public static final QSystemFooter systemFooter = new QSystemFooter("systemFooter");

    public final StringPath address = createString("address");

    public final StringPath businessNum = createString("businessNum");

    public final StringPath companyName = createString("companyName");

    public final StringPath copyright = createString("copyright");

    public final StringPath email = createString("email");

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> image = createNumber("image", Long.class);

    public final StringPath informationNotice = createString("informationNotice");

    public final StringPath informationPolicy = createString("informationPolicy");

    public final StringPath owner = createString("owner");

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final StringPath supportNum = createString("supportNum");

    public final StringPath termService = createString("termService");

    public final StringPath week = createString("week");

    public QSystemFooter(String variable) {
        super(SystemFooter.class, forVariable(variable));
    }

    public QSystemFooter(Path<? extends SystemFooter> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSystemFooter(PathMetadata metadata) {
        super(SystemFooter.class, metadata);
    }

}

