package com.example.everytask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseTransferObject {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType; // Bearer ( OAuth, Jwt )
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }
}
