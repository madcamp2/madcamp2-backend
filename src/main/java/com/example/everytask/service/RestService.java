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
import reactor.util.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.*;
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
                .name(nameCreate.randomName())
                .build();
        restMapper.addUser(userObject);
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.CREATED_USER);
    }

    public DefaultResponse userSignIn(@Validated UserRequestTransferObject.SignIn signInForm){
        if (restMapper.findByEmail(signInForm.getEmail()) == null) {
            return DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        }

        UsernamePasswordAuthenticationToken authenticationToken = signInForm.toAuthenticationObject();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        UserResponseTransferObject.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue()
                .set("RT:"+authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenInfo);
    }

    public DefaultResponse reissue(UserRequestTransferObject.Reissue reissue) {

        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())){
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.TOKEN_FAILED);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        String refreshToken = (String) redisTemplate.opsForValue().get("RT:"+authentication.getName());
        if (!refreshToken.equals(reissue.getRefreshToken())){
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.TOKEN_FAILED);
        }

        UserResponseTransferObject.TokenInfo tokenInfo = jwtTokenProvider.updateToken(authentication, refreshToken);

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

        int userId = restMapper.getIdFromUserEmail(authentication.getName());
        UserDetail userDetail = UserDetail.builder()
                .email(authentication.getName())
                .name(restMapper.getNameFromUserID(userId))
                .organizations(restMapper.getOrgsFromUserId(userId))
                .followers(restMapper.getFollowersFromUserId(userId))
                .follows(restMapper.getFollowsFromUserId(userId))
                .build();
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_USER, userId);
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
            courseObjects.get(i).setCourse_followers(restMapper.getCourseFollowersFromCourseID(courseObjects.get(i).getId()));
        }
        Collections.sort(courseObjects, new Comparator<CourseObject>() {
            @Override
            public int compare(CourseObject o1, CourseObject o2) {
                int o1f = o1.getCourse_followers();
                int o2f = o2.getCourse_followers();
                return -Integer.compare(o1f, o2f);
            }
        });
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.RESULT_FOUND, courseObjects);
    }

    public DefaultResponse getUserToDo(int userId){
        String userName = restMapper.getNameFromUserID(userId);
        ArrayList<UserToDo> userTasks = restMapper.getUserToDo(userId);
        for (UserToDo userTask : userTasks) {
            userTask.setUser_name(userName);
            userTask.setReactionList(restMapper.getRecationsFromTaskId(userTask.getId()));
        }
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.RESULT_FOUND, userTasks);
    }

}
