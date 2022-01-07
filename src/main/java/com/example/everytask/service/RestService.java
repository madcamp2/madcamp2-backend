package com.example.everytask.service;

import com.example.everytask.model.dao.RestMapper;
import com.example.everytask.model.dto.User;
import com.example.everytask.model.dto.UserSignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestService implements RestServiceInterface {
    private final RestMapper restMapper;

    @Override
    public List<User> getAllUserList(){
        //service 가 mapper 호출
        return restMapper.getAllUserList();
    }

    @Override
    public User getSingleUser(int userId) {
        return restMapper.getSingleUser(userId);
    }

    public boolean userSignUp(UserSignUpForm userSignUpForm) {
        return restMapper.userSignUp(userSignUpForm) > 0;
    }

}
