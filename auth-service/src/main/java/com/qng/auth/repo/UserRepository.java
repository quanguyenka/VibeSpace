package com.qng.auth.repo;

import com.qng.auth.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findByEmail(String email);
}
