package com.safecryptostocks.repository;

import com.safecryptostocks.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByUserIdAndSymbol(Long userId, String symbol);

    List<Portfolio> findAllByUserId(Long userId);

    List<Portfolio> findByUserId(Long userId);

    // ✅ New method to fetch latest portfolio by userId (most recently updated)
    Portfolio findTopByUserIdOrderByUpdatedAtDesc(Long userId);
    Optional<Portfolio> findTopByUserIdOrderByIdDesc(Long userId);
}
