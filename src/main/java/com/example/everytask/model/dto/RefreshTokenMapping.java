package com.example.everytask.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenMapping {
    @ApiModelProperty(example = "awwsb41@gmail.com")
    private String email;
    @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDIzNDM2ODd9.vHWbsVggDgorFKPuemf_cQJt4ZtztpxcYm2tK3bltRY")
    private String refreshToken;
}
