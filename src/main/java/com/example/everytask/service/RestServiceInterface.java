package com.example.everytask.service;

import com.example.everytask.model.dto.KakaoSigninForm;
import com.example.everytask.model.dto.UserObject;
import com.example.everytask.model.dto.UserRequestTransferObject;
import com.example.everytask.model.formats.DefaultResponse;

import java.util.List;

public interface RestServiceInterface {
    UserObject getSingleUser(int userId);
    DefaultResponse userSignUp(UserRequestTransferObject.SignUp signUpForm);
    DefaultResponse userSignIn(UserRequestTransferObject.SignIn userSignInForm);
    DefaultResponse reissue(UserRequestTransferObject.Reissue reissue);
    DefaultResponse userInfo();
    DefaultResponse searchCourse(String keyword);
    DefaultResponse getUserToDo(int userId);
    DefaultResponse userLogOut();
    DefaultResponse kakaoLogin(KakaoSigninForm kakaoSigninForm);
}

