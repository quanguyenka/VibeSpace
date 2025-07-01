package com.qng.user.repo;

import com.qng.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserAccount, UUID> {
    UserAccount findByUsername(String username);
}
