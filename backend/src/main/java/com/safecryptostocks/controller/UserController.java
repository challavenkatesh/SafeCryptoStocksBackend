package com.safecryptostocks.controller;

import com.safecryptostocks.model.User;
import com.safecryptostocks.repository.UserRepository;
import com.safecryptostocks.security.JwtUtil;
import com.safecryptostocks.service.MailService;
import com.safecryptostocks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil = new JwtUtil();
    private final String UPLOAD_DIR = "uploads/";

    // ==================== Registration ====================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.findByFullname(user.getFullname()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // ==================== Login ====================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByFullname(user.getFullname());

        if (existingUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
                User u = existingUser.get();
                String token = jwtUtil.generateToken(u.getFullname());

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("id", u.getId());
                response.put("fullName", u.getFullname());
                response.put("email", u.getEmail());
                response.put("profileImage", u.getProfileImage() != null
                        ? "http://localhost:8080/api/user/profile-image/" + u.getProfileImage()
                        : "");

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
    }

    // ==================== Profile ====================
    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String fullname = jwtUtil.extractUsername(token);

            Optional<User> userOpt = userRepository.findByFullname(fullname);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = userOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("fullName", user.getFullname() != null ? user.getFullname() : "");
            response.put("email", user.getEmail() != null ? user.getEmail() : "");
            response.put("address", user.getAddress() != null ? user.getAddress() : "");
            response.put("phone", user.getPhonenumber() != null ? user.getPhonenumber() : "");
            response.put("profileImage", user.getProfileImage() != null
                    ? "http://localhost:8080/api/user/profile-image/" + user.getProfileImage()
                    : "");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    // ==================== ✅ Update Profile ====================
    @PutMapping("/user/update-profile")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> updatedData) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String fullname = jwtUtil.extractUsername(token);

            Optional<User> userOpt = userRepository.findByFullname(fullname);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = userOpt.get();

            // Update allowed fields
            if (updatedData.containsKey("fullName")) {
                user.setFullname((String) updatedData.get("fullName"));
            }
            if (updatedData.containsKey("email")) {
                user.setEmail((String) updatedData.get("email"));
            }
            if (updatedData.containsKey("address")) {
                user.setAddress((String) updatedData.get("address"));
            }
            if (updatedData.containsKey("phone")) {
                user.setPhonenumber((String) updatedData.get("phone"));
            }

            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile updated successfully!");
            response.put("user", user);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating profile: " + e.getMessage());
        }
    }

    // ==================== Change Password ====================
    @PutMapping("/user/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request) {

        String token = authHeader.replace("Bearer ", "");
        String fullname = jwtUtil.extractUsername(token);

        Optional<User> userOpt = userRepository.findByFullname(fullname);
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        User user = userOpt.get();
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("Password changed successfully!");
    }

    // ==================== Forgot Password ====================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        User user = userOpt.get();
        String otp = String.valueOf((int) (Math.random() * 9000) + 1000);
        userService.saveOtp(user, otp);
        mailService.sendOtp(user.getEmail(), otp);

        return ResponseEntity.ok("OTP sent to registered email!");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otpInput = request.get("otp");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        User user = userOpt.get();

        if (user.getOtp() == null || !user.getOtp().equals(otpInput)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }

        if (user.getOtpTimestamp().plusMinutes(5).isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP expired");
        }

        user.setResetState(User.ResetState.OTP_VERIFIED);
        userService.saveOtp(user, null);
        return ResponseEntity.ok("OTP verified successfully!");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");

        User user = userOpt.get();
        if (user.getResetState() != User.ResetState.OTP_VERIFIED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP verification required before resetting password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully!");
    }

    // ==================== Upload Profile Image ====================
    @PostMapping("/user/upload-profile")
    public ResponseEntity<?> uploadProfileImage(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {

        try {
            String token = authHeader.replace("Bearer ", "");
            String fullname = jwtUtil.extractUsername(token);

            Optional<User> userOpt = userRepository.findByFullname(fullname);
            if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            User user = userOpt.get();

            String fileName = System.currentTimeMillis() + "_" + Paths.get(file.getOriginalFilename()).getFileName();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());
            file.transferTo(path);

            user.setProfileImage(fileName);
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile image uploaded successfully!");
            response.put("profileImage", "http://localhost:8080/api/user/profile-image/" + fileName);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // ==================== Serve Profile Image ====================
    @GetMapping("/user/profile-image/{filename}")
    public ResponseEntity<?> getProfileImage(@PathVariable String filename) {
        try {
            Path path = Paths.get(UPLOAD_DIR + filename);
            byte[] image = Files.readAllBytes(path);

            String contentType = Files.probeContentType(path);

            return ResponseEntity.ok()
                    .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                    .body(image);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
    }
}
