package com.prgrms.nabmart.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikeItem is a Querydsl query type for LikeItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeItem extends EntityPathBase<LikeItem> {

    private static final long serialVersionUID = -629365902L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikeItem likeItem = new QLikeItem("likeItem");

    public final QItem item;

    public final NumberPath<Long> likeItemId = createNumber("likeItemId", Long.class);

    public final com.prgrms.nabmart.domain.user.QUser user;

    public QLikeItem(String variable) {
        this(LikeItem.class, forVariable(variable), INITS);
    }

    public QLikeItem(Path<? extends LikeItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikeItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikeItem(PathMetadata metadata, PathInits inits) {
        this(LikeItem.class, metadata, inits);
    }

    public QLikeItem(Class<? extends LikeItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
        this.user = inits.isInitialized("user") ? new com.prgrms.nabmart.domain.user.QUser(forProperty("user")) : null;
    }

}

