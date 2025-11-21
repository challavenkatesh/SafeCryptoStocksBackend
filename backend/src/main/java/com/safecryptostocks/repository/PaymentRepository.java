package com.safecryptostocks.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.safecryptostocks.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // ✅ Custom method to find a payment by Razorpay Order ID
    Payment findByRazorpayOrderId(String razorpayOrderId);

    // ✅ Optional: find by payment ID (useful after success callback)
    Payment findByRazorpayPaymentId(String razorpayPaymentId);
}
