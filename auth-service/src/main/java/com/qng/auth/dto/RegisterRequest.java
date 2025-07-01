package com.qng.auth.dto;

public record RegisterRequest(String username, String password, String email, String fullName) {
}
