package com.example.everytask.model.dto;


import io.swagger.annotations.ApiModelProperty;
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
public class SimpleUserObject {
    @ApiModelProperty(example = "1")
    private int id;
    @ApiModelProperty(example = "이홍기")
    private String name;
    @ApiModelProperty(example = "honggi@gmail.com")
    private String email;
}
