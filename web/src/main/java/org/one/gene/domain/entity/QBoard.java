package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -534725761;

    public static final QBoard board = new QBoard("board");

    public final StringPath boardNo = createString("boardNo");

    public final BooleanPath boardType = createBoolean("boardType");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final NumberPath<Integer> createUser = createNumber("createUser", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath type = createString("type");

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QBoard(PathMetadata<?> metadata) {
        super(Board.class, metadata);
    }

}

