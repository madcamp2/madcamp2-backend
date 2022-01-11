package com.example.everytask.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SortedCourseView {
    private int course_id;
    private String course_name;
    private int followers;
}
