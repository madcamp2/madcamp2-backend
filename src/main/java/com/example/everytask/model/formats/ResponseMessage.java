package com.example.everytask.model.formats;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String TOKEN_FAILED = "유효하지 않은 토큰입니다.";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String LOGOUT_FAIL = "로그아웃 실패: 로그인 정보가 없습니다.";
    public static final String REQUIRES_TOKEN_UPDATE = "토큰 업데이트가 필요합니다.";
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String TOKEN_UPDATED = "토큰 업데이트 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";
    public static final String SIGNUP_FAILED = "회원가입 실패";
    public static final String LOGOUT_SUCCESS = "로그아웃 성공";
    public static final String ALREADY_EXISTS = "이미 존재하는 회원입니다";
    public static final String RESULT_FOUND = "쿼리 결과가 존재합니다.";
    public static final String RESULT_NON_FOUND = "쿼리 결과가 존재하지 않습니다.";
    public static final String LOGIN_REQUIRED = "로그아웃된 상태입니다.";
    public static final String MODIFIED = "수정되었습니다.";
    public static final String DENIED = "권한이 없습니다.";
    public static final String DELETED = "삭제되었습니다.";
}
