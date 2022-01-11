package com.example.everytask.controller;


import com.example.everytask.model.dto.KakaoSigninForm;
import com.example.everytask.model.dto.TaskDescription;
import com.example.everytask.model.dto.UserRequestTransferObject;
import com.example.everytask.model.dto.UserResponseTransferObject;
import com.example.everytask.model.formats.DefaultResponse;
import com.example.everytask.model.formats.ResponseMessage;
import com.example.everytask.model.formats.StatusCode;
import com.example.everytask.service.RestServiceInterface;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final RestServiceInterface service;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ApiOperation(value="어플리케이션 로그인", notes="이메일과 비밀번호를 로그인이 진행됩니다.")
    @ApiResponses({
            @ApiResponse(code= StatusCode.OK, message = ResponseMessage.LOGIN_SUCCESS, response = UserResponseTransferObject.TokenInfo.class),
            @ApiResponse(code= StatusCode.NOT_FOUND, message = ResponseMessage.NOT_FOUND_USER)
    })
    @PostMapping("user/sign-in")
    public DefaultResponse userSignIn(@RequestBody UserRequestTransferObject.SignIn userSignInForm) {
        return service.userSignIn(userSignInForm);
    }


    @ApiOperation(value="어플리케이션 회원가입", notes="이메일과 비밀번호를 받아 랜덤한 이름으로 회원가입이 진행됩니다. Auth_type은 'APP'이어야 하며, 패스워드는 암호화됩니다.")
    @ApiResponses({
            @ApiResponse(code= StatusCode.OK, message = ResponseMessage.CREATED_USER),
            @ApiResponse(code= StatusCode.BAD_REQUEST, message = ResponseMessage.ALREADY_EXISTS)
    })
    @PostMapping("user/sign-up")
    public DefaultResponse userSignUp(@RequestBody UserRequestTransferObject.SignUp userSignUpForm){
        return service.userSignUp(userSignUpForm);
    }


    @ApiOperation(value="어플리케이션 로그아웃", notes="현재 어플리케이션 이용 컨텍스트상의 유저가 로그아웃됩니다.")
    @ApiResponses({
            @ApiResponse(code= StatusCode.OK, message = ResponseMessage.LOGOUT_SUCCESS),
            @ApiResponse(code= StatusCode.NOT_FOUND, message = ResponseMessage.LOGOUT_FAIL)
    })
    @GetMapping("user/sign-out")
    public DefaultResponse userLogOut(){
        return service.userLogOut();
    }


    @ApiOperation(value="토큰 재발급", notes="엑세스 토큰이 만료된 경우 호출 시 엑세스 토큰을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(code= StatusCode.OK, message = ResponseMessage.TOKEN_UPDATED),
            @ApiResponse(code= StatusCode.BAD_REQUEST, message = ResponseMessage.TOKEN_FAILED)
    })
    @PostMapping("user/reissue")
    public DefaultResponse reissue(@RequestBody UserRequestTransferObject.Reissue reissue){
        return service.reissue(reissue);
    }


    @ApiOperation(value="유저 상세정보", notes="유저의 기본 정보와 속한 학교, 팔로워, 팔로우하는 유저에 대한 정보를 얻어옵니다.")
    @ApiResponses({
            @ApiResponse(code= StatusCode.OK, message = ResponseMessage.RESULT_FOUND),
            @ApiResponse(code= StatusCode.NOT_FOUND, message = ResponseMessage.RESULT_NON_FOUND)
    })
    @GetMapping("user/info")
    public DefaultResponse userInfo(){
        return service.userInfo();
    }


    @ApiOperation(value="과목 검색", notes="과목명과 학수번호를 기준으로 과목을 검색합니다. 검색된 과목들에 대한 정보와 이를 팔로우하는 학생들의 수를 표시합니다.")
    @ApiResponses({
            @ApiResponse(code= StatusCode.OK, message = ResponseMessage.RESULT_FOUND),
            @ApiResponse(code= StatusCode.NOT_FOUND, message = ResponseMessage.RESULT_NON_FOUND)
    })
    @ApiImplicitParam(name="keyword", value = "과목 검색 키워드")
    @GetMapping("course/search")
    public DefaultResponse searchCourse(@RequestParam String keyword){
        return service.searchCourse(keyword);
    }

    @ApiOperation(value="유저 Todo 검색", notes="유저가 계획한 할 일에 대한 정보 및 이에 해당하는 과목, 날짜, 유저들의 반응을 표시합니다.")
    @ApiResponses({
            @ApiResponse(code= StatusCode.OK, message = ResponseMessage.RESULT_FOUND),
            @ApiResponse(code= StatusCode.NOT_FOUND, message = ResponseMessage.RESULT_NON_FOUND)
    })
    @ApiImplicitParam(name="id", value = "검색할 유저의 아이디")
    @GetMapping("user/todo")
    public DefaultResponse getUserTodo(@RequestParam("id") int id) {
        return service.getUserToDo(id);
    }

    @ApiOperation(value="카카오 로그인", notes="클라이언트로부터 카카오 아이디를 넘겨받아 access token과 refresh token을 재생성합니다. 회원 정보가 없을 시 회원 가입이 함께 진행됩니다.")
    @ApiResponses({
            @ApiResponse(code= StatusCode.OK, message = ResponseMessage.LOGIN_SUCCESS),
            @ApiResponse(code= StatusCode.BAD_REQUEST, message = ResponseMessage.LOGIN_FAIL)
    })
    @PostMapping("user/kakao/sign-in")
    public DefaultResponse kakaoUserLogin(@RequestBody KakaoSigninForm kakaoSigninForm){
        return service.kakaoLogin(kakaoSigninForm);
    }

    @GetMapping("user/sort")
    public DefaultResponse showPopularUsers(@RequestParam("num") int num) {
        return service.showPopularUsers(num);
    }

    @GetMapping("subject/sort")
    public DefaultResponse showPopularCourses(@RequestParam("num") int num){
        return service.showPopularCourses(num);
    }

    @PutMapping("task/modify/description/{id}")
    public DefaultResponse changeTaskDescription(@PathVariable("id") int id, @RequestBody TaskDescription taskDescription){
        return service.modifyTask(id, taskDescription);
    }

    @DeleteMapping("task/delete/{id}")
    public DefaultResponse deleteTask(@PathVariable("id") int id){
        logger.info("엥?");
        return service.deleteTask(id);
    }

    @GetMapping("task/date") //!!
    public DefaultResponse retrieveTaskByDate(@RequestParam("id") int user_id, @RequestParam("year") int year, @RequestParam("month") int month, @RequestParam("day") int day){
        return service.retrieveTaskByDate(user_id, year, month, day);
    }

    @GetMapping("user/get-id")
    public DefaultResponse getIdFromToken(){
        return service.getIdFromToken();
    }

    @GetMapping("course/follow")
    public DefaultResponse followCourse(@RequestParam("course_id") int course_id){
        return service.followCourse(course_id);
    }

    @GetMapping("task/like")
    public DefaultResponse likeTask(@RequestParam("task_id") int task_id){
        logger.info("dpd??");
        return service.likeTask(task_id);
    }


}

