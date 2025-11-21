package com.safecryptostocks.controller;

import com.safecryptostocks.model.Payment;
import com.safecryptostocks.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3000") // ✅ Allows React access
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // --- Step 1: Create Razorpay Order ---
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request) {
        try {
            // 1️⃣ Map request data to a new Payment entity
            Payment newPayment = new Payment();
            newPayment.setAmount(((Number) request.get("amount")).doubleValue());
            newPayment.setName((String) request.get("name"));
            newPayment.setEmail((String) request.get("email"));
            newPayment.setPhno((String) request.get("phno"));

            // 2️⃣ Create the order via service
            Payment createdOrder = paymentService.createOrder(newPayment);

            // 3️⃣ Build response for Razorpay popup
            Map<String, Object> response = new HashMap<>();
            response.put("razorpayOrderId", createdOrder.getRazorpayOrderId());
            response.put("amount", (long) Math.round(createdOrder.getAmount() * 100)); // in paise

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating Razorpay order: " + e.getMessage());
        }
    }

    // --- Step 2: Handle Payment Success ---
    @PostMapping("/payment-success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestBody Map<String, String> responseData) {
        String razorpayOrderId = responseData.get("razorpay_order_id");
        String razorpayPaymentId = responseData.get("razorpay_payment_id");
        String razorpaySignature = responseData.get("razorpay_signature");

        if (razorpayOrderId == null || razorpayPaymentId == null || razorpaySignature == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing payment verification data ❌");
        }

        try {
            paymentService.updatePaymentStatus(razorpayOrderId, razorpayPaymentId, razorpaySignature);
            return ResponseEntity.ok("Payment Successful and Saved ✅");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed ❌");
        }
    }

    // --- Step 3: Get Payment by Order ID ---
    @GetMapping("/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable String orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
