package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1817794542;

    public static final QUser user = new QUser("user");

    public final StringPath code = createString("code");

    public final StringPath comCode = createString("comCode");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath mobile = createString("mobile");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final BooleanPath staffFlag = createBoolean("staffFlag");

    public final BooleanPath validate = createBoolean("validate");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QUser(PathMetadata<?> metadata) {
        super(User.class, metadata);
    }

}

