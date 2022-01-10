package com.example.everytask.controller;


import com.example.everytask.model.dto.UserObject;
import com.example.everytask.model.dto.UserRequestTransferObject;
import com.example.everytask.model.formats.DefaultResponse;
import com.example.everytask.model.formats.ResponseMessage;
import com.example.everytask.model.formats.StatusCode;
import com.example.everytask.service.KakaoSigninService;
import com.example.everytask.service.RestServiceInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class restController {

    private final RestServiceInterface service;
    private final KakaoSigninService kakaoSigninService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //로그인
    @PostMapping("user/sign-in")
    public DefaultResponse userSignIn(@RequestBody UserRequestTransferObject.SignIn userSignInForm) {
        return service.userSignIn(userSignInForm);
    }

    //회원가입
    @PostMapping("user/sign-up")
    public DefaultResponse userSignUp(@RequestBody UserRequestTransferObject.SignUp userSignUpForm){
        return service.userSignUp(userSignUpForm);
    }

    @GetMapping("user/log-out")
    public DefaultResponse userLogOut(){
        return service.userLogOut();
    }

    //토큰 재발급
    @PostMapping("user/reissue")
    public DefaultResponse reissue(@RequestBody UserRequestTransferObject.Reissue reissue){
        return service.reissue(reissue);
    }

    @GetMapping("user/info")
    public DefaultResponse userInfo(){
        return service.userInfo();
    }

    @PostMapping("user/refresh-token")
    public DefaultResponse refreshToken(@RequestBody UserRequestTransferObject.Reissue reissue){
        return service.refreshToken(reissue);
    }

    @GetMapping("course/search")
    public DefaultResponse searchCourse(@RequestParam String keyword){
        return service.searchCourse(keyword);
    }

//745ffe68d06ddbf22efb96dc9fc84f47
    @GetMapping("user/todo")
    public DefaultResponse getUserTodo(@RequestParam("id") int id) {
        return service.getUserToDo(id);
    }

    @RequestMapping("user/kakao")
    public String kakaoLogin(@RequestParam("code") String code){
        logger.info("으아아아아아아아아아아!!!");
        String currentToken = kakaoSigninService.getAccessTokenFromKakaoServer(code);
//        return "redirect:webauthcallback://success?customToken="+result.get("customToken").toString();
//        return "redirect:webauthcallback://success?customToken="+currentToken;
//        return "redirect:kakaocc69a0dc9f878280d4bd6b1f87a1dadb://success?customToken="+currentToken;
        return "AUTHCODE :: "+code + "   ACCESSTOKEN :: "+currentToken;
    }


}

