package com.example.everytask.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseObject {
    @ApiModelProperty(example = "1")
    private int id;
    @ApiModelProperty(example = "COSE474")
    private String course_code;
    @ApiModelProperty(example = "2021")
    private String year;
    @ApiModelProperty(example = "FALL")
    private String semester;
    @ApiModelProperty(example = "01")
    private String division;
    @ApiModelProperty(example = "딥러닝")
    private String course_name;
    @ApiModelProperty(example = "김승룡")
    private String professor_name;
    @ApiModelProperty(example = "우정정보관")
    private String building_name;
    @ApiModelProperty(example = "2")
    private int organization_id;
    @ApiModelProperty(example = "고려대학교")
    private String organization_name;
    @ApiModelProperty(example = "5")
    private int course_followers;
}
