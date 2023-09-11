package com.prgrms.nabmart.domain.delivery.repository;

import com.prgrms.nabmart.domain.delivery.Rider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {

    boolean existsByUsername(String username);

    Optional<Rider> findByUsername(String username);
}
