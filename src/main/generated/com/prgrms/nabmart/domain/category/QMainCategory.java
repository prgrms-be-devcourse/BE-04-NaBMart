package com.prgrms.nabmart.domain.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMainCategory is a Querydsl query type for MainCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMainCategory extends EntityPathBase<MainCategory> {

    private static final long serialVersionUID = 855693172L;

    public static final QMainCategory mainCategory = new QMainCategory("mainCategory");

    public final ListPath<com.prgrms.nabmart.domain.item.Item, com.prgrms.nabmart.domain.item.QItem> items = this.<com.prgrms.nabmart.domain.item.Item, com.prgrms.nabmart.domain.item.QItem>createList("items", com.prgrms.nabmart.domain.item.Item.class, com.prgrms.nabmart.domain.item.QItem.class, PathInits.DIRECT2);

    public final NumberPath<Long> mainCategoryId = createNumber("mainCategoryId", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<SubCategory, QSubCategory> subCategories = this.<SubCategory, QSubCategory>createList("subCategories", SubCategory.class, QSubCategory.class, PathInits.DIRECT2);

    public QMainCategory(String variable) {
        super(MainCategory.class, forVariable(variable));
    }

    public QMainCategory(Path<? extends MainCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMainCategory(PathMetadata metadata) {
        super(MainCategory.class, metadata);
    }

}

