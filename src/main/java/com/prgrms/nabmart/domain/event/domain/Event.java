package com.prgrms.nabmart.domain.event.domain;

import static java.util.Objects.isNull;

import com.prgrms.nabmart.domain.event.exception.InvalidEventDescriptionException;
import com.prgrms.nabmart.domain.event.exception.InvalidEventTitleException;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    public Event(String title, String description) {
        validateTitle(title);
        validateDescription(description);
        this.title = title;
        this.description = description;
    }

    private void validateTitle(String title) {
        if (isNull(title)) {
            throw new InvalidEventTitleException("이벤트 제목이 존재하지 않습니다.");
        }
    }

    private void validateDescription(String description) {
        if (isNull(description)) {
            throw new InvalidEventDescriptionException("이벤트 설명이 존재하지 않습니다.");
        }
    }
}
