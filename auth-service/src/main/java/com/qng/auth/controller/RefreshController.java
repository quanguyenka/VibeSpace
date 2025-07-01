package com.qng.auth.controller;

import com.qng.auth.entity.RefreshToken;
import com.qng.auth.repo.RefreshTokenRepository;
import com.qng.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RefreshController {
    private final RefreshTokenRepository refreshTokenRepo;
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String token = request.get("refreshToken");
        if (token == null) {
            return ResponseEntity.badRequest()
                    .body("Refresh token không được để trống");
        }

        try {
            RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Token không hợp lệ"));

            String newAccessToken = tokenService.generateNewAccessToken(refreshToken);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}