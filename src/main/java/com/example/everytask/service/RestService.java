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
    public UserObject getSingleUser(int userId) {
        return restMapper.getSingleUser(userId);
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

    public DefaultResponse userSignUp(UserRequestTransferObject.SignUp signUpForm) {
        try {
            UserObject userObject = UserObject.builder()
                    .email(signUpForm.getEmail())
                    .password(passwordEncoder.encode(signUpForm.getPassword()))
                    .auth_type(signUpForm.getAuth_type())
                    .name(nameCreate.randomName())
                    .build();
            restMapper.addUser(userObject);
        } catch(Exception e) {
            e.printStackTrace();
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.ALREADY_EXISTS);
        }
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.CREATED_USER);
    }

    public DefaultResponse userLogOut(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == null || restMapper.findByEmail(authentication.getName()) == null){
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGOUT_FAIL);
        }
        SecurityContextHolder.clearContext();
        redisTemplate.opsForValue().set("", ""); //refresh token 비워줌
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGOUT_SUCCESS);
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
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_USER, userDetail);
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

    public DefaultResponse kakaoLogin(KakaoSigninForm kakaoSigninForm){
        String stringId = kakaoSigninForm.getKakaoId() + "@kakao.co.kr";
        String userPasswd = kakaoSigninForm.getKakaoId() + "rand";
        if (restMapper.findKakaoId(stringId) <= 0) {
            //없는 회원인 경우 새로 가입
            try {
                UserObject userObject = UserObject.builder()
                        .email(stringId)
                        .password(passwordEncoder.encode(userPasswd))
                        .auth_type("KAKAO")
                        .name(nameCreate.randomName())
                        .build();
                restMapper.addUser(userObject);
            } catch(Exception e) {
                e.printStackTrace();
                return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.ALREADY_EXISTS);
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(stringId, userPasswd);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        UserResponseTransferObject.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        redisTemplate.opsForValue()
                .set("RT:"+authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenInfo);
    }

    public DefaultResponse showPopularUsers(int num){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == null || restMapper.findByEmail(authentication.getName()) == null){
            return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_REQUIRED);
        }
        String currentName = authentication.getName();
        int currentId = restMapper.getIdFromUserEmail(currentName);
        ArrayList<SortedUserView> sortedUserView = restMapper.getSortedPopularView(num, currentId, currentName);
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.RESULT_FOUND, currentId);
    }

    public DefaultResponse showPopularCourses(int num){
        ArrayList<SortedCourseView> sortedCourseView = restMapper.getSortedPopularCourseView(num, 2021);
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.RESULT_FOUND, sortedCourseView);
    }

    public DefaultResponse modifyTask(int taskId, TaskDescription taskDescription){
        restMapper.modifyTask(taskId, taskDescription.getDescription());
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.MODIFIED);
    }

    public DefaultResponse deleteTask(int taskId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == null || restMapper.findByEmail(authentication.getName()) == null){
            return DefaultResponse.res(StatusCode.UNAUTHORIZED, ResponseMessage.DENIED);
        }
        UserObject owner = restMapper.findByEmail(authentication.getName());
        restMapper.deleteTask(taskId, owner.getId());
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.DELETED);
    }

    public DefaultResponse retrieveTaskByDate(int user_id, int year, int month, int date){
        String dateString = year+"-";
        dateString += (month < 10) ? "0"+month+"-" : month+"-";
        dateString += (date < 10) ? "0"+date+"" : date+"";
        ArrayList<TaskView> searchResult = restMapper.retrieveTaskByDate(user_id, dateString);
        if (searchResult == null){
            return DefaultResponse.res(StatusCode.NO_CONTENT, ResponseMessage.RESULT_NON_FOUND);
        }
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.RESULT_FOUND, searchResult);
    }

    public DefaultResponse getIdFromToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == null || restMapper.findByEmail(authentication.getName()) == null){
            return DefaultResponse.res(StatusCode.UNAUTHORIZED, ResponseMessage.DENIED);
        }
        int myId = restMapper.getIdFromUserEmail(authentication.getName());
        return DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_USER, myId);
    }
}
