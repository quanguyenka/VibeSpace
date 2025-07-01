package com.qng.auth.repo;

import com.qng.auth.entity.RefreshToken;
import com.qng.auth.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(UserAccount user);

    void deleteByUser(UserAccount user);
}