package com.qng.user.controller;

import com.qng.user.entity.UserAccount;
import com.qng.user.repo.UserRepository;
import com.qng.user.security.UserInfoExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserInfoExtractor userInfoExtractor;

    @GetMapping("/me")
    public UserAccount getMe() {
        String username = userInfoExtractor.getCurrentUsername();
        System.out.println("🔍 Gọi API /me với user: " + username);

        return userRepository.findByUsername(username);
    }
}
