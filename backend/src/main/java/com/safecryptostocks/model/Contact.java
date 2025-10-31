package com.safecryptostocks.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Constructors
    public Contact() {}

    public Contact(String fullname, String email, String message) {
        this.fullname = fullname;
        this.email = email;
        this.message = message;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // No setter for createdAt because it's managed by Hibernate

    @Entity
    @Table(name = "transactions")
    public static class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(nullable = false)
        private BigDecimal amount; // + deposit, - withdraw

        @Column(name = "total_amount", nullable = false)
        private BigDecimal totalAmount; // total balance after this transaction

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TransactionType type; // DEPOSIT or WITHDRAW

        @Column(nullable = false)
        private LocalDateTime timestamp = LocalDateTime.now();

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

        public TransactionType getType() { return type; }
        public void setType(TransactionType type) { this.type = type; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }
}
