package com.prgrms.nabmart.domain.event.domain;

import static java.util.Objects.isNull;

import com.prgrms.nabmart.domain.event.exception.NotFoundEventException;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"event_id", "item_id"}
    )
})
public class EventItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public EventItem(Event event, Item item) {
        validateEvent(event);
        validateItem(item);
        this.event = event;
        this.item = item;
    }

    public void validateEvent(Event event) {
        if (isNull(event)) {
            throw new NotFoundEventException("Event가 존재하지 않습니다.");
        }
    }

    private void validateItem(Item item) {
        if (isNull(item)) {
            throw new NotFoundItemException("Item이 존재하지 않습니다.");
        }
    }
}
