# MadCamp — 2

# 카이스트 몰입캠프 2주차 프로젝트

## 1. Project Description

카이스트 몰입캠프 2주차 프로젝트입니다. 

## 2. Getting Started

### dependencies

```java
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.0'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	runtimeOnly 'mysql:mysql-connector-java'
	compileOnly 'org.projectlombok:lombok:1.18.22'
	annotationProcessor 'org.projectlombok:lombok:1.18.22'
	testCompileOnly 'org.projectlombok:lombok:1.18.22'
	testAnnotationProcessor 'org.projectlombok:lombok:1.18.22'
	implementation 'javax.xml.bind:jaxb-api'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.security:spring-security-test'
	implementation 'org.springframework.boot:spring-boot-starter-validation' //유효성 검증관련. @NotNull 등
	runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.2'
	runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.2'
	implementation 'io.jsonwebtoken:jjwt-api:0.11.2'
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	annotationProcessor "org.springframework.boot:spring-boot-configuration-processor"
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
	implementation group: 'com.google.code.gson', name: 'gson', version: '2.8.2'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.projectreactor:reactor-spring:1.0.1.RELEASE'
	implementation 'io.springfox:springfox-swagger2:2.9.2'
	implementation 'io.springfox:springfox-swagger-ui:2.9.2'
	implementation 'com.google.code.findbugs:jsr305:3.0.2'
}
```

### Add to Service

```bash
build  build.gradle  gradle  gradlew  gradlew.bat  settings.gradle  src
root@camp-3:~# ./gradlew build
Starting a Gradle Daemon (subsequent builds will be faster)

> Task :compileJava
Note: /root/src/main/java/com/example/everytask/service/RestService.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.

BUILD SUCCESSFUL in 16s
7 actionable tasks: 6 executed, 1 up-to-date
root@camp-3:~# sudo systemctl daemon-reload
root@camp-3:~# sudo systemctl enable everyTask.service
root@camp-3:~# sudo systemctl start everyTask.service
root@camp-3:~# sudo systemctl status everyTask.service
● everyTask.service - server start
   Loaded: loaded (/etc/systemd/system/everyTask.service; enabled; vendor preset
   Active: active (running) since Tue 2022-01-11 06:54:59 UTC; 4h 48min ago
 Main PID: 28137 (java)
    Tasks: 40 (limit: 4701)
   CGroup: /system.slice/everyTask.service
           └─28137 java -jar /root/build/libs/everytask-0.0.1-SNAPSHOT.jar
```

### 3. Database

![image](https://user-images.githubusercontent.com/82388712/151817399-927ca021-881f-4a53-a0fd-afe450d04f87.png)

![https://github.com/madcamp2/madcamp2-backend/blob/main/folder/database.png](https://github.com/madcamp2/madcamp2-backend/blob/main/folder/database.png)

## 4. Functionality & Implementation

### API Documentation

[Swagger UI](http://192.249.18.137/swagger-ui.html)

### 로그인

로그인은 카카오 아이디로 로그인 혹은 어플리케이션 회원가입을 통한 계정 생성 후의 로그인이 있다. 이 때 카카오톡 아이디를 통해 생성한 계정은 카카오톡 토큰을 이용해 얻어온 uid를 기반으로 새로운 암호화 과정을 거쳐 Json Web Token을 지급받게 된다. 

```java
if (restMapper.findKakaoId(stringId) <= 0) {
    try {
        UserObject userObject = UserObject.builder()
                .email(stringId)
                .password(passwordEncoder.encode(userPasswd))
                .auth_type("KAKAO")
                .name(nameCreate.randomName())
                .build();
        restMapper.addUser(userObject);
    } catch(Exception e) {
        e.printStackTrace();
        return DefaultResponse.res(StatusCode.BAD_REQUEST, ResponseMessage.ALREADY_EXISTS);
    }
}
```
![https://github.com/madcamp2/madcamp2-backend/blob/main/folder/KakaoTalk_Photo_2022-01-11-22-51-15%20006.png](https://github.com/madcamp2/madcamp2-backend/blob/main/folder/KakaoTalk_Photo_2022-01-11-22-51-15%20006.png)

자체적으로 생성된 계정은 이메일과 비밀번호를 기반으로 암호화되며, 이후 비밀번호는 단방향 복호화 과정을 거쳐 데이터베이스에 등록된다. 이렇게 데이터베이스에 등록된 암호는 Spring Security기반으로 유저의 토큰에 담긴 정보와 함께 인증 절차에 사용된다.

![https://github.com/madcamp2/madcamp2-backend/blob/main/folder/database2.png](https://github.com/madcamp2/madcamp2-backend/blob/main/folder/database2.png)
![https://github.com/madcamp2/madcamp2-backend/blob/main/folder/KakaoTalk_Photo_2022-01-11-22-51-15%20001.png](https://github.com/madcamp2/madcamp2-backend/blob/main/folder/KakaoTalk_Photo_2022-01-11-22-51-15%20001.png)
### 과목 검색

과목 검색은 제목 혹은 학수번호를 통해 이루어지며 검색 결과는 과목에 좋아요를 표시한 학생 수, 분반 등의 정보를 포함하여 조회됩니다. 팔로우 수가 가장 많은 과목은 홈 화면에 학교와 관계없이 출력됩니다.

### 자신 및 다른 유저의 task보기
![https://github.com/madcamp2/madcamp2-backend/blob/main/folder/KakaoTalk_Photo_2022-01-11-22-51-15%20005.png](https://github.com/madcamp2/madcamp2-backend/blob/main/folder/KakaoTalk_Photo_2022-01-11-22-51-15%20005.png)
![]
모든 유저들의 작업은 과목별로 분류됩니다. 유저들은 같은 과목을 수강하거나 공부하는 유저들의  Task를 보고 이에 반응할 수 있습니다.

### 할 일 추가 및 점검

수강 과목 및 날짜별로 할 일을 정리하고 관리할 수 있습니다. 자신을 팔로우한 유저나 같은 과목을 수강하는 유저들의 반응을 볼 수 있습니다.

### 유저 프로필 보기

유저의 팔로워 수, 팔로우하는 유저 수, 소속된 학교 등의 정보를 볼 수 있습니다. 유저의 닉네임은 랜덤한 모듈로 생성되며 이에 상응하는 사진이 프로필 사진으로 등록됩니다.
