package com.example.everytask.controller;


import com.example.everytask.model.dto.User;
import com.example.everytask.model.dto.UserSignUpForm;
import com.example.everytask.model.formats.DefaultResponse;
import com.example.everytask.model.formats.ResponseMessage;
import com.example.everytask.model.formats.StatusCode;
import com.example.everytask.service.RestServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class restController {
    static class ResponseForm{
        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    private final RestServiceInterface service;

    @GetMapping("hello-api")
    public Hello helloAPI(@RequestParam("name") String name){
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello{ //정적 클래스라서 클래스 내에서 재사용가능
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @GetMapping("/test")
    public List<User> test() {
        return service.getAllUserList();
    }

    @GetMapping("/user")
    public User getUser(@RequestParam("id") int userId) {
        return service.getSingleUser(userId);
    }

//    @GetMapping("user/signin")
//    public U

    @PostMapping("user/signup")
    public ResponseEntity userSignUp(@RequestBody UserSignUpForm userSignUpForm) {
        try {
            service.userSignUp(userSignUpForm);
        } catch (Exception e){
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.SIGNUP_FAILED, e), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.CREATED_USER), HttpStatus.OK);
    }

}

