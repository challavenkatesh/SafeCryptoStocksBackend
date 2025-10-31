package com.safecryptostocks.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "symbol", nullable = false)
    private String symbol; // ✅ Added symbol name

    @Column(name = "wallet_balance", precision = 20, scale = 2)
    private BigDecimal walletBalance = BigDecimal.ZERO;

    @Column(name = "portfolio_value", precision = 20, scale = 2)
    private BigDecimal portfolioValue = BigDecimal.ZERO;

    @Column(name = "total_realized_pl", precision = 20, scale = 2)
    private BigDecimal totalRealizedPL = BigDecimal.ZERO;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Portfolio() {}

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public BigDecimal getWalletBalance() { return walletBalance; }
    public void setWalletBalance(BigDecimal walletBalance) { this.walletBalance = walletBalance; }

    public BigDecimal getPortfolioValue() { return portfolioValue; }
    public void setPortfolioValue(BigDecimal portfolioValue) { this.portfolioValue = portfolioValue; }

    public BigDecimal getTotalRealizedPL() { return totalRealizedPL; }
    public void setTotalRealizedPL(BigDecimal totalRealizedPL) { this.totalRealizedPL = totalRealizedPL; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
