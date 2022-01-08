package com.example.everytask.model.dto;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserObject implements UserDetails{

    private int id;
    private String name;
    private String auth_type;
    private String email;
    private String password;
    private ArrayList<GrantedAuthority> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("auth"));
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
