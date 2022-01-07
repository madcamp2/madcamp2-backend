package com.example.everytask.model.dao;

import com.example.everytask.model.dto.User;
import com.example.everytask.model.dto.UserSignUpForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//비즈니스 로직 실행에 필요한 쿼리 호출. 해당 프로젝트에서는 DAO 대신 Mapper 사용
@Repository
@org.apache.ibatis.annotations.Mapper
public interface RestMapper {
    List<User> getAllUserList();
    User getSingleUser(int userId);
    int userSignUp(@Param("item") UserSignUpForm userSignUpForm);
}
