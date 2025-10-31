package com.safecryptostocks.controller;

import com.safecryptostocks.model.Trade;
import com.safecryptostocks.model.Transaction;
import com.safecryptostocks.model.TransactionType;
import com.safecryptostocks.model.User;
import com.safecryptostocks.service.PortfolioService;
import com.safecryptostocks.repository.TradeRepository;
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
@RequestMapping("/api/trades")
@CrossOrigin(origins = "http://localhost:3000")
public class TradeController {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PortfolioService portfolioService;

    // ==================== Get all trades for a user ====================
    @GetMapping
    public ResponseEntity<?> getTrades(@RequestParam Long userId) {
        try {
            List<Trade> trades = tradeRepository.findByUserIdOrderByTimestampDesc(userId);
            return ResponseEntity.ok(trades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching trades: " + e.getMessage());
        }
    }

    // ==================== Get all trades (for all users) ====================
    @GetMapping("/all")
    public ResponseEntity<?> getAllTrades() {
        try {
            List<Trade> trades = tradeRepository.findAll();
            return ResponseEntity.ok(trades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching all trades: " + e.getMessage());
        }
    }

    // ==================== Add a trade (Buy or Sell) ====================
    @PostMapping
    public ResponseEntity<?> addTrade(@RequestBody Trade trade) {
        try {
            if (trade.getUserId() == null || trade.getAmount() == null || trade.getPrice() == null || trade.getType() == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }

            Optional<User> userOpt = userRepository.findById(trade.getUserId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }
            User user = userOpt.get();

            BigDecimal totalTradeValue = trade.getPrice().multiply(trade.getAmount());
            trade.setTotal(totalTradeValue);
            trade.setTimestamp(LocalDateTime.now());

            List<Transaction> transactions = transactionRepository.findByUserIdOrderByTimestampDesc(trade.getUserId());
            BigDecimal currentBalance = transactions.isEmpty() ? BigDecimal.ZERO : transactions.get(0).getTotalAmount();
            BigDecimal updatedBalance;

            if (trade.getType().equalsIgnoreCase("BUY")) {
                if (totalTradeValue.compareTo(currentBalance) > 0) {
                    return ResponseEntity.badRequest().body("Insufficient wallet balance for this trade");
                }

                updatedBalance = currentBalance.subtract(totalTradeValue);

                Transaction transaction = new Transaction();
                transaction.setUser(user);
                transaction.setType(TransactionType.WITHDRAW);
                transaction.setAmount(totalTradeValue.negate());
                transaction.setTotalAmount(updatedBalance);
                transaction.setPaymentMethod("Trade BUY");
                transaction.setTimestamp(LocalDateTime.now());
                transactionRepository.save(transaction);
            } else if (trade.getType().equalsIgnoreCase("SELL")) {
                updatedBalance = currentBalance.add(totalTradeValue);

                Transaction transaction = new Transaction();
                transaction.setUser(user);
                transaction.setType(TransactionType.DEPOSIT);
                transaction.setAmount(totalTradeValue);
                transaction.setTotalAmount(updatedBalance);
                transaction.setPaymentMethod("Trade SELL");
                transaction.setTimestamp(LocalDateTime.now());
                transactionRepository.save(transaction);
            } else {
                return ResponseEntity.badRequest().body("Invalid trade type. Use 'BUY' or 'SELL'.");
            }

            Trade savedTrade = tradeRepository.save(trade);
            portfolioService.recalculatePortfolio(trade.getUserId());

            return ResponseEntity.ok(savedTrade);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Trade failed: " + e.getMessage());
        }
    }

    // ==================== Get trade by ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<?> getTradeById(@PathVariable Long id) {
        Optional<Trade> tradeOpt = tradeRepository.findById(id);
        return tradeOpt
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Trade not found with ID: " + id));
    }
}
