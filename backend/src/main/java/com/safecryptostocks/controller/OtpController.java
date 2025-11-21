package com.safecryptostocks.controller;

import com.safecryptostocks.model.Otp;
import com.safecryptostocks.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "*")
public class OtpController {

    @Autowired
    private OtpService otpService;

    // =========================================================
    // 🔹 Generate OTP API
    // =========================================================
    @PostMapping("/generate")
    public ResponseEntity<?> generateOtp(@RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(request.get("userId"));
            String email = request.get("email");
            String paymentType = request.get("paymentType");

            Otp otp = otpService.generateOtp(userId, email, paymentType);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "OTP generated and sent successfully!",
                    "otpId", otp.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Failed to generate OTP: " + e.getMessage()
            ));
        }
    }

    // =========================================================
    // 🔹 Verify OTP API
    // =========================================================
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        boolean isValid = otpService.verifyOtp(email, otp);

        if (isValid) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "✅ OTP verified successfully!"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "❌ Invalid or expired OTP!"
            ));
        }
    }
}
