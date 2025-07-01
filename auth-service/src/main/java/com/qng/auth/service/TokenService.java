package com.qng.auth.service;

import com.qng.auth.entity.RefreshToken;
import com.qng.auth.entity.UserAccount;
import com.qng.auth.repo.RefreshTokenRepository;
import com.qng.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepo;
    private final JwtUtil jwtUtil;

    public Map<String, String> createTokens(UserAccount user) {
        String accessToken = jwtUtil.generateToken(user.getUsername());
        RefreshToken refreshToken = getOrCreateRefreshToken(user);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken()
        );
    }

    private RefreshToken getOrCreateRefreshToken(UserAccount user) {
        return refreshTokenRepo.findByUser(user)
                .map(token -> {
                    if (isRefreshTokenExpired(token)) {
                        refreshTokenRepo.deleteByUser(user);
                        return createNewRefreshToken(user);
                    }
                    return token;
                })
                .orElseGet(() -> createNewRefreshToken(user));
    }

    private RefreshToken createNewRefreshToken(UserAccount user) {
        RefreshToken newToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusSeconds(60 * 60 * 24 * 7)) // 7 ngày
                .build();
        return refreshTokenRepo.save(newToken);
    }

    public boolean isRefreshTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public String generateNewAccessToken(RefreshToken refreshToken) {
        if (isRefreshTokenExpired(refreshToken)) {
            refreshTokenRepo.deleteByUser(refreshToken.getUser());
            throw new RuntimeException("Refresh token đã hết hạn");
        }
        return jwtUtil.generateToken(refreshToken.getUser().getUsername());
    }
}