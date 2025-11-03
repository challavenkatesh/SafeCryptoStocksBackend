package com.safecryptostocks.controller;

import com.safecryptostocks.model.CurrencyType;
import com.safecryptostocks.model.UserSettings;
import com.safecryptostocks.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "http://localhost:3000")
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    // ✅ Get currency by user ID
    @GetMapping("/{userId}/currency")
    public ResponseEntity<CurrencyType> getCurrency(@PathVariable Long userId) {
        CurrencyType currency = userSettingsService.getCurrencyByUserId(userId);
        return ResponseEntity.ok(currency);
    }

    // ✅ Update currency for a user
    @PutMapping("/{userId}/currency")
    public ResponseEntity<UserSettings> updateCurrency(
            @PathVariable Long userId,
            @RequestBody UserSettings request
    ) {
        UserSettings updatedSettings = userSettingsService.saveOrUpdateCurrency(userId, request.getCurrency());
        return ResponseEntity.ok(updatedSettings);
    }

    // ✅ Get notification status
    @GetMapping("/{userId}/notifications")
    public ResponseEntity<Boolean> getNotificationStatus(@PathVariable Long userId) {
        boolean status = userSettingsService.getNotificationStatus(userId);
        return ResponseEntity.ok(status);
    }

    // ✅ Update notification status
    @PutMapping("/{userId}/notifications")
    public ResponseEntity<UserSettings> updateNotificationStatus(
            @PathVariable Long userId,
            @RequestBody UserSettings request
    ) {
        UserSettings updatedSettings = userSettingsService.updateNotificationStatus(userId, request.isNotificationsEnabled());
        return ResponseEntity.ok(updatedSettings);
    }
}
