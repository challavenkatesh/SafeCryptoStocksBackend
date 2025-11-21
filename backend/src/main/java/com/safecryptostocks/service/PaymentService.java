package com.safecryptostocks.service;

import com.safecryptostocks.model.Payment;
import com.safecryptostocks.repository.PaymentRepository;
import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    @Autowired
    private PaymentRepository paymentRepository;

    // ✅ Create Razorpay order and store in DB
    public Payment createOrder(Payment payment) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpaySecret);

            Double baseAmount = payment.getAmount();
            if (baseAmount == null || baseAmount <= 0) {
                throw new IllegalArgumentException("Invalid payment amount.");
            }

            Integer amountInPaise = (int) Math.round(baseAmount * 100);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt_" + System.currentTimeMillis());

            Order order = razorpay.orders.create(orderRequest);

            payment.setRazorpayOrderId(order.get("id"));
            payment.setStatus(order.get("status"));
            payment.setCurrency(order.get("currency"));

            return paymentRepository.save(payment);

        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
        }
    }

    // ✅ Update payment after success callback
    public Payment updatePaymentStatus(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        Payment existingPayment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);

        if (existingPayment != null) {
            existingPayment.setRazorpayPaymentId(razorpayPaymentId);
            existingPayment.setRazorpaySignature(razorpaySignature);
            existingPayment.setStatus("SUCCESS");
            return paymentRepository.save(existingPayment);
        } else {
            throw new RuntimeException("Payment not found for orderId: " + razorpayOrderId);
        }
    }

    // ✅ Get payment by order ID (optional)
    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId);
    }
}
