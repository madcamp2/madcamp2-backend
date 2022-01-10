package com.example.everytask.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    @ApiModelProperty(example = "awwsb41@gmail.com")
    private String email;
    @ApiModelProperty(example = "배고픈 호랑이")
    private String name;
    @ApiModelProperty(example = "[\t\t\t{\n" +
            "\t\t\t\t\"id\": 1,\n" +
            "\t\t\t\t\"name\": \"한국과학기술원\",\n" +
            "\t\t\t\t\"domain\": \"kaist.ac.kr\",\n" +
            "\t\t\t\t\"type\": \"COLLEGE\"\n" +
            "\t\t\t} ..]")
    private ArrayList<Organization> organizations;
    @ApiModelProperty(example = "")
    private ArrayList<SimpleUserObject> followers;
    @ApiModelProperty(example = "")
    private ArrayList<SimpleUserObject> follows;
}
