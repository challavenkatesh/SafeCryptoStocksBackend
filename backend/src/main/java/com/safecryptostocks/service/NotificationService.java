package com.safecryptostocks.service;

import com.safecryptostocks.model.Notification;
import com.safecryptostocks.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // 🔹 Get count of unread notifications
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // 🔹 Get all notifications of a user
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // ✅ Mark all as read
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    // 🧹 Clear all notifications for a user
    public void clearAllNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        if (!notifications.isEmpty()) {
            notificationRepository.deleteAll(notifications);
        }
    }

    // 🧹 Optional: Clear only notifications older than 7 days
    public void clearOldNotifications(Long userId) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Notification> oldNotifications =
                notificationRepository.findByUserIdAndCreatedAtBefore(userId, sevenDaysAgo);
        if (!oldNotifications.isEmpty()) {
            notificationRepository.deleteAll(oldNotifications);
        }
    }
}
