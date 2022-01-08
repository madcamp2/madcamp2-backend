package com.example.everytask.service;

import com.example.everytask.jwt.JwtTokenProvider;
import com.example.everytask.model.dao.RestMapper;
import com.example.everytask.model.dto.*;

import com.example.everytask.model.formats.DefaultResponse;
import com.example.everytask.model.formats.ResponseMessage;
import com.example.everytask.model.formats.StatusCode;
import com.example.everytask.trivialModules.NameCreate;
import lombok.RequiredArgsConstructor;

import org.apache.catalina.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RestService implements RestServiceInterface {
    private final RestMapper restMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder; //?
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;
    private final NameCreate nameCreate;

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
        UserObject userObject = UserObject.builder()
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .auth_type(signUpForm.getAuth_type())
                .name("nameCreate.randomName()")
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

        redisTemplate.opsForValue()
                .set("RT:"+authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        //토큰과 response code 200을 반환
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenInfo);
    }

    public DefaultResponse reissue(UserRequestTransferObject.Reissue reissue) {
        //Refreshtoken검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())){
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.TOKEN_FAILED);
        }
        //access token에서 user email을 가져온다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        String refreshToken = (String) redisTemplate.opsForValue().get("RT:"+authentication.getName());
        if (!refreshToken.equals(reissue.getRefreshToken())){
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.TOKEN_FAILED);
        }

        UserResponseTransferObject.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return DefaultResponse.res(StatusCode.OK, ResponseMessage.TOKEN_UPDATED, tokenInfo);
    }

    public DefaultResponse userInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            //토큰 정보가 아예 소실된 경우
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.TOKEN_FAILED);
        }
        if (authentication.getName().equals("anonymousUser")){
            return DefaultResponse.res(StatusCode.NEED_REFRESH, ResponseMessage.REQUIRES_TOKEN_UPDATE);
        }
        //유저 id 받아옴
        int userId = restMapper.getIdFromUserEmail(authentication.getName());
        UserDetail userDetail = UserDetail.builder()
                .email(authentication.getName())
                .name(restMapper.getNameFromUserID(userId))
                .organizations(restMapper.getOrgsFromUserId(userId))
                .followers(restMapper.getFollowersFromUserId(userId))
                .follows(restMapper.getFollowsFromUserId(userId)).build();
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_USER, userDetail);
    }


    public DefaultResponse refreshToken(UserRequestTransferObject.Reissue reissue){
        RefreshTokenMapping refreshTokenMapping;
        //1. Expiration이 0보다 작은 경우. 즉 access token이 만료된 경우
        if (jwtTokenProvider.getExpiration(reissue.getAccessToken()) < 0) {
            //2. Refreshtoken을 저장한 테이블에 refreshtoken이 있는지 확인
            refreshTokenMapping = restMapper.isThereRefreshToken(reissue.getRefreshToken());
            if (refreshTokenMapping == null){
                //3-1. Refreshtoken이 없으면 로그인을 다시 해야 하는 유저거나 없던 유저
                return DefaultResponse.res(111, "boom");
            }
            //3-2. RefreshToken이 있으면 이를 기반으로 다시 accesstoken을 만들어줘야 함.
        }
        return DefaultResponse.res(111, "boom");
    }

    public DefaultResponse searchCourse(String keyword) {
        ArrayList<CourseObject> courseObjects = new ArrayList<CourseObject>();
        courseObjects = restMapper.getCourseListFromKeyword(keyword);
        if (courseObjects.size() < 1) return DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.RESULT_NON_FOUND);
        for (int i = 0; i < courseObjects.size(); i++){
            courseObjects.get(i).setOrganization_name(restMapper.getNameFromOrgID(courseObjects.get(i).getOrganization_id()));
        }
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.RESULT_FOUND, courseObjects);
    }

}
