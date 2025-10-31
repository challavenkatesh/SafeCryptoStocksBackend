package com.safecryptostocks.dto;

import java.math.BigDecimal;

public class PortfolioDTO {
    private Long userId;
    private BigDecimal walletBalance;
    private BigDecimal portfolioValue;
    private BigDecimal totalRealizedPL;
    private String symbolName; // ✅ Added new field

    // ======== Getters and Setters ========

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
