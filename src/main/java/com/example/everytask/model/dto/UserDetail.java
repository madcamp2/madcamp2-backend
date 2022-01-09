package com.example.everytask.model.dto;


import lombok.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    private String email;
    private String name;
    private ArrayList<Organization> organizations;
    private ArrayList<SimpleUserObject> followers;
    private ArrayList<SimpleUserObject> follows;
}
