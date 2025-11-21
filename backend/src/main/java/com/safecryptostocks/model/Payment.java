package com.safecryptostocks.model;

import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email; // Removed unique=true to allow multiple payments from the same user

    @Column(nullable = false)
    private String phno;

    // ⚠️ CRITICAL CORRECTION: Changed from Integer to Double to handle decimal amounts
    @Column(nullable = false)
    private Double amount; // Amount in rupees (base currency, e.g., INR)

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    @Column(nullable = false)
    private String status; // CREATED, SUCCESS, FAILED

    private String currency;

    // ✅ Default Constructor
    public Payment() {}

    // ✅ Parameterized Constructor
    public Payment(String name, String email, String phno, Double amount, String status) { // Type changed
        this.name = name;
        this.email = email;
        this.phno = phno;
        this.amount = amount;
        this.status = status;
    }

    // ✅ Getters and Setters (Updated for Double)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhno() {
        return phno;
    }
    public void setPhno(String phno) {
        this.phno = phno;
    }
    public Double getAmount() { // Return type changed
        return amount;
    }
    public void setAmount(Double amount) { // Parameter type changed
        this.amount = amount;
    }
    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }
    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }
    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }
    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }
    public String getRazorpaySignature() {
        return razorpaySignature;
    }
    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phno='" + phno + '\'' +
                ", amount=" + amount + // Value is now Double
                ", razorpayOrderId='" + razorpayOrderId + '\'' +
                ", razorpayPaymentId='" + razorpayPaymentId + '\'' +
                ", razorpaySignature='" + razorpaySignature + '\'' +
                ", status='" + status + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}