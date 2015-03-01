package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QCustomerPrice is a Querydsl query type for CustomerPrice
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCustomerPrice extends EntityPathBase<CustomerPrice> {

    private static final long serialVersionUID = 138977188;

    public static final QCustomerPrice customerPrice = new QCustomerPrice("customerPrice");

    public final NumberPath<java.math.BigDecimal> baseVal = createNumber("baseVal", java.math.BigDecimal.class);

    public final StringPath customerCode = createString("customerCode");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<java.math.BigDecimal> modiPrice = createNumber("modiPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> purifyVal = createNumber("purifyVal", java.math.BigDecimal.class);

    public QCustomerPrice(String variable) {
        super(CustomerPrice.class, forVariable(variable));
    }

    public QCustomerPrice(Path<? extends CustomerPrice> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCustomerPrice(PathMetadata<?> metadata) {
        super(CustomerPrice.class, metadata);
    }

}

