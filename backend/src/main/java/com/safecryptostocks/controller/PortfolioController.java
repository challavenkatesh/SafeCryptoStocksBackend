package com.safecryptostocks.controller;

import com.safecryptostocks.model.Portfolio;
import com.safecryptostocks.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:3000")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    // ✅ Save or Update Portfolio
    @PostMapping("/saveOrUpdate")
    public ResponseEntity<?> saveOrUpdatePortfolio(@RequestBody PortfolioRequest request) {
        try {
            Portfolio portfolio = portfolioService.saveOrUpdatePortfolio(
                    request.getUserId(),
                    request.getSymbolName(),
                    request.getWalletBalance(),
                    request.getPortfolioValue(),
                    request.getTotalRealizedPL()
            );
            return ResponseEntity.ok(portfolio);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to save/update portfolio: " + e.getMessage());
        }
    }

    // ✅ Get All Portfolios by User ID
    @GetMapping("/{userId}")
    public ResponseEntity<?> getPortfoliosByUserId(@PathVariable Long userId) {
        try {
            List<Portfolio> portfolios = portfolioService.getPortfoliosByUserId(userId);
            return ResponseEntity.ok(portfolios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("No portfolios found for userId: " + userId);
        }
    }

    // ✅ Get Portfolio by User ID and Symbol
    @GetMapping("/{userId}/{symbolName}")
    public ResponseEntity<?> getPortfolioBySymbol(
            @PathVariable Long userId,
            @PathVariable String symbolName
    ) {
        try {
            Portfolio portfolio = portfolioService.getPortfolioByUserIdAndSymbol(userId, symbolName);
            return ResponseEntity.ok(portfolio);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404)
                    .body("Portfolio not found for userId: " + userId + " and symbol: " + symbolName);
        }
    }

    // ✅ DTO Class
    public static class PortfolioRequest {
        private Long userId;
        private BigDecimal walletBalance;
        private BigDecimal portfolioValue;
        private BigDecimal totalRealizedPL;
        private String symbolName;

        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public BigDecimal getWalletBalance() { return walletBalance; }
        public void setWalletBalance(BigDecimal walletBalance) { this.walletBalance = walletBalance; }

        public BigDecimal getPortfolioValue() { return portfolioValue; }
        public void setPortfolioValue(BigDecimal portfolioValue) { this.portfolioValue = portfolioValue; }

        public BigDecimal getTotalRealizedPL() { return totalRealizedPL; }
        public void setTotalRealizedPL(BigDecimal totalRealizedPL) { this.totalRealizedPL = totalRealizedPL; }

        public String getSymbolName() { return symbolName; }
        public void setSymbolName(String symbolName) { this.symbolName = symbolName; }
    }
}
