package com.safecryptostocks.repository;

import com.safecryptostocks.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    // Get trades by userId ordered by timestamp descending
    List<Trade> findByUserIdOrderByTimestampDesc(Long userId);

}
