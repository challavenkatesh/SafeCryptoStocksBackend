package com.safecryptostocks.controller;

import com.safecryptostocks.model.Transaction;
import com.safecryptostocks.model.TransactionType;
import com.safecryptostocks.model.User;
import com.safecryptostocks.repository.TransactionRepository;
import com.safecryptostocks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "http://localhost:3000")
public class WalletController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // ==================== Get all transactions for a user ====================
    @GetMapping("/{userId}")
    public ResponseEntity<?> getTransactions(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest().body("User not found");

        List<Transaction> transactions = transactionRepository.findByUserIdOrderByTimestampDesc(userId);
        return ResponseEntity.ok(transactions);
    }

    // ==================== Add a deposit / withdraw / trade deduction ====================
    @PostMapping("/{userId}/transaction")
    public ResponseEntity<?> addTransaction(
            @PathVariable Long userId,
            @RequestBody Transaction request
    ) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty())
                return ResponseEntity.badRequest().body("User not found");

            User user = userOpt.get();

            // Get the last total balance
            List<Transaction> transactions = transactionRepository.findByUserIdOrderByTimestampDesc(userId);
            BigDecimal lastTotal = transactions.isEmpty()
                    ? BigDecimal.ZERO
                    : transactions.get(0).getTotalAmount();

            BigDecimal amount = request.getAmount();
            if (amount == null)
                return ResponseEntity.badRequest().body("Amount is required");

            BigDecimal newTotal;

            // ==================== Deposit ====================
            if (request.getType() == TransactionType.DEPOSIT) {
                newTotal = lastTotal.add(amount);
            }

            // ==================== Withdraw ====================
            else if (request.getType() == TransactionType.WITHDRAW) {
                if (amount.compareTo(lastTotal) > 0)
                    return ResponseEntity.badRequest().body("Insufficient balance");
                newTotal = lastTotal.subtract(amount);
                amount = amount.negate(); // store as negative
            }

            // ==================== Trade (BUY/SELL) ====================
            else {
                return ResponseEntity.badRequest().body("Invalid transaction type");
            }

            // Save transaction
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setType(request.getType());
            transaction.setAmount(amount);
            transaction.setTotalAmount(newTotal);
            transaction.setPaymentMethod(request.getPaymentMethod()); // card / upi / paypal
            transaction.setTimestamp(LocalDateTime.now());

            transactionRepository.save(transaction);

            return ResponseEntity.ok(transaction);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Transaction failed: " + e.getMessage());
        }
    }

    // ==================== Helper endpoint: get wallet balance ====================
    @GetMapping("/{userId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByTimestampDesc(userId);
        BigDecimal balance = transactions.isEmpty()
                ? BigDecimal.ZERO
                : transactions.get(0).getTotalAmount();
        return ResponseEntity.ok(balance);
    }
}
