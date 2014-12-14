package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPrimerProductValue is a Querydsl query type for PrimerProductValue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPrimerProductValue extends EntityPathBase<PrimerProductValue> {

    private static final long serialVersionUID = -1101032644;

    public static final QPrimerProductValue primerProductValue = new QPrimerProductValue("primerProductValue");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> primerProductId = createNumber("primerProductId", Integer.class);

    public final StringPath type = createString("type");

    public final StringPath typeDesc = createString("typeDesc");

    public final NumberPath<java.math.BigDecimal> value = createNumber("value", java.math.BigDecimal.class);

    public QPrimerProductValue(String variable) {
        super(PrimerProductValue.class, forVariable(variable));
    }

    public QPrimerProductValue(Path<? extends PrimerProductValue> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPrimerProductValue(PathMetadata<?> metadata) {
        super(PrimerProductValue.class, metadata);
    }

}

