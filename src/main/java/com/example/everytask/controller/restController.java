package com.example.everytask.controller;


import com.example.everytask.model.dto.UserObject;
import com.example.everytask.model.dto.UserRequestTransferObject;
import com.example.everytask.model.formats.DefaultResponse;
import com.example.everytask.model.formats.ResponseMessage;
import com.example.everytask.model.formats.StatusCode;
import com.example.everytask.service.RestServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class restController {

    private final RestServiceInterface service;

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
}

