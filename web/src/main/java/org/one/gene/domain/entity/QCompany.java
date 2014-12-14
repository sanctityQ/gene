package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -1871821098;

    public static final QCompany company = new QCompany("company");

    public final StringPath address = createString("address");

    public final StringPath comCode = createString("comCode");

    public final NumberPath<Byte> comLevel = createNumber("comLevel", Byte.class);

    public final StringPath comName = createString("comName");

    public final StringPath comType = createString("comType");

    public final StringPath desc = createString("desc");

    public final StringPath faxNumber = createString("faxNumber");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath postCode = createString("postCode");

    public final StringPath upperComCode = createString("upperComCode");

    public final BooleanPath validate = createBoolean("validate");

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCompany(PathMetadata<?> metadata) {
        super(Company.class, metadata);
    }

}

