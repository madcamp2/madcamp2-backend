package com.example.everytask.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @ApiModelProperty(example = "1")
    private int id;
    @ApiModelProperty(example = "한국과학기술원")
    private String name;
    @ApiModelProperty(example = "kaist.ac.kr")
    private String domain;
    @ApiModelProperty(example = "COLLEGE")
    private String type;
}
