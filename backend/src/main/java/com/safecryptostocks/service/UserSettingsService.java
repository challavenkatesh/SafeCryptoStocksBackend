package com.safecryptostocks.service;

import com.safecryptostocks.model.CurrencyType;
import com.safecryptostocks.model.UserSettings;
import com.safecryptostocks.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSettingsService {

    @Autowired
    private UserSettingsRepository repository;

    // --- 💰 Currency Operations ---
    public UserSettings saveOrUpdateCurrency(Long userId, CurrencyType currency) {
        UserSettings settings = repository.findByUserId(userId)
                .orElse(new UserSettings(userId, currency));

        settings.setCurrency(currency);
        settings.setUpdatedAt(LocalDateTime.now());
        return repository.save(settings);
    }

    public CurrencyType getCurrencyByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(UserSettings::getCurrency)
                .orElse(CurrencyType.INR);
    }

    // --- 🔔 Notification Operations ---
    public boolean getNotificationStatus(Long userId) {
        return repository.findByUserId(userId)
                .map(UserSettings::isNotificationsEnabled)
                .orElse(false);
    }

    public UserSettings updateNotificationStatus(Long userId, boolean enabled) {
        UserSettings settings = repository.findByUserId(userId)
                .orElseGet(() -> {
                    UserSettings newSettings = new UserSettings();
                    newSettings.setUserId(userId);
                    return newSettings;
                });

        settings.setNotificationsEnabled(enabled);
        settings.setUpdatedAt(LocalDateTime.now());
        return repository.save(settings);
    }
}
