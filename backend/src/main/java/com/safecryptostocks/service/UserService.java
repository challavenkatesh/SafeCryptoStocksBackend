package com.safecryptostocks.service;

import com.safecryptostocks.model.User;
import com.safecryptostocks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveOtp(User user, String otp) {
        user.setOtp(otp);
        user.setOtpTimestamp(LocalDateTime.now());
        userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        user.setResetState(User.ResetState.PASSWORD_CHANGED);
        userRepository.save(user);
    }
}
