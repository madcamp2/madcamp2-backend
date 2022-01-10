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
public class UserSignUpForm {
    @ApiModelProperty(example = "성난 토끼")
    private String name;
    @ApiModelProperty(example = "APP")
    private String auth_type;
    @ApiModelProperty(example = "awwsb41@gmail.com")
    private String email;
    @ApiModelProperty(example = "qwerty1234!")
    private String password;
}