package com.prgrms.nabmart.domain.notification.repository;

import com.prgrms.nabmart.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
