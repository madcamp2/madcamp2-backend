package com.example.everytask.model.dao;

import com.example.everytask.model.dto.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.javassist.Loader;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//비즈니스 로직 실행에 필요한 쿼리 호출. 해당 프로젝트에서는 DAO 대신 Mapper 사용
@Repository
@org.apache.ibatis.annotations.Mapper
public interface RestMapper {
    List<UserObject> getAllUserList();
    UserObject getSingleUser(int userId);
    UserObject findByEmail(String email);
    void addUser(@Param("item")UserObject userObject);
    RefreshTokenMapping isThereRefreshToken(String refreshToken);
    int getIdFromUserEmail(String email);
    String getNameFromUserID(int id);
    String getNameFromOrgID(int id);
    ArrayList<Organization> getOrgsFromUserId(int id);
    ArrayList<SimpleUserObject> getFollowersFromUserId(int id);
    ArrayList<SimpleUserObject> getFollowsFromUserId(int id);
    ArrayList<CourseObject> getCourseListFromKeyword(String keyword);
}
