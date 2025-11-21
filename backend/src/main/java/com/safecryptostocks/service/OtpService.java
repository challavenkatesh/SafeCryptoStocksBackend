package com.safecryptostocks.service;

import com.safecryptostocks.model.Otp;
import com.safecryptostocks.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private MailService mailService;

    // =========================================================
    // 🔹 GENERATE OTP AND SEND MAIL
    // =========================================================
    public Otp generateOtp(Long userId, String email, String paymentType) {
        // Delete any existing OTP for this email (avoid duplicates)
        otpRepository.findByEmail(email).ifPresent(otpRepository::delete);

        // Generate a 6-digit random OTP
        int otpCode = 100000 + new Random().nextInt(900000);

        // Create and save new OTP record
        Otp otp = new Otp();
        otp.setUserId(userId);
        otp.setEmail(email);
        otp.setOtp(String.valueOf(otpCode));
        otp.setPaymentType(paymentType);
        otp.setCreatedAt(LocalDateTime.now());

        Otp savedOtp = otpRepository.save(otp);
        System.out.println("✅ OTP generated for " + email + ": " + otpCode);

        // Send OTP via Email
        try {
            mailService.sendOtp(email, String.valueOf(otpCode));
            System.out.println("📧 OTP email sent successfully to " + email);
        } catch (Exception e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
        }

        return savedOtp;
    }

    // =========================================================
    // 🔹 VERIFY OTP (Valid for 5 Minutes)
    // =========================================================
    public boolean verifyOtp(String email, String enteredOtp) {
        Optional<Otp> otpRecord = otpRepository.findByEmailAndOtp(email, enteredOtp);

        if (otpRecord.isEmpty()) {
            System.out.println("❌ No OTP found for email: " + email + " or OTP mismatch.");
            return false;
        }

        Otp otp = otpRecord.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = otp.getCreatedAt().plusMinutes(5);

        // Check if OTP expired
        if (now.isAfter(expiryTime)) {
            System.out.println("⚠️ OTP expired for email: " + email);
            otpRepository.delete(otp);
            return false;
        }

        // OTP valid → delete after successful verification
        System.out.println("✅ OTP verified successfully for " + email);
        otpRepository.delete(otp);
        return true;
    }
}
