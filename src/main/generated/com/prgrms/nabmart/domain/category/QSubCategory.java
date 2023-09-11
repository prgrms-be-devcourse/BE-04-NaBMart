package com.prgrms.nabmart.domain.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubCategory is a Querydsl query type for SubCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubCategory extends EntityPathBase<SubCategory> {

    private static final long serialVersionUID = 1029111329L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubCategory subCategory = new QSubCategory("subCategory");

    public final ListPath<com.prgrms.nabmart.domain.item.Item, com.prgrms.nabmart.domain.item.QItem> items = this.<com.prgrms.nabmart.domain.item.Item, com.prgrms.nabmart.domain.item.QItem>createList("items", com.prgrms.nabmart.domain.item.Item.class, com.prgrms.nabmart.domain.item.QItem.class, PathInits.DIRECT2);

    public final QMainCategory mainCategory;

    public final StringPath name = createString("name");

    public final NumberPath<Long> subCategoryId = createNumber("subCategoryId", Long.class);

    public QSubCategory(String variable) {
        this(SubCategory.class, forVariable(variable), INITS);
    }

    public QSubCategory(Path<? extends SubCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubCategory(PathMetadata metadata, PathInits inits) {
        this(SubCategory.class, metadata, inits);
    }

    public QSubCategory(Class<? extends SubCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mainCategory = inits.isInitialized("mainCategory") ? new QMainCategory(forProperty("mainCategory")) : null;
    }

}

