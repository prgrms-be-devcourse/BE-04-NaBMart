package com.prgrms.nabmart.domain.event.repository;

import com.prgrms.nabmart.domain.event.domain.EventItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventItemRepository extends JpaRepository<EventItem, Long> {

}
