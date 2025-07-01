package com.qng.user.service;

import com.qng.user.entity.UserAccount;
import com.qng.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserAccount getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
