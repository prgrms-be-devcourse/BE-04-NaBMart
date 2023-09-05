package com.prgrms.nabmart.domain.event.repository;

import com.prgrms.nabmart.domain.event.domain.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByCreatedAtDesc();
}
