package com.safecryptostocks.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "holdings")
public class Holdings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "avg_buy_price", nullable = false, precision = 20, scale = 8)
    private BigDecimal avgBuyPrice = BigDecimal.ZERO;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal value = BigDecimal.ZERO;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal pl = BigDecimal.ZERO;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ================== Constructors ==================
    public Holdings() {}

    public Holdings(Long userId, String assetName, String symbol, BigDecimal quantity, BigDecimal avgBuyPrice) {
        this.userId = userId;
        this.assetName = assetName;
        this.symbol = symbol;
        this.quantity = quantity;
        this.avgBuyPrice = avgBuyPrice;
        this.value = BigDecimal.ZERO;
        this.pl = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ================== Getters and Setters ==================
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAvgBuyPrice() {
        return avgBuyPrice;
    }
    public void setAvgBuyPrice(BigDecimal avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getPl() {
        return pl;
    }
    public void setPl(BigDecimal pl) {
        this.pl = pl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
