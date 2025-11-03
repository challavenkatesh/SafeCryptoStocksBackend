package com.safecryptostocks.repository;

import com.safecryptostocks.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByUserIdAndIsReadFalse(Long userId);
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndCreatedAtBefore(Long userId, LocalDateTime dateTime);

    // ✅ Add this line if not already present
    List<Notification> findByUserId(Long userId);
}
