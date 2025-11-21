package com.safecryptostocks.repository;

import com.safecryptostocks.model.Help;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {

    // Find all tickets for a specific user
    List<Help> findByUserId(Long userId);

}
