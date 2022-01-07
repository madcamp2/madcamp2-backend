package com.example.everytask.model.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User{
    private int id;
    private String name;
    private String auth_type;
    private String email;
    private String password;
    private String social_id;
}
