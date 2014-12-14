package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QBoardHole is a Querydsl query type for BoardHole
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBoardHole extends EntityPathBase<BoardHole> {

    private static final long serialVersionUID = 577457375;

    public static final QBoardHole boardHole = new QBoardHole("boardHole");

    public final StringPath boardNo = createString("boardNo");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final NumberPath<Integer> createUser = createNumber("createUser", Integer.class);

    public final StringPath holeNo = createString("holeNo");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public QBoardHole(String variable) {
        super(BoardHole.class, forVariable(variable));
    }

    public QBoardHole(Path<? extends BoardHole> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QBoardHole(PathMetadata<?> metadata) {
        super(BoardHole.class, metadata);
    }

}

