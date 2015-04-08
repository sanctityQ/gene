package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QModifiedPrice is a Querydsl query type for ModifiedPrice
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QModifiedPrice extends EntityPathBase<ModifiedPrice> {

    private static final long serialVersionUID = 888325725;

    public static final QModifiedPrice modifiedPrice = new QModifiedPrice("modifiedPrice");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<java.math.BigDecimal> modiPrice = createNumber("modiPrice", java.math.BigDecimal.class);

    public final StringPath modiType = createString("modiType");

    public final StringPath validate = createString("validate");

    public QModifiedPrice(String variable) {
        super(ModifiedPrice.class, forVariable(variable));
    }

    public QModifiedPrice(Path<? extends ModifiedPrice> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QModifiedPrice(PathMetadata<?> metadata) {
        super(ModifiedPrice.class, metadata);
    }

}

