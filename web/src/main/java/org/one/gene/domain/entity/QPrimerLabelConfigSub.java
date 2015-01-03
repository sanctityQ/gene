package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPrimerLabelConfigSub is a Querydsl query type for PrimerLabelConfigSub
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPrimerLabelConfigSub extends EntityPathBase<PrimerLabelConfigSub> {

    private static final long serialVersionUID = 1323344388;

    public static final QPrimerLabelConfigSub primerLabelConfigSub = new QPrimerLabelConfigSub("primerLabelConfigSub");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> primerLabelConfigId = createNumber("primerLabelConfigId", Integer.class);

    public final NumberPath<Byte> sorting = createNumber("sorting", Byte.class);

    public final StringPath type = createString("type");

    public final StringPath typeDesc = createString("typeDesc");

    public QPrimerLabelConfigSub(String variable) {
        super(PrimerLabelConfigSub.class, forVariable(variable));
    }

    public QPrimerLabelConfigSub(Path<? extends PrimerLabelConfigSub> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPrimerLabelConfigSub(PathMetadata<?> metadata) {
        super(PrimerLabelConfigSub.class, metadata);
    }

}

