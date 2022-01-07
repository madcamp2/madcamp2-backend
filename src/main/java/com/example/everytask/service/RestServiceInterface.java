package com.example.everytask.service;

import com.example.everytask.model.dto.User;
import com.example.everytask.model.dto.UserSignUpForm;

import java.util.List;

public interface RestServiceInterface {
    List<User> getAllUserList();
    User getSingleUser(int userId);
    boolean userSignUp(UserSignUpForm userSignUpForm);
}

