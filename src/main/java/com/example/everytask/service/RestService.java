package com.example.everytask.service;

import com.example.everytask.jwt.JwtTokenProvider;
import com.example.everytask.model.dao.RestMapper;
import com.example.everytask.model.dto.UserObject;
import com.example.everytask.model.dto.UserRequestTransferObject;
import com.example.everytask.model.dto.UserResponseTransferObject;

import com.example.everytask.model.formats.Authority;
import com.example.everytask.model.formats.DefaultResponse;
import com.example.everytask.model.formats.ResponseMessage;
import com.example.everytask.model.formats.StatusCode;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestService implements RestServiceInterface {
    private final RestMapper restMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder; //?
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserObject> getAllUserList(){
        //service 가 mapper 호출
        return restMapper.getAllUserList();
    }

    @Override
    public UserObject getSingleUser(int userId) {
        return restMapper.getSingleUser(userId);
    }

    public DefaultResponse userSignUp(UserRequestTransferObject.SignUp signUpForm) {
        if (restMapper.findByEmail(signUpForm.getEmail()) != null){
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.ALREADY_EXISTS);
        }
        if (signUpForm.getName() == null) signUpForm.setName("익명유저");
        signUpForm.setAuth_type("APP");
        UserObject userObject = UserObject.builder()
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .auth_type(signUpForm.getAuth_type())
                .name(signUpForm.getName())
                .build();
        restMapper.addUser(userObject);
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.CREATED_USER);
    }

    public DefaultResponse userSignIn(@Validated UserRequestTransferObject.SignIn signInForm){
        if (restMapper.findByEmail(signInForm.getEmail()) == null) {
            return DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        }

        //1. signInForm의 데이터(아이디, 비밀번호)기반으로 authentication 객체로 변환
        //아직 인증된 상태는 아니고 인증을 위한 객체로만 변환된 것
        UsernamePasswordAuthenticationToken authenticationToken = signInForm.toAuthenticationObject();
        //2. 아이디랑 비밀번호로 우선 인증 객체를 생성한 뒤, 비밀번호가 맞는지를 검증하는 단계 - authenticationManager 객체를 받아서 authenticate 메서드 실행하면
        //CustomerUserDetailService 에서 만든 loadUserByName 메서드 실행. 즉 db에 접근해서 사용자 정보를 가지고 온다.
        //Spring Security 동작원리상 AuthenticationManager interface 를 구현한 ProviderManager 클래스의 authenticate() 메서드가 동작
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


        //3. authentication이 정상적으로 이루어지면 받아온 인증 정보 기반으로 토큰을 생성
        UserResponseTransferObject.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);


        //redis 구현해야 함

        //토큰과 response code 200을 반환
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenInfo);
    }

}
