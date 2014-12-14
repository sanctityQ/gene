package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPrimerProductOperation is a Querydsl query type for PrimerProductOperation
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPrimerProductOperation extends EntityPathBase<PrimerProductOperation> {

    private static final long serialVersionUID = 579991986;

    public static final QPrimerProductOperation primerProductOperation = new QPrimerProductOperation("primerProductOperation");

    public final NumberPath<Byte> backTimes = createNumber("backTimes", Byte.class);

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath failReason = createString("failReason");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> primerProductId = createNumber("primerProductId", Integer.class);

    public final StringPath type = createString("type");

    public final StringPath typeDesc = createString("typeDesc");

    public final StringPath userCode = createString("userCode");

    public final StringPath userName = createString("userName");

    public QPrimerProductOperation(String variable) {
        super(PrimerProductOperation.class, forVariable(variable));
    }

    public QPrimerProductOperation(Path<? extends PrimerProductOperation> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPrimerProductOperation(PathMetadata<?> metadata) {
        super(PrimerProductOperation.class, metadata);
    }

}

