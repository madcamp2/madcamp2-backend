package com.example.everytask.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserRequestTransferObject {
    @Getter
    @Setter
    public static class SignUp {
        //String email, String password
        @ApiModelProperty(example = "awwsb41@gmail.com")
        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @ApiModelProperty(example = "kdljfieek21@")
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        @ApiModelProperty(example = "APP")
        private String auth_type;
    }

    @Getter
    @Setter
    public static class SignIn {
        @ApiModelProperty(example = "awwsb41@gmail.com")
        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @ApiModelProperty(example = "dksdjkh$$33")
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        //signInForm의 데이터(아이디, 비밀번호)기반으로 authentication 객체로 변환. service의 signin 함수에서 참조함
        public UsernamePasswordAuthenticationToken toAuthenticationObject() {
            //principal(인증받으려는 사람), credentials 를 인자로 받음
            //인증객체 생성
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Getter
    @Setter
    public static class UserInfo {
        @NonNull
        @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDIzNDM2ODd9.vHWbsVggDgorFKPuemf_cQJt4ZtztpxcYm2tK3bltRY")
        private String accessToken;
    }

    @Getter
    @Setter
    public static class Reissue {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxcXFxQGhlbGxvLmNvbSIsImF1dGgiOiJhdXRoIiwiZXhwIjoxNjQxODI5ODgyfQ.4KrbRFAqHuXgw39H6rcuW2AyanNtHKWMggzsc2KOzYs")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDIzNDM2ODd9.vHWbsVggDgorFKPuemf_cQJt4ZtztpxcYm2tK3bltRY")
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class Logout {
        @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxcXFxQGhlbGxvLmNvbSIsImF1dGgiOiJhdXRoIiwiZXhwIjoxNjQxODI5ODgyfQ.4KrbRFAqHuXgw39H6rcuW2AyanNtHKWMggzsc2KOzYs")
        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDIzNDM2ODd9.vHWbsVggDgorFKPuemf_cQJt4ZtztpxcYm2tK3bltRY")
        @NotEmpty(message = "잘못된 요청입니다.")
        private String refreshToken;
    }
}
