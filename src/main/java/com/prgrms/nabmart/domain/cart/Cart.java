package com.prgrms.nabmart.domain.cart;

import static java.util.Objects.isNull;

import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotExistUserException;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Cart(
        final User user
    ) {
        validateUser(user);
        this.user = user;
    }

    public void validateUser(
        final User user
    ) {
        if (isNull(user)) {
            throw new NotExistUserException("User 가 존재하지 않습니다.");
        }
    }
}
