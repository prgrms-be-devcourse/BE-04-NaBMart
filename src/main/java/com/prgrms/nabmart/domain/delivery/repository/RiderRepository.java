package com.prgrms.nabmart.domain.delivery.repository;

import com.prgrms.nabmart.domain.delivery.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {

    boolean existsByUsername(String username);
}
