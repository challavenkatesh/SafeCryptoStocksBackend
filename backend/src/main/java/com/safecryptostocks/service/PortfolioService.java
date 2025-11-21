package com.safecryptostocks.service;

import com.safecryptostocks.model.Portfolio;
import com.safecryptostocks.repository.PortfolioRepository;
import com.safecryptostocks.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TradeRepository tradeRepository;

    // ✅ Save or Update portfolio (symbol-based)
    public Portfolio saveOrUpdatePortfolio(Long userId, String symbol, BigDecimal walletBalance,
                                           BigDecimal portfolioValue, BigDecimal totalRealizedPL) {

        Optional<Portfolio> existingPortfolio = portfolioRepository.findByUserIdAndSymbol(userId, symbol);

        Portfolio portfolio;
        if (existingPortfolio.isPresent()) {
            portfolio = existingPortfolio.get();
            portfolio.setWalletBalance(walletBalance);
            portfolio.setPortfolioValue(portfolioValue);
            portfolio.setTotalRealizedPL(totalRealizedPL);
        } else {
            portfolio = new Portfolio();
            portfolio.setUserId(userId);
            portfolio.setSymbol(symbol);
            portfolio.setWalletBalance(walletBalance);
            portfolio.setPortfolioValue(portfolioValue);
            portfolio.setTotalRealizedPL(totalRealizedPL);
        }

        return portfolioRepository.save(portfolio);
    }

    // ✅ Get all portfolios by userId
    public List<Portfolio> getPortfoliosByUserId(Long userId) {
        return portfolioRepository.findAllByUserId(userId);
    }

    // ✅ Get single portfolio by userId and symbol
    public Portfolio getPortfolioByUserIdAndSymbol(Long userId, String symbol) {
        return portfolioRepository.findByUserIdAndSymbol(userId, symbol)
                .orElseThrow(() -> new RuntimeException("Portfolio not found for userId: " + userId + " and symbol: " + symbol));
    }

    // ✅ Get the latest updated portfolio by userId
    public Portfolio getLatestPortfolioByUserId(Long userId) {
        return portfolioRepository.findTopByUserIdOrderByIdDesc(userId)
                .orElseThrow(() -> new RuntimeException("No portfolio found for userId: " + userId));
    }

    // ✅ Recalculate total portfolio value and profit/loss
    public void recalculatePortfolio(Long userId) {
        var trades = tradeRepository.findByUserIdOrderByTimestampDesc(userId);

        if (trades.isEmpty()) return;

        BigDecimal totalPortfolioValue = BigDecimal.ZERO;
        BigDecimal totalPL = BigDecimal.ZERO;

        for (var trade : trades) {
            BigDecimal tradeValue = trade.getPrice().multiply(trade.getAmount());

            if (trade.getType().equalsIgnoreCase("BUY")) {
                totalPortfolioValue = totalPortfolioValue.add(tradeValue);
            } else if (trade.getType().equalsIgnoreCase("SELL")) {
                totalPortfolioValue = totalPortfolioValue.subtract(tradeValue);
                totalPL = totalPL.add(tradeValue);
            }
        }

        Optional<Portfolio> portfolioOpt = portfolioRepository.findByUserIdAndSymbol(userId, "ALL");
        Portfolio portfolio = portfolioOpt.orElseGet(Portfolio::new);

        portfolio.setUserId(userId);
        portfolio.setSymbol("ALL");
        portfolio.setPortfolioValue(totalPortfolioValue);
        portfolio.setTotalRealizedPL(totalPL);

        portfolioRepository.save(portfolio);
    }
}
