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

    // Save or update currency
    public UserSettings saveOrUpdateCurrency(Long userId, CurrencyType currency) {
        UserSettings settings = repository.findByUserId(userId)
                .orElse(new UserSettings(userId, currency));

        settings.setCurrency(currency);
        settings.setUpdatedAt(LocalDateTime.now());
        return repository.save(settings);
    }

    // Fetch currency by user ID
    public CurrencyType getCurrencyByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(UserSettings::getCurrency)
                .orElse(CurrencyType.INR); // default INR
    }
}
