package com.prgrms.nabmart.domain.event.repository;

import com.prgrms.nabmart.domain.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    // CREATE
    Event save(Event event);

    // READ

    // DELETE

}
