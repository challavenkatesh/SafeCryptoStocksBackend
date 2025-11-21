package com.safecryptostocks.controller;

import com.safecryptostocks.model.User;
import com.safecryptostocks.repository.UserRepository;
import com.safecryptostocks.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class GoogleAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final String CLIENT_ID = "616447967153-0ca1dbs29uoho297206k2avqjbqbcq81.apps.googleusercontent.com";

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        try {
            String name = payload.get("name");
            String email = payload.get("email");

            // Validate input
            if (name == null || email == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid Google user data"));
            }

            // Check if user exists
            Optional<User> existingUser = userRepository.findByEmail(email);
            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
            } else {
                // Create new user with dummy/default values for mandatory fields
                user = new User();
                user.setFullname(name);
                user.setEmail(email);
                user.setPassword("GOOGLE_AUTH"); // dummy password
                user.setAddress("N/A");          // for not-null field
                user.setPhonenumber("N/A");      // for not-null field
                userRepository.save(user);
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(email);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "fullName", user.getFullname(),
                    "email", user.getEmail(),
                    "profileImage", payload.getOrDefault("profileImage", "")
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Google login failed"));
        }
    }
}
