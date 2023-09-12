package com.prgrms.nabmart.domain.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserCoupon is a Querydsl query type for UserCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCoupon extends EntityPathBase<UserCoupon> {

    private static final long serialVersionUID = 1307415494L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserCoupon userCoupon = new QUserCoupon("userCoupon");

    public final com.prgrms.nabmart.global.QBaseTimeEntity _super = new com.prgrms.nabmart.global.QBaseTimeEntity(this);

    public final QCoupon coupon;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath isUsed = createBoolean("isUsed");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.prgrms.nabmart.domain.user.QUser user;

    public final NumberPath<Long> userCouponId = createNumber("userCouponId", Long.class);

    public QUserCoupon(String variable) {
        this(UserCoupon.class, forVariable(variable), INITS);
    }

    public QUserCoupon(Path<? extends UserCoupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserCoupon(PathMetadata metadata, PathInits inits) {
        this(UserCoupon.class, metadata, inits);
    }

    public QUserCoupon(Class<? extends UserCoupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon")) : null;
        this.user = inits.isInitialized("user") ? new com.prgrms.nabmart.domain.user.QUser(forProperty("user")) : null;
    }

}

