package com.safecryptostocks.repository;

import com.safecryptostocks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFullname(String username);
    Optional<User> findByEmail(String email);

}
