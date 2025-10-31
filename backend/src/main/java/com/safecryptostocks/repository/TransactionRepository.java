package com.safecryptostocks.repository;

import com.safecryptostocks.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ Get all transactions for a specific user (latest first)
    List<Transaction> findByUserIdOrderByTimestampDesc(Long userId);

    // ✅ If you need it without ordering:
    List<Transaction> findByUserId(Long userId);
}
