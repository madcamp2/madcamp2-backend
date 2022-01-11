package com.example.everytask.service;

import com.example.everytask.model.dto.KakaoSigninForm;
import com.example.everytask.model.dto.TaskDescription;
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
    DefaultResponse showPopularUsers(int num);
    DefaultResponse showPopularCourses(int num);
    DefaultResponse modifyTask(int taskId, TaskDescription taskDescription);
    DefaultResponse deleteTask(int taskId);
    DefaultResponse retrieveTaskByDate(int user_id, int year, int month, int date);
    DefaultResponse getIdFromToken();
    DefaultResponse followCourse(int course_id);
    DefaultResponse likeTask(int task_id);
}

