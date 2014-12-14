package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPrimerProduct is a Querydsl query type for PrimerProduct
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPrimerProduct extends EntityPathBase<PrimerProduct> {

    private static final long serialVersionUID = 1987004213;

    public static final QPrimerProduct primerProduct = new QPrimerProduct("primerProduct");

    public final NumberPath<Byte> backTimes = createNumber("backTimes", Byte.class);

    public final StringPath boardNo = createString("boardNo");

    public final StringPath comCode = createString("comCode");

    public final StringPath fromProductNo = createString("fromProductNo");

    public final StringPath geneOrder = createString("geneOrder");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath modiFiveType = createString("modiFiveType");

    public final StringPath modiFiveVal = createString("modiFiveVal");

    public final StringPath modiMidType = createString("modiMidType");

    public final NumberPath<java.math.BigDecimal> modiPrice = createNumber("modiPrice", java.math.BigDecimal.class);

    public final StringPath modiSpeType = createString("modiSpeType");

    public final StringPath modiThreeType = createString("modiThreeType");

    public final StringPath operationType = createString("operationType");

    public final StringPath orderNo = createString("orderNo");

    public final StringPath outProductNo = createString("outProductNo");

    public final StringPath primeName = createString("primeName");

    public final StringPath productNo = createString("productNo");

    public final StringPath purifyType = createString("purifyType");

    public final StringPath remark = createString("remark");

    public final StringPath reviewFileName = createString("reviewFileName");

    public QPrimerProduct(String variable) {
        super(PrimerProduct.class, forVariable(variable));
    }

    public QPrimerProduct(Path<? extends PrimerProduct> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPrimerProduct(PathMetadata<?> metadata) {
        super(PrimerProduct.class, metadata);
    }

}

