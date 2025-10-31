package com.safecryptostocks.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // BCrypt password encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/login", "/api/register").permitAll()
                        .requestMatchers("/api/forgot-password", "/api/verify-otp", "/api/reset-password").permitAll()
                        .requestMatchers("/api/contacts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/profile-image/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()

                        // Allow wallet, portfolio, trades, settings
                        .requestMatchers("/api/wallet/**").permitAll()
                        .requestMatchers("/api/trades/**").permitAll()
                        .requestMatchers("/api/portfolio/**").permitAll()
                        .requestMatchers("/api/settings/**").permitAll()

                        // ✅ Allow stock chart proxy
                        .requestMatchers("/api/stocks/**").permitAll()

                        // All others require authentication
                        .anyRequest().authenticated()
                );

        // Add JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
