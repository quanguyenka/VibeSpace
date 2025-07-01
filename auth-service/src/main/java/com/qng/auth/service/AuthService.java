package com.qng.auth.service;

import com.qng.auth.dto.LoginRequest;
import com.qng.auth.dto.RegisterRequest;
import com.qng.auth.entity.UserAccount;
import com.qng.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public String register(RegisterRequest req) {
        if (userRepository.findByUsername(req.username()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        UserAccount user = UserAccount.builder()
                .username(req.username())
                .email(req.email())
                .fullName(req.fullName())
                .password(passwordEncoder.encode(req.password()))
                .provider(UserAccount.AuthProvider.LOCAL)
                .role(UserAccount.Role.USER)
                .build();

        userRepository.save(user);
        return "Đăng ký thành công!";
    }

    public Map<String, String> login(LoginRequest req) {
        UserAccount user = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("Thông tin đăng nhập không hợp lệ"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new RuntimeException("Thông tin đăng nhập không hợp lệ");
        }

        return tokenService.createTokens(user);
    }
}