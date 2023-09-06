package com.prgrms.nabmart.domain.item;

import com.prgrms.nabmart.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "like_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public LikeItem(User user, Item item) {
        this.user = user;
        this.item = item;
    }
}
