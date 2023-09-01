package com.prgrms.nabmart.domain.item.repository;

import com.prgrms.nabmart.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
