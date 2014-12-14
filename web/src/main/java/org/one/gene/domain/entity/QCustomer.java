package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QCustomer is a Querydsl query type for Customer
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {

    private static final long serialVersionUID = -985939803;

    public static final QCustomer customer = new QCustomer("customer");

    public final StringPath address = createString("address");

    public final StringPath code = createString("code");

    public final StringPath comId = createString("comId");

    public final StringPath comName = createString("comName");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath invoiceTitle = createString("invoiceTitle");

    public final StringPath leaderName = createString("leaderName");

    public final StringPath name = createString("name");

    public final StringPath payWays = createString("payWays");

    public final StringPath phoneNo = createString("phoneNo");

    public QCustomer(String variable) {
        super(Customer.class, forVariable(variable));
    }

    public QCustomer(Path<? extends Customer> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCustomer(PathMetadata<?> metadata) {
        super(Customer.class, metadata);
    }

}

