package com.example.everytask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseObject {
    private int id;
    private String course_code;
    private String year;
    private String semester;
    private String division;
    private String course_name;
    private String professor_name;
    private String building_name;
    private int organization_id;
    private String organization_name;
    private int course_followers;
}
