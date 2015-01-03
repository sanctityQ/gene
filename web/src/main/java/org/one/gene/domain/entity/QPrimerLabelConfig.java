package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPrimerLabelConfig is a Querydsl query type for PrimerLabelConfig
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPrimerLabelConfig extends EntityPathBase<PrimerLabelConfig> {

    private static final long serialVersionUID = -745746788;

    public static final QPrimerLabelConfig primerLabelConfig = new QPrimerLabelConfig("primerLabelConfig");

    public final NumberPath<Byte> columns = createNumber("columns", Byte.class);

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath customerCode = createString("customerCode");

    public final StringPath customerName = createString("customerName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> modifyTime = createDateTime("modifyTime", java.util.Date.class);

    public final StringPath userCode = createString("userCode");

    public final StringPath userName = createString("userName");

    public QPrimerLabelConfig(String variable) {
        super(PrimerLabelConfig.class, forVariable(variable));
    }

    public QPrimerLabelConfig(Path<? extends PrimerLabelConfig> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPrimerLabelConfig(PathMetadata<?> metadata) {
        super(PrimerLabelConfig.class, metadata);
    }

}

