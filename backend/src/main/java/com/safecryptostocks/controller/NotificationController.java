package com.safecryptostocks.controller;

import com.safecryptostocks.model.Notification;
import com.safecryptostocks.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // ✅ Get count of unread notifications
    @GetMapping("/count/{userId}")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    // ✅ Get all notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    // ✅ Clear all notifications for a user
    @DeleteMapping("/{userId}/clear-all")
    public ResponseEntity<Void> clearAllNotifications(@PathVariable Long userId) {
        notificationService.clearAllNotifications(userId);
        return ResponseEntity.ok().build();
    }
    // ✅ Mark all notifications as read for a specific user
    @PutMapping("/{userId}/mark-all-read")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All notifications marked as read successfully");
    }
}
