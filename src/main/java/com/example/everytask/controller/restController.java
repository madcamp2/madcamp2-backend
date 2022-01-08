package com.example.everytask.controller;


import com.example.everytask.model.dto.UserObject;
import com.example.everytask.model.dto.UserRequestTransferObject;
import com.example.everytask.model.formats.DefaultResponse;
import com.example.everytask.model.formats.ResponseMessage;
import com.example.everytask.model.formats.StatusCode;
import com.example.everytask.service.RestServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class restController {

    private final RestServiceInterface service;
//
//    @GetMapping("/api/hello")
//    public DefaultResponse hello() {
//       return DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS);
//    }
//
//    @GetMapping("hello-api")
//    public Hello helloAPI(@RequestParam("name") String name){
//        Hello hello = new Hello();
//        hello.setName(name);
//        return hello;
//    }
//
//    static class Hello{ //정적 클래스라서 클래스 내에서 재사용가능
//        private String name;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//    }
//
//    @GetMapping("/test")
//    public List<UserObject> test() {
//        return service.getAllUserList();
//    }
//
//    @GetMapping("/user")
//    public UserObject getUser(@RequestParam("id") int userId) {
//        return service.getSingleUser(userId);
//    }

    @PostMapping("user/sign-in")
    public DefaultResponse userSignIn(@RequestBody UserRequestTransferObject.SignIn userSignInForm) {
        return service.userSignIn(userSignInForm);
    }

    @PostMapping("user/sign-up")
    public DefaultResponse userSignUp(@RequestBody UserRequestTransferObject.SignUp userSignUpForm){
        System.out.println(1);
        return service.userSignUp(userSignUpForm);
    }

}

