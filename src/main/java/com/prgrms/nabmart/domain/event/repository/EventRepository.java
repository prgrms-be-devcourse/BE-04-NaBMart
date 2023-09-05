package com.prgrms.nabmart.domain.event.repository;

import com.prgrms.nabmart.domain.event.domain.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByCreatedAtDesc();

    @Query("SELECT e FROM Event e "
        + "LEFT JOIN  FETCH e.eventItemList ei "
        + "WHERE e.eventId = :eventId")
    Optional<Event> findByIdWithEventItems(@Param("eventId") Long eventId);
}
