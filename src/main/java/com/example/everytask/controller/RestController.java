package com.example.everytask.controller;


import com.example.everytask.model.dto.KakaoSigninForm;
import com.example.everytask.model.dto.UserRequestTransferObject;
import com.example.everytask.model.formats.DefaultResponse;
import com.example.everytask.service.RestServiceInterface;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {


    private final RestServiceInterface service;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @ApiOperation(value="", notes="")
    @ApiImplicitParam()
    @PostMapping("user/sign-in")
    public DefaultResponse userSignIn(@RequestBody UserRequestTransferObject.SignIn userSignInForm) {
        return service.userSignIn(userSignInForm);
    }

    @PostMapping("user/kakao/signin")
    public DefaultResponse kakaoUserSignIn(KakaoSigninForm kakaoSigninForm){
        return service.kakaoLogin(kakaoSigninForm);
    }

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

    @GetMapping("user/todo")
    public DefaultResponse getUserTodo(@RequestParam("id") int id) {
        return service.getUserToDo(id);
    }

    @PostMapping("user/kakao/sign-in")
    public DefaultResponse kakaoUserLogin(@RequestBody KakaoSigninForm kakaoSigninForm){
        return service.kakaoLogin(kakaoSigninForm);
    }
}

