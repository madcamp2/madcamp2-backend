package com.example.everytask.service;

import com.example.everytask.model.dao.RestMapper;
import com.example.everytask.model.dto.UserObject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RestMapper restMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return restMapper.findByEmail(username)
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        UserObject users = restMapper.findByEmail(username);
        if (users == null){
            throw new UsernameNotFoundException("없는 유저입니다");
        }
        return users;
    }

//    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
//    private UserDetails createUserDetails(UserObject users) {
//        return new UserObject(users.getUsername(), users.getPassword(), users.getAuthorities());
//    }
}
