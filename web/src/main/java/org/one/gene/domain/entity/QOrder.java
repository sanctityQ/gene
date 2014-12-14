package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.Order;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOrder extends EntityPathBase<com.mysema.query.types.Order> {

    private static final long serialVersionUID = -522628121;

    public static final QOrder order = new QOrder("order1");

    public final StringPath comCode = createString("comCode");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath customerCode = createString("customerCode");

    public final StringPath customerName = createString("customerName");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> modifyTime = createDateTime("modifyTime", java.util.Date.class);

    public final StringPath orderNo = createString("orderNo");

    public final StringPath outOrderNo = createString("outOrderNo");

    public final NumberPath<Byte> status = createNumber("status", Byte.class);

    public final StringPath type = createString("type");

    public final BooleanPath validate = createBoolean("validate");

    public QOrder(String variable) {
        super(com.mysema.query.types.Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends com.mysema.query.types.Order> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QOrder(PathMetadata<?> metadata) {
        super(Order.class, metadata);
    }

}

