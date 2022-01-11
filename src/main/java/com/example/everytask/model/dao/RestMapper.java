package com.example.everytask.model.dao;

import com.example.everytask.model.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

//비즈니스 로직 실행에 필요한 쿼리 호출. 해당 프로젝트에서는 DAO 대신 Mapper 사용
@Repository
@org.apache.ibatis.annotations.Mapper
public interface RestMapper {
    @Nullable List<UserObject> getAllUserList();
    @Nullable UserObject getSingleUser(int userId);
    @Nullable UserObject findByEmail(String email);
    void addUser(@Param("item")UserObject userObject);
    @Nullable RefreshTokenMapping isThereRefreshToken(String refreshToken);
    int getIdFromUserEmail(String email);
    @Nullable String getNameFromUserID(int id);
    @Nullable String getNameFromOrgID(int id);
    @Nullable ArrayList<Organization> getOrgsFromUserId(int id);
    @Nullable ArrayList<SimpleUserObject> getFollowersFromUserId(int id);
    @Nullable ArrayList<SimpleUserObject> getFollowsFromUserId(int id);
    @Nullable ArrayList<CourseObject> getCourseListFromKeyword(String keyword);
    int getCourseFollowersFromCourseID(int courseId);
    @Nullable ArrayList<UserToDo> getUserToDo(int userId);
    @Nullable ArrayList<SimpleUserObject> getRecationsFromTaskId(int id);
    int findKakaoId(String kakaoId);
    @Nullable ArrayList<SortedUserView> getSortedPopularView(int num, int currentId, String currentName);
    @Nullable ArrayList<SortedCourseView> getSortedPopularCourseView(int num, int year);
    @Nullable void modifyTask(int num, String contents);
    @Nullable void deleteTask(int task_id, int user_id);
    @Nullable ArrayList<TaskView> retrieveTaskByDate(int user_id, String dateString);
    @Nullable int  findIsUserFollowingCourse(int user_id, int course_id);
    @Nullable void followCourse(int user_id, int course_id);
    @Nullable void unFollowCourse(int user_id, int course_id);
    @Nullable String findCourseName(int course_id);
    @Nullable int findTaskLikes(int task_id);
    @Nullable int findIsUserLikesTask(int user_id, int task_id);
    @Nullable void likesTask(int user_id, int task_id);
    @Nullable void unLikesTask(int user_id, int task_id);
}
