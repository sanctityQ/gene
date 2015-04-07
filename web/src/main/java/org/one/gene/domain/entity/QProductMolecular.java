package org.one.gene.domain.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QProductMolecular is a Querydsl query type for ProductMolecular
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProductMolecular extends EntityPathBase<ProductMolecular> {

    private static final long serialVersionUID = -1434264956;

    public static final QProductMolecular productMolecular = new QProductMolecular("productMolecular");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<java.math.BigDecimal> modifiedMolecular = createNumber("modifiedMolecular", java.math.BigDecimal.class);

    public final StringPath productCategories = createString("productCategories");

    public final StringPath productCode = createString("productCode");

    public final StringPath validate = createString("validate");

    public QProductMolecular(String variable) {
        super(ProductMolecular.class, forVariable(variable));
    }

    public QProductMolecular(Path<? extends ProductMolecular> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QProductMolecular(PathMetadata<?> metadata) {
        super(ProductMolecular.class, metadata);
    }

}

