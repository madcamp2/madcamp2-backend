package com.example.everytask.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseTransferObject {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        @ApiModelProperty(example = "Bearer")
        private String grantType; // Bearer ( OAuth, Jwt )
        @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxcXFxQGhlbGxvLmNvbSIsImF1dGgiOiJhdXRoIiwiZXhwIjoxNjQxODI5ODgyfQ.4KrbRFAqHuXgw39H6rcuW2AyanNtHKWMggzsc2KOzYs")
        private String accessToken;
        @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDI0MzEwODJ9.i8bVJ8naKvVP6JtjxSlgRpoQ_HUQzLCabSCFGS33Siw")
        private String refreshToken;
        @ApiModelProperty(example = "604800000")
        private Long refreshTokenExpirationTime;
    }
}
