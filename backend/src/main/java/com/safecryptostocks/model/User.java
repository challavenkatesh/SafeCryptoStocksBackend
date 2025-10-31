package com.safecryptostocks.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String fullname;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String phonenumber;

    @Column(nullable = true)
    private String otp;

    @Column(nullable = true)
    private LocalDateTime otpTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResetState resetState = ResetState.RESET_REQUESTED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ===== Add this new field =====
    @Column(nullable = true)
    private String profileImage;

    // Enum for reset state
    public enum ResetState {
        RESET_REQUESTED, EMAIL_VERIFIED, OTP_VERIFIED, PASSWORD_CHANGED
    }

    // Constructors
    public User() {}

    public User(String fullname, String password, String email, String phonenumber, String address) {
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address = address;
        this.resetState = ResetState.RESET_REQUESTED;
    }

    // Auto timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Getters and Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhonenumber() { return phonenumber; }
    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public LocalDateTime getOtpTimestamp() { return otpTimestamp; }
    public void setOtpTimestamp(LocalDateTime otpTimestamp) { this.otpTimestamp = otpTimestamp; }

    public ResetState getResetState() { return resetState; }
    public void setResetState(ResetState resetState) { this.resetState = resetState; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ===== Profile Image getter/setter =====
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
